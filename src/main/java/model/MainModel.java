package model;

import model.renderdata.EnemyData;
import model.renderdata.PlayerData;
import model.renderdata.RayData;
import model.renderdata.RenderData;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainModel {
    //TODO change to private
    public final Player player;

    private final int RESOLUTION;
    private final double FOV;

    public AtomicBoolean writingToFirst = new AtomicBoolean(true);

    //TODO change to private
    public Soldier[] enemies = new Soldier[1];

    protected CyclicBarrier barrier;

    public RenderData renderData1;
    public RenderData renderData2;

    public MainModel(int res, double fov, String path, CyclicBarrier barrier) {
        Map.loadMap(path);
        player = new Player(53.5 * Map.getTILE_SIZE(), 62.5 * Map.getTILE_SIZE(), 90, fov, enemies);

        enemies[0] = new Soldier(44.5 * Map.getTILE_SIZE(), 55.5 * Map.getTILE_SIZE(), player);
        /*enemies[1] = new Soldier(45.5 * Map.getTILE_SIZE(), 55.5 * Map.getTILE_SIZE(), player);
        enemies[2] = new Soldier(46.5 * Map.getTILE_SIZE(), 55.5 * Map.getTILE_SIZE(), player);
        enemies[3] = new Soldier(47.5 * Map.getTILE_SIZE(), 55.5 * Map.getTILE_SIZE(), player);*/

        RESOLUTION = res;
        FOV = fov;

        this.barrier = barrier;
    }

    public void update(){
        player.update();
        updateEnemies();
    }

    private void updateEnemies(){
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].update(player.getStandardDirVector(), player.getxCoor(), player.getyCoor());
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
        if (writingToFirst.get()){
            renderData1 = new RenderData(rays, enemies, player);
        } else {
            renderData2 = new RenderData(rays, enemies, player);
        }
    }

    private EnemyData[] prepareEnemyData(){
        EnemyData[] result = new EnemyData[enemies.length];

        for (int i = 0; i < enemies.length; i++) {
            result[i] = renderEnemy(enemies[i]);
        }

        return result;
    }

    private PlayerData preparePlayerData(){
        return new PlayerData(player.getGun().getCurrentSprite(), player.getHP(), player.isDead());
    }

    private EnemyData renderEnemy(Soldier soldier){

        double posInFOV;

        Point enemyVector = new Point(soldier.getX() - player.getxCoor(), -(soldier.getY() - player.getyCoor()));

        Point[] vecs = player.getFOVVectors();

        double angle1 = Point.angle(vecs[0], enemyVector);
        double angle2 = Point.angle(vecs[1], enemyVector);

        if (angle1 + angle2 > 57 && angle1 + angle2 < 63){
            posInFOV = (FOV - angle1) / FOV;
        } else {
            posInFOV = 2;
        }

        return new EnemyData(player.distToEnemy(soldier), posInFOV, soldier.getCurrentSpriteStage(), soldier.getOrientatedSpriteIndex());
        //return result;
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
}
