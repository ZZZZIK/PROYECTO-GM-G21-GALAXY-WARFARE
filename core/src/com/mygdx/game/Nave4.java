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
    private float friction=0.9f;
    
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	//spr.setOriginCenter();
    	spr.setBounds(x, y, 45, 45);

    }
    
    public void draw(SpriteBatch batch, PantallaJuego juego){
        float x =  spr.getX();
        float y =  spr.getY();
        if (!herido) {
	        
                
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVel = -moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVel = moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            yVel = -moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            yVel = moveAmount;
        }

        xVel *= friction;
        yVel *= friction;

        // Detenerse cuando la velocidad es muy pequeña
        if (Math.abs(xVel) < 0.1) xVel = 0;
        if (Math.abs(yVel) < 0.1) yVel = 0;
                

	// que se mantenga dentro de los bordes de la ventana
	if (x+xVel < 0 || x+xVel+spr.getWidth() > Gdx.graphics.getWidth())
	    xVel*=0;
	if (y+yVel < 0 || y+yVel+spr.getHeight() > Gdx.graphics.getHeight())
	    yVel*=0;
	        
	spr.setPosition(x+xVel, y+yVel);   
        //spr.setPosition(xVel, yVel);   
 	spr.draw(batch);
        } else {
            spr.setX(spr.getX()+MathUtils.random(-2,2));
            spr.draw(batch); 
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido<=0){
                herido = false;
            }
 	}
        
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {         
            Bala  bala = new Bala(spr.getX()+spr.getWidth()/2-5,spr.getY()+ spr.getHeight()-5,0,10,txBala);
	    juego.agregarBala(bala);
	    soundBala.play();
        }
       
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
    
    
    
    /*
    public boolean checkCollision(Bala b) {
        if(!herido && b.verificarColision(spr)){
            // rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            
            vidas--;
            herido = true;
            tiempoHerido=tiempoHeridoMax;
            sonidoHerido.play();
            
            if (vidas<=0) destruida = true;
            
            return true;
        }
        return false;
    }
    */
    
    
    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {vidas = vidas2;}
}
