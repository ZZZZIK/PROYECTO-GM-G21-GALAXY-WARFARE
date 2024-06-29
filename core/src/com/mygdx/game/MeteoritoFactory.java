package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public interface MeteoritoFactory {
    ObjetoEspacial crearMeteoritoComun(float x, float y, int size, int velX, int velY, Texture texture);
    ObjetoEspacial crearMeteorito3hits(float x, float y, int size, int velX, int velY, Texture texture);
    ObjetoEspacial crearMeteoritoVida(float x, float y, int size, int velX, int velY, Texture texture);
}