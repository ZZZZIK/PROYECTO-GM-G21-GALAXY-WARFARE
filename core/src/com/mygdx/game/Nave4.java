package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Nave4 extends Jugador {
    private Sound soundBala;
    private Texture txBala;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x, y, tx, soundChoque);
        this.soundBala = soundBala;
        this.txBala = txBala;
    }

    private void disparar(GestorMeteoritos gestorMeteoros) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Bala bala = new Bala(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 10, txBala);
            gestorMeteoros.agregarBala(bala);
            soundBala.play();
        }
    }

    @Override
    public void draw(SpriteBatch batch, GestorMeteoritos gestorMeteoros) {
        super.draw(batch, gestorMeteoros);
        // Llamamos a la función disparar de la nave
        disparar(gestorMeteoros);
    }
}
