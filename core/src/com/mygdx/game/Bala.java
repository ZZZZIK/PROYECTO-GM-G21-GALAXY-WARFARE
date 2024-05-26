package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bala extends ObjetoEspacial {

    private Vector2 speed;
    
    public Bala(float x, float y, int xSpeed, int ySpeed, Texture tx) {

        
        super(x, y, 10, tx,0,1); // Assuming the bullet size is 10
        this.speed = new Vector2(xSpeed, ySpeed);
    }

    @Override
    public void update() {
        if (!destruido) {
            sprite.setPosition(sprite.getX() + speed.x, sprite.getY() + speed.y);
            if (sprite.getX() < 0 || sprite.getX() + sprite.getWidth() > Gdx.graphics.getWidth() ||
                sprite.getY() < 0 || sprite.getY() + sprite.getHeight() > Gdx.graphics.getHeight()) {
                setDestruir();
            }
        }
    }

    @Override
    protected void reboteX() {}

    @Override
    protected void reboteY() {}

}


    