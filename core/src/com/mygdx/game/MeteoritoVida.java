package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.math.Vector2;

public class MeteoritoVida extends ObjetoEspacial{

    private int hits;
    private Vector2 velocidad;

    public MeteoritoVida(float x, float y, int size, int xSpeed, int ySpeed, Texture tx) {        
        super(x,y,size,tx,0,1);
        //Creamos un vector con los componentes X e Y de la velocidad
        this.velocidad = new Vector2(xSpeed, ySpeed);
    }

    public void update() {
        if (!getEstaDestruido()) {
            // Mueve el sprite a una nueva posición tomando los componentes X e Y de la velocidad.
            sprite.translate(velocidad.x, velocidad.y);
            // Verificamos si el meteorito se salió de la pantalla horizontalmente
            if (sprite.getX() < 0 || sprite.getX() > Gdx.graphics.getWidth() - sprite.getWidth()) {
                reboteX();
            }
            // Verificamos si el meteorito se salió de la pantalla verticalmente
            if (sprite.getY() < 0 || sprite.getY() > Gdx.graphics.getHeight() - sprite.getHeight()) {
                reboteY();
            }
            
            
        }
    }

    @Override
    protected void reboteX() {
        // Cuando rebota en X
        velocidad.x = -velocidad.x;
    }

    @Override
    protected void reboteY() {
        // Cuando rebota en Y
        velocidad.y = -velocidad.y;
    }
}