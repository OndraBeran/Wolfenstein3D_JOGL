package model;

import model.renderdata.*;
import model.sounddata.SoundData;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainModel {
    //TODO change to private
    public Player player;

    private final int RESOLUTION;
    private final double FOV;

    public AtomicBoolean writingToFirst = new AtomicBoolean(true);

    //TODO change to private
    public ArrayList<Soldier> enemies;
    private List<SpriteObject> spriteObjects;

    public CyclicBarrier barrier;

    public RenderData renderData1;
    public RenderData renderData2;

    public SoundData soundData1;
    public SoundData soundData2;

    private int[] finishTile = new int[2];
    private boolean levelFinished = false;
    private boolean nextLevelLoaded = false;

    private int currentLevel = 0;
    private String[] mapPaths;

    public MainModel(int res, double fov, String[] mapPaths, CyclicBarrier barrier) {
        MapLoader.load(mapPaths[currentLevel], this);

        RESOLUTION = res;
        FOV = fov;
        this.mapPaths = mapPaths;

        this.barrier = barrier;
    }

    public void update(){
        if (KeyInputData.isRestart()){
            restart();
            KeyInputData.setRestart(false);
        }

        if (levelFinished){
            currentLevel++;
            loadNextLevel();
            return;
        };

        player.update();

        //check if player is in finish
        if (isInFinish()){
            levelFinished = true;
        }

        updateEnemies();
    }

    private void updateEnemies(){
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(player.getStandardDirVector(), player.getxCoor(), player.getyCoor());
        }
    }

    public void prepareRenderData(){
        /*
        * cast rays
        * get enemy data
        * get player data
        */
        RayData[] rays = castRays();
        SpriteData[] enemies = prepareEnemyData();
        PlayerData player = preparePlayerData();
        GameStateData state = new GameStateData(levelFinished, nextLevelLoaded);

        if (writingToFirst.get()){
            renderData1 = new RenderData(rays, enemies, player, state);
            soundData1 = new SoundData(this.player.getGun().isPlaySound(), false, false);
        } else {
            renderData2 = new RenderData(rays, enemies, player, state);
            soundData2 = new SoundData(this.player.getGun().isPlaySound(), false, false);
        }
    }

    public void prepareSoundData(){
        boolean gunshot = false;
        boolean shouting = false;

        if (player.getGun().isPlaySound()){
            gunshot = true;
            player.getGun().setPlaySound(false);
        }

        for (Soldier soldier :
                enemies) {
            if (soldier.isPlayAchtung()) {
                shouting = true;
                soldier.setPlayAchtung(false);
            }

        }

        if (writingToFirst.get()){
            soundData1 = new SoundData(gunshot, shouting, false);
        } else {
            soundData2 = new SoundData(gunshot, shouting, false);
        }
    }

    private SpriteData[] prepareEnemyData(){
        SpriteData[] result = new SpriteData[enemies.size() + spriteObjects.size()];

        int counter = 0;
        for (Soldier soldier :
                enemies) {
            result[counter] = renderSprite(soldier.getCoordinates(), soldier.getOrientatedSpriteIndex(), soldier.getCurrentSpriteStage());
            counter++;
        }

        for (SpriteObject sprite :
                spriteObjects) {
            result[counter] = renderSprite(sprite.getPosition(), sprite.getIndex(), sprite.getStage());
            counter++;
        }

        Arrays.sort(result, (o1, o2) -> {
            if (o1.distance() == o2.distance()) return 0;
            return o1.distance() > o2.distance() ? -1 : 1;
        });

        return result;
    }

    private PlayerData preparePlayerData(){
        return new PlayerData(player.getGun().getCurrentSprite(), player.getHP(), player.isDead());
    }

    private SpriteData renderSprite(Point pos, int spriteIndex, int spriteStageIndex){

        double posInFOV;

        Point vectorFromPlayer = new Point(pos.getX() - player.getxCoor(), -(pos.getY() - player.getyCoor()));

        double vectorFromPlayerAngle = Point.angleToXAxis(vectorFromPlayer);

        double angleDiff = vectorFromPlayerAngle - player.getAngle();

        if(player.getAngle() >= 0 && player.getAngle() < 90
            && vectorFromPlayerAngle >= 270 && vectorFromPlayerAngle < 360){
            angleDiff -= 360;
        }

        if (player.getAngle() >= 270 && player.getAngle() < 360
            && vectorFromPlayerAngle >= 0 && vectorFromPlayerAngle < 90){
            angleDiff += 360;
        }

        posInFOV = 0.5 - angleDiff / FOV;

        //asi nejhorších 20 rádků co jsem kdy napsal

        Point[] perpendiculars = Point.perpendicularVectors(vectorFromPlayer);

        perpendiculars[0] = Point.changeMagnitude(perpendiculars[0], 32);
        perpendiculars[1] = Point.changeMagnitude(perpendiculars[1], 32);

        Point edge1 = Point.moveByVector(pos, perpendiculars[0]);
        Point edge2 = Point.moveByVector(pos, perpendiculars[1]);

        //double distToPlayer = Double.min(Point.distance(edge1, player.getCoordinates()), Point.distance(edge2, player.getCoordinates()));

        double distToPlayer = Point.distance(player.getCoordinates(), pos);

        distToPlayer *= Math.cos(Math.toRadians(angleDiff));

        if (posInFOV < -0.5 || posInFOV > 1.5) distToPlayer = Double.MAX_VALUE;

        return new SpriteData(distToPlayer, posInFOV, spriteStageIndex, spriteIndex);
    }

    private RayData[] castRays(){
        double startAngle = player.getAngle() - (FOV / 2);
        //must be -1 to account for starting at 0
        double increment = FOV / (RESOLUTION - 1);

        RayData[] data = new RayData[RESOLUTION];

        for (int i = 0; i < RESOLUTION; i++) {

            data[i] = RayCaster.castRenderRay(startAngle);
            startAngle += increment;
        }

        return data;
    }

    private boolean isInFinish(){
        return Point.distance(Map.centerOfTile(finishTile[0], finishTile[1]), player.getCoordinates()) < Map.getTILE_SIZE() / 2;
    }

    public void setEnemies(ArrayList<Soldier> enemies) {
        this.enemies = enemies;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSpriteObjects(List<SpriteObject> spriteObjects) {
        this.spriteObjects = spriteObjects;
    }

    public void setFinishTile(int[] finishTile) {
        this.finishTile = finishTile;
    }

    private void loadNextLevel(){
        if (currentLevel > mapPaths.length - 1){
            currentLevel = 0;
        }
        MapLoader.load(mapPaths[currentLevel], this);
        levelFinished = false;
    }

    private void restart(){
        MapLoader.load(mapPaths[0], this);
        currentLevel = 0;
    }
}
