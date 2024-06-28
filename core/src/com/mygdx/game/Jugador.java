package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public abstract class Jugador implements Destructible {
    protected boolean destruida = false;
    protected int vidas = 3;
    protected float xVel = 0;
    protected float yVel = 0;
    protected Sprite spr;
    protected Sound sonidoHerido;
    protected boolean herido = false;
    protected int tiempoHeridoMax = 50;
    protected int tiempoHerido;
    protected int moveAmount = 4;
    protected float friccion = 0.9f;

    public Jugador(int x, int y, Texture tx, Sound soundChoque) {
        sonidoHerido = soundChoque;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
    }

    protected void moverIzq() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVel = -moveAmount;
        }
    }

    protected void moverDerecha() {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVel = moveAmount;
        }
    }

    protected void moverAbajo() {
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            yVel = -moveAmount;
        }
    }

    protected void moverArriba() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            yVel = moveAmount;
        }
    }

    protected void añadirFriccion() {
        xVel *= friccion;
        yVel *= friccion;
    }

    protected void frenadoVelMin() {
        if (Math.abs(xVel) < 0.1) xVel = 0;
        if (Math.abs(yVel) < 0.1) yVel = 0;
    }

    protected void mantenerJugadorEnPantalla(float x, float y) {
        if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth())
            xVel *= 0;
        if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight())
            yVel *= 0;
    }

    protected void actualizarPosJugador(SpriteBatch batch) {
        float x = spr.getX();
        float y = spr.getY();

        spr.setPosition(x + xVel, y + yVel);
        spr.draw(batch);
    }

    protected void RecibeDaño(SpriteBatch batch) {
        float x = spr.getX();
        float y = spr.getY();

        spr.setX(spr.getX() + MathUtils.random(-2, 2));
        spr.draw(batch);

        spr.setX(x);
        tiempoHerido--;
        if (tiempoHerido <= 0) {
            herido = false;
        }
    }

    public void draw(SpriteBatch batch, GestorMeteoritos gestorMeteoros) {
        float x = spr.getX();
        float y = spr.getY();

        if (!herido) {
            moverIzq();
            moverDerecha();
            moverArriba();
            moverAbajo();

            // Añadimos fricción al desplazamiento de la nave
            añadirFriccion();

            // La nave se va frenando cuando la velocidad disminuye
            frenadoVelMin();
 
            // Función para que se mantenga dentro de los bordes de la ventana
            mantenerJugadorEnPantalla(x, y);

            actualizarPosJugador(batch);
        } else {
            RecibeDaño(batch);
        }
    }

    public boolean checkCollision(ObjetoEspacial obj) {
        if (!herido && obj.verificarColision(spr)) {
            // La nave reacciona a la colisión
            xVel = -xVel;
            yVel = -yVel;

            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();

            if (vidas <= 0) {
                setDestruir();
            }

            obj.setDestruir();
            return true;
        }
        return false;
    }

 

    public boolean estaHerido() {
        return herido;
    }

    public void ganarVida() {
        vidas++;
    }

    public int getVidas() {
        return vidas;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public boolean getEstaDestruido() {
        return !herido && destruida;
    }
    
    public void setDestruir() {
        destruida=true;
    }
    
    public float getXVel() {
        return xVel;
    }

    public void setXVel(float xVel) {
        this.xVel = xVel;
    }

    public float getYVel() {
        return yVel;
    }

    public void setYVel(float yVel) {
        this.yVel = yVel;
    }
    
    public void setMoveAmount(int moveAmount){
        this.moveAmount=moveAmount;
    }
    
    
    
    
}

