package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 {	
    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    private int moveAmount=4;
    private float friccion=0.9f;
    
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	spr.setBounds(x, y, 45, 45);
    }
    
    private void moverNaveIzq(){
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVel = -moveAmount;
        }
    }
    
    private void moverNaveDerecha(){
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVel = moveAmount;
        }
    }
    
    private void moverNaveAbajo(){
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            yVel = -moveAmount;
        }
    }
    
    private void moverNaveArriba(){
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            yVel = moveAmount;
        }
    }
    
    private void añadirFriccion(){
        xVel *= friccion;
        yVel *= friccion;
    }
    
    private void frenadoVelMin(){
        if (Math.abs(xVel) < 0.1) xVel = 0;
        if (Math.abs(yVel) < 0.1) yVel = 0;
    }
    
    private void mantenerNavePantalla(float x,float y ){
        if (x+xVel < 0 || x+xVel+spr.getWidth() > Gdx.graphics.getWidth())
            xVel*=0;
        if (y+yVel < 0 || y+yVel+spr.getHeight() > Gdx.graphics.getHeight())
            yVel*=0;
    }
    private void actualizarPosNave(SpriteBatch batch){
        float x =  spr.getX();
        float y =  spr.getY();
        
        spr.setPosition(x+xVel, y+yVel);   
        spr.draw(batch);
    }
    
    
    private void naveRecibeDaño(SpriteBatch batch){
        float x =  spr.getX();
        float y =  spr.getY();
        
        spr.setX(spr.getX()+MathUtils.random(-2,2));
        spr.draw(batch); 
            
        spr.setX(x);
        tiempoHerido--;
        if (tiempoHerido<=0){
            herido = false;
        }
    
    }
    
    private void disparar(GestorMeteoritos gestorMeteoros){
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE ) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) ) {         
            Bala  bala = new Bala(spr.getX()+spr.getWidth()/2-5,spr.getY()+ spr.getHeight()-5,0,10,txBala);
	    gestorMeteoros.agregarBala(bala);
	    soundBala.play();
        }
    }

    public void draw(SpriteBatch batch, GestorMeteoritos gestorMeteoros){
        float x =  spr.getX();
        float y =  spr.getY();
        
        if (!herido){
            moverNaveIzq();
            moverNaveDerecha();
            moverNaveArriba();
            moverNaveAbajo();
     
            //Añadimos fricción al desplazamiento de la nave
            añadirFriccion();
            
            //La nave se va frenando cuando la velocidad disminuye
            frenadoVelMin();

            //funcion para que se mantenga dentro de los bordes de la ventana
            mantenerNavePantalla(x,y);
            
            actualizarPosNave(batch);
        } else {
            naveRecibeDaño(batch);
 	}
        
        //Llamamos a la función disparar de la nave
        disparar(gestorMeteoros);       
    }

    public boolean checkCollision(Destructible obj) {
        if (!herido && obj.verificarColision(spr)) {
            // La nave reacciona a la colisión
            xVel = -xVel;
            yVel = -yVel;

            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();

            if (vidas <= 0) destruida = true;

            obj.setDestruir();
            return true;
        }
        return false;
    }
        
    public boolean estaDestruido() {
        return !herido && destruida;
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
    public void setVidas(int vidas2) {
        vidas = vidas2;
    }
}
