package com.mygdx.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.helpers.Direction;
import com.mygdx.helpers.Rotation;

/**
 * Created by d.holuj on 06-Oct-17.
 */

public class Tank {

    Sprite playerSprite;
    Texture playerTexture;
    SpriteBatch batch;
    public long lastShotTime;

    int currentState = Direction.UP;
    private float deltaTime;
    private float speed =100;

    public Tank(SpriteBatch batch) {
        playerTexture = new Texture("player.jpg");
        // Sprite dla gracza przyjmuje texture i 16x16 dlugosc i szerokosc
        playerSprite = new Sprite(playerTexture, 16, 16);
        this.batch = batch;
        lastShotTime = 0;
        this.deltaTime = 0;
    }

    public void draw() {
        batch.begin();
        playerSprite.draw(batch);
        batch.end();
    }

    public void move(int directionMove, float delta) {
        this.deltaTime = delta;
        if (directionMove == Direction.LEFT) {
            setPositionFromRectangle(moveLeft());
        }
        if (directionMove == Direction.RIGHT) {
            setPositionFromRectangle(moveRight());
        }
        if (directionMove == Direction.UP) {
            setPositionFromRectangle(moveUp());
        }
        if (directionMove == Direction.DOWN) {
            setPositionFromRectangle(moveDown());
        }
        changeDirection(directionMove);
    }

    public void setPositionFromRectangle(Rectangle rect) {
        playerSprite.setPosition(rect.x, rect.y);
    }

    public Rectangle moveLeft() {
        return new Rectangle(playerSprite.getX() - speed * this.deltaTime, playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());
    }

    public Rectangle moveRight() {
        return new Rectangle(playerSprite.getX() + speed * this.deltaTime, playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());
    }

    public Rectangle moveUp() {
        return new Rectangle(playerSprite.getX(), playerSprite.getY() + speed * this.deltaTime, playerSprite.getWidth(), playerSprite.getHeight());
    }

    public Rectangle moveDown() {
        return new Rectangle(playerSprite.getX(), playerSprite.getY() - speed * this.deltaTime, playerSprite.getWidth(), playerSprite.getHeight());
    }

    private void changeDirection(int direction) {
        this.currentState = direction;
        if (direction == Direction.LEFT) {
            playerSprite.setRotation(Rotation.LEFT);
        }
        if (direction == Direction.RIGHT) {
            playerSprite.setRotation(Rotation.RIGHT);
        }
        if (direction == Direction.UP) {
            playerSprite.setRotation(Rotation.UP);
        }
        if (direction == Direction.DOWN) {
            playerSprite.setRotation(Rotation.DOWN);
        }

    }

    public int getDirection() {
        return currentState;
    }

    public float getX() {
        return playerSprite.getX();
    }

    public float getY() {
        return playerSprite.getY();
    }

    public void setPosition(int x, int y) {
        playerSprite.setPosition(x, y);
    }

    public Rectangle getRectangle() {
        return new Rectangle(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());
    }

}