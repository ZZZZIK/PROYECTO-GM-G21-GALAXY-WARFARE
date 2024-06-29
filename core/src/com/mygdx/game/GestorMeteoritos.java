package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class GestorMeteoritos {
    private int cantAsteroides;
    private Audiovisuales audiovisuales;
    private SpriteBatch batch;
    private int velXAsteroides;
    private int velYAsteroides;
    private MeteoritoFactory meteoritoFactory;
    
    private ArrayList<Bala> balas = new ArrayList<>();
    private ArrayList<ObjetoEspacial> todosAsteroides = new ArrayList<>();
    
    public GestorMeteoritos(SpriteBatch batch ,int velXAsteroides,int velYAsteroides, Audiovisuales audiovisuales,int cantAsteroides, MeteoritoFactory meteoritoFactory){
        this.cantAsteroides = cantAsteroides;
        this.audiovisuales=audiovisuales;
        this.batch=batch;
        this.velXAsteroides=velXAsteroides;
        this.velYAsteroides=velYAsteroides;
        this.meteoritoFactory = meteoritoFactory;
    }

    public void crearAsteroides() {
        for (int i = 0; i < cantAsteroides; i++) {
            MeteoritoComun ball = audiovisuales.dibujarMeteoritoComun(velXAsteroides, velYAsteroides);
            todosAsteroides.add(ball);
            if (i % 5 == 4) {
                Meteorito3hits meteoro3hits = audiovisuales.dibujarMeteorito3hits(velXAsteroides, velYAsteroides);
                todosAsteroides.add(meteoro3hits);
            } else if (i % 10 == 0) {
                MeteoritoVida meteoroVida = audiovisuales.dibujarMeteoritoVida(velXAsteroides, velYAsteroides);
                todosAsteroides.add(meteoroVida);
            }
        }
    }
    
    public void renderAsteroides() {
        for (ObjetoEspacial m : todosAsteroides) {
            m.update();
            m.dibujo(batch);
        }
    }

    public ArrayList<ObjetoEspacial> getMeteoritos() {
        return todosAsteroides;
    }

    public void dibujarAsteroide() {
        for (ObjetoEspacial mc3 : todosAsteroides) {
            mc3.dibujo(batch);
        }
    }

    public boolean hayMeteoritos() {
        return !todosAsteroides.isEmpty();
    }

    public void colisionConNave(Nave4 nave) {
        // si la nave choca se elimina el meteoro
        ArrayList<ObjetoEspacial> asteroidesParaEliminar = new ArrayList<>();
        for (ObjetoEspacial meteoro : todosAsteroides) {
            if (nave.checkCollision(meteoro)) {
                meteoro.setDestruir();
                if (meteoro.getEstaDestruido()) {
                    asteroidesParaEliminar.add(meteoro);
                }
            }
        }
        todosAsteroides.removeAll(asteroidesParaEliminar);
    }

    public int colisionBalaAsteroide(Nave4 nv) {
        ArrayList<Bala> balasAEliminar = new ArrayList<>();
        ArrayList<ObjetoEspacial> asteroidesAEliminar = new ArrayList<>();
        int sumaScore = 0;

        for (Bala b : balas) {
            b.update();
            for (ObjetoEspacial meteoro : todosAsteroides) {
                if (b.verificarColision(meteoro.getSprite())) {
                    if (meteoro instanceof MeteoritoComun || meteoro instanceof Meteorito3hits
                            || meteoro instanceof MeteoritoVida) {
                        if (meteoro instanceof MeteoritoComun) {
                            sumaScore += 10;
                        } else if (meteoro instanceof Meteorito3hits) {
                            sumaScore += 20;
                        } else if (meteoro instanceof MeteoritoVida) {
                            sumaScore += 10;
                            nv.ganarVida();
                        }
                        meteoro.setDestruir();
                        if (meteoro.getEstaDestruido()) {
                            audiovisuales.reproducirSonidoExplosion();
                            asteroidesAEliminar.add(meteoro);
                        }
                    }
                    b.setDestruir();
                    if (b.getEstaDestruido()) {
                        balasAEliminar.add(b);
                    }
                    break;
                }
            }
        }
        balas.removeAll(balasAEliminar);
        todosAsteroides.removeAll(asteroidesAEliminar);

        return sumaScore;
    }

    public void rebotesAsteroides() {
        for (int i = 0; i < todosAsteroides.size(); i++) {
            ObjetoEspacial obj1 = todosAsteroides.get(i);
            for (int j = i + 1; j < todosAsteroides.size(); j++) {
                ObjetoEspacial obj2 = todosAsteroides.get(j);
                obj1.rebote(obj2);
            }
        }
    }

    public void actualizarMovimientoAsteroides() {
        for (ObjetoEspacial meteoro : todosAsteroides) {
            meteoro.update();
        }
    }

    public boolean agregarBala(Bala bb) {
        return balas.add(bb);
    }
    
    public void dibujarBalas(){
        for (Bala b : balas) {
            audiovisuales.dibujarBalas(b);
        }
    }
}
