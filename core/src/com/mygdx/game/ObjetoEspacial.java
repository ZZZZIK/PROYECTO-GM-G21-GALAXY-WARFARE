package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public abstract class ObjetoEspacial implements Destructible {
    protected Sprite sprite;
    protected boolean destruido;
    protected int hits,hitsMax;

    public ObjetoEspacial(float x, float y, int size, Texture texture,int hits, int hitsMax) {
        this.sprite = new Sprite(texture);
        //this.sprite.setSize(size, size);
        this.sprite.setPosition(x, y);
        this.destruido = false;
        this.hits = hits;
        this.hitsMax=hitsMax;
   
    }
    
    public void dibujo(SpriteBatch batch){
        if(!destruido){
            sprite.draw(batch);
        }  
    }
    
    public boolean verificarColision(Sprite other) {
        return sprite.getBoundingRectangle().overlaps(other.getBoundingRectangle());
    }
    
    public abstract void update();
    
    @Override
    public void setDestruir() {
        hits++;
    }

    @Override
    public boolean getEstaDestruido() {
        return hits>=hitsMax;
               
    }
    
    public Sprite getSprite() {
        return sprite;
    }
      
    // Método para en caso de colision de dos obj espaciales, concretamente meteoritos en consecuencia rebote
    public void rebote(ObjetoEspacial objE) {
        if (!destruido && !objE.getEstaDestruido() && this.verificarColision(objE.sprite)) {
            Vector2 diff = new Vector2(sprite.getX() - objE.sprite.getX(), sprite.getY() - objE.sprite.getY());
            float overlapX = (sprite.getWidth() / 2 + objE.sprite.getWidth() / 2) - Math.abs(diff.x);
            float overlapY = (sprite.getHeight() / 2 + objE.sprite.getHeight() / 2) - Math.abs(diff.y);
            if (overlapX < overlapY) {
                if (diff.x > 0) {
                    sprite.setX(sprite.getX() + overlapX / 2);
                    objE.sprite.setX(objE.sprite.getX() - overlapX / 2);
                } else {
                    sprite.setX(sprite.getX() - overlapX / 2);
                    objE.sprite.setX(objE.sprite.getX() + overlapX / 2);
                }
                reboteX();
                objE.reboteX();
            } else {
                if (diff.y > 0) {
                    sprite.setY(sprite.getY() + overlapY / 2);
                    objE.sprite.setY(objE.sprite.getY() - overlapY / 2);
                } else {
                    sprite.setY(sprite.getY() - overlapY / 2);
                    objE.sprite.setY(objE.sprite.getY() + overlapY / 2);
                }
                reboteY();
                objE.reboteY();
            }
        }
    }
    

    // Método para en caso de colisión de dos obj espaciales, concretamente meteoritos en consecuencia rebote
    // Métodos abstractos que los hijos pueden utilizar para el manejo del rebote
    protected abstract void reboteX();
    protected abstract void reboteY();
    
}
