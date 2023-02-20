package model;

import model.renderdata.*;

import java.util.ArrayList;
import java.util.TreeMap;
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

    protected CyclicBarrier barrier;

    public RenderData renderData1;
    public RenderData renderData2;

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
        EnemyData[] enemies = prepareEnemyData();
        PlayerData player = preparePlayerData();
        GameStateData state = new GameStateData(levelFinished, nextLevelLoaded);

        if (writingToFirst.get()){
            renderData1 = new RenderData(rays, enemies, player, state);
        } else {
            renderData2 = new RenderData(rays, enemies, player, state);
        }
    }

    private EnemyData[] prepareEnemyData(){
        EnemyData[] result = new EnemyData[enemies.size()];

        TreeMap<Double, Soldier> enemyDist = new TreeMap<>();

        for (Soldier soldier :
                enemies) {
            enemyDist.put(soldier.getDistToPlayer(), soldier);
        }

        int counter = 0;
        for (Double key :
                enemyDist.descendingKeySet()) {
            result[counter] = renderEnemy(enemyDist.get(key));
            counter++;
        }
        
        return result;
    }

    private PlayerData preparePlayerData(){
        return new PlayerData(player.getGun().getCurrentSprite(), player.getHP(), player.isDead());
    }

    private EnemyData renderEnemy(Soldier soldier){

        double posInFOV;

        Point enemyVector = new Point(soldier.getX() - player.getxCoor(), -(soldier.getY() - player.getyCoor()));

        double enemyVectAngle = Point.angleToXAxis(enemyVector);

        double angleDiff = enemyVectAngle - player.getAngle();

        if(player.getAngle() >= 0 && player.getAngle() < 90
            && enemyVectAngle >= 270 && enemyVectAngle < 360){
            angleDiff -= 360;
        }

        if (player.getAngle() >= 270 && player.getAngle() < 360
            && enemyVectAngle >= 0 && enemyVectAngle < 90){
            angleDiff += 360;
        }

        if (angleDiff > 0){
            posInFOV = 0.5 - angleDiff / FOV;
        } else {
            posInFOV = 0.5 - (angleDiff / FOV);
        }

        return new EnemyData(player.distToEnemy(soldier), posInFOV, soldier.getCurrentSpriteStage(), soldier.getOrientatedSpriteIndex());
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

    public void setFinishTile(int[] finishTile) {
        this.finishTile = finishTile;
    }

    private void loadNextLevel(){
        MapLoader.load(mapPaths[currentLevel], this);
        levelFinished = false;
    }

    private void restart(){
        MapLoader.load(mapPaths[0], this);
        currentLevel = 0;
    }
}
