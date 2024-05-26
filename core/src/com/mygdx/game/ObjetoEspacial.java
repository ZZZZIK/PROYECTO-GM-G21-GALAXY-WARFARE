package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public abstract class ObjetoEspacial implements Destructible {
    protected Sprite sprite;
    protected boolean destruido;
    
    public ObjetoEspacial(float x, float y, int size, Texture texture) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(size, size);
        this.sprite.setPosition(x, y);
        this.destruido = false;
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
        destruido = true;
    }

    @Override
    public boolean getEstaDestruido() {
        return destruido;
    }
    
    public Sprite getSprite() {
        return sprite;
    }
   
    
    // Método para en caso de colision de dos obj espaciales, concretamente meteoritos en consecuencia rebote
    public void rebote(ObjetoEspacial objE) {
        if (!destruido && !objE.getEstaDestruido() && this.verificarColision(objE.sprite)) {
            // Rebotar en X
            if (sprite.getX() < objE.sprite.getX() + objE.sprite.getWidth() && sprite.getX() + sprite.getWidth() > objE.sprite.getX()) {
                reboteX();
                objE.reboteX();
            }
            // Rebotar en Y
            if (sprite.getY() < objE.sprite.getY() + objE.sprite.getHeight() && sprite.getY() + sprite.getHeight() > objE.sprite.getY()) {
                reboteY();
                objE.reboteY();
            }
        
        
        
        }
        
    }

    // Métodos abstractos que los hijos pueden utilizar para el manejo del rebote
    protected abstract void reboteX();
    protected abstract void reboteY();
    
}
