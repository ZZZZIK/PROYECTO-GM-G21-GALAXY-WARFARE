package com.mygdx.game;
import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Destructible {
    void setDestruir();
    boolean getEstaDestruido();
    //boolean verificarColision(Sprite other);
}