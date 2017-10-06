package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.enemies.Bullet;
import com.mygdx.enemies.Tank;
import com.mygdx.helpers.Direction;

import java.util.Iterator;
import java.util.Random;


public class PlayScreen implements Screen {

    private Texture img;
    private MyGame game;
    private Viewport gamePort;
    private OrthographicCamera gameCamera;

    private Tank player;

    private Array<Tank> enemyTanks = new Array<Tank>();
    private Array<Bullet> bullets = new Array<Bullet>();

    public PlayScreen(MyGame game) {
        this.game = game;
        img = new Texture("badlogic.jpg");
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false);
        gamePort = new FillViewport(MyGame.V_WIDTH, MyGame.V_HEIGHT, gameCamera);


        player = new Tank(this.game.batch);
        player.setPosition(MyGame.V_WIDTH/2, MyGame.V_HEIGHT/2);
        for (int i=0; i<4; i++){
            createTankInRandomLocation();
        }
    }

    public void update(float dt) {
        handleKey(dt);
    }

    public void createTank(float x, float y){
        Tank tank = new Tank(this.game.batch);
        tank.setPositionFromRectangle(new Rectangle(x,y,16,16));
        enemyTanks.add(tank);
    }

    public void createTankInRandomLocation(){
        Random random = new Random();
        float x = (float) (random.nextInt(MyGame.V_WIDTH+16) - 32);
        float y = (float) (random.nextInt(MyGame.V_HEIGHT+16) - 32);
        createTank(x, y);
    }
    
    public void handleKey(float dt) {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0);
            gameCamera.unproject(touchPos);
            Rectangle tankPos = player.getRectangle();
            if ((Math.abs(touchPos.x - tankPos.getX())) > (Math.abs(touchPos.y - tankPos.getY()))){
                if (touchPos.x > tankPos.getX()){
                    player.move(Direction.RIGHT, dt, enemyTanks);
                } else {
                    player.move(Direction.LEFT, dt, enemyTanks);
                }
            } else {
                if (touchPos.y > tankPos.getY()){
                    player.move(Direction.UP, dt, enemyTanks);
                } else {
                    player.move(Direction.DOWN, dt, enemyTanks);


                }
            }
//            if (leftButton.contains(new Vector2(touchPos.x,touchPos.y))){
//                Gdx.app.log("Mouse Event","Projected at " + touchPos.x + "," + touchPos.y);
//            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            player.move(Direction.LEFT, dt, enemyTanks);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            player.move(Direction.RIGHT, dt, enemyTanks);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            player.move(Direction.UP, dt, enemyTanks);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            player.move(Direction.DOWN, dt, enemyTanks);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            if (player.lastShotTime + 500 < TimeUtils.millis()){
                bullets.add(new Bullet(game.batch, player.getDirection(), player.getX()+7.5f , player.getY()+7.5f));
                player.lastShotTime = TimeUtils.millis();
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(gameCamera.combined);

        gameCamera.update();
        player.draw();
        for (Tank tank: enemyTanks){
            tank.draw();
        }

        Iterator<Bullet> iter = bullets.iterator();
        while(iter.hasNext()) {
            Bullet bullet = iter.next();
            if (bullet.inGameArea(bullet.getRectangle(), -3)){
                bullet.move();
                bullet.draw();
                Iterator<Tank> tankIter = enemyTanks.iterator();
                while(tankIter.hasNext()){
                    Tank tank = tankIter.next();
                    if (bullet.isCollision(bullet.getRectangle(), tank.getRectangle())){
                        usunCzolg(tank);
                        usunPocisk(bullet);
                        createTankInRandomLocation();
                        break;
                    }
                }
            }else{
                usunPocisk(bullet);
            }
        }

        game.batch.begin();
        //game.batch.draw(img, leftButton.x, leftButton.y);
        game.batch.end();
    }

    private void usunCzolg(Tank czolg){
        enemyTanks.removeIndex(enemyTanks.indexOf(czolg, false));
    }

    private void usunPocisk(Bullet pocisk){
        bullets.removeIndex(bullets.indexOf(pocisk, false));
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        img.dispose();
        game.batch.dispose();
    }
}
