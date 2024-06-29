package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;

public class ImplementMeteoritoFactory implements MeteoritoFactory {
    @Override
    public ObjetoEspacial crearMeteoritoComun(float x, float y, int size, int velX, int velY, Texture texture) {
        return new MeteoritoComun(x, y, size, velX, velY, texture);
    }

    @Override
    public ObjetoEspacial crearMeteorito3hits(float x, float y, int size, int velX, int velY, Texture texture) {
        return new Meteorito3hits(x, y, size, velX, velY, texture);
    }

    @Override
    public ObjetoEspacial crearMeteoritoVida(float x, float y, int size, int velX, int velY, Texture texture) {
        return new MeteoritoVida(x, y, size, velX, velY, texture);
    }
}
