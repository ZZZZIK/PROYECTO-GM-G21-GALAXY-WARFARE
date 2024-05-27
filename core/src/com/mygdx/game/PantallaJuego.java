package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Audiovisuales;

public class PantallaJuego implements Screen {
    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private Texture backgroundTexture;
    private Nave4 nave;
    
    private Audiovisuales audioVisuales;
    
    private ArrayList<Bala> balas = new ArrayList<>();
    private ArrayList<ObjetoEspacial> todosAsteroides = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;

        audioVisuales = new Audiovisuales(game);
        
        // Crear Pantalla y retornar cámara
        batch = audioVisuales.crearPantalla();
        camera = audioVisuales.getCamera();

        // Cargar imagen de la nave, 64x64   
        nave = audioVisuales.dibujarNave(vidas);

        // Inicializar assets; música de fondo y efectos de sonido
        audioVisuales.generarAudioJuego();
        
        audioVisuales.crearFondo();
        //backgroundTexture = new Texture("Background.png");

        // Crear asteroides
        for (int i = 0; i < cantAsteroides; i++) {
            MeteoritoComun ball = audioVisuales.dibujarMeteoritoComun(velXAsteroides, velYAsteroides);
            todosAsteroides.add(ball);
            if (i % 5 == 4) {
                Meteorito3hits meteoro3hits = audioVisuales.dibujarMeteorito3hits(velXAsteroides, velYAsteroides);
                todosAsteroides.add(meteoro3hits);
            } else if (i % 10 == 0) {
                MeteoritoVida meteoroVida = audioVisuales.dibujarMeteoritoVida(velXAsteroides, velYAsteroides);
                todosAsteroides.add(meteoroVida);
            }
        }
    }
    
    private void avanzarAlSiguienteNivel() {
        if (todosAsteroides.isEmpty()) {
            
            PantallaJuego siguienteNivel = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 3);
            siguienteNivel.resize(1200, 800);
            game.setScreen(siguienteNivel);
            dispose();
        }
    }
    
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        batch.begin();
        audioVisuales.dibujarFondo();
        nave.draw(batch, this);
        
        // Dibujar el encabezado
        audioVisuales.dibujaEncabezado( nave,  score,  ronda);
        batch.end();

        if (!nave.estaHerido()) {
            // Colisiones entre balas y asteroides + destrucción
            ArrayList<Bala> balasAEliminar = new ArrayList<>();
            ArrayList<ObjetoEspacial> asteroidesAEliminar = new ArrayList<>();

            for (Bala b : balas) {
                b.update();
                for (ObjetoEspacial meteoro : todosAsteroides) {
                    if (b.verificarColision(meteoro.getSprite())) {
                        if (meteoro instanceof MeteoritoComun || meteoro instanceof Meteorito3hits || meteoro instanceof MeteoritoVida) {
                            if (meteoro instanceof MeteoritoComun) {
                                score += 10;
                            } else if (meteoro instanceof Meteorito3hits) {
                                score += 20;
                            } else if (meteoro instanceof MeteoritoVida) {
                                score += 10;
                                nave.ganarVida();
                            }
                            meteoro.setDestruir();
                            if (meteoro.getEstaDestruido()) {
                                audioVisuales.reproducirSonidoExplosion();
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

            // Actualizar movimiento de asteroides dentro del área
            for (ObjetoEspacial meteoro : todosAsteroides) {
                meteoro.update();
                
            }

            // Calcular los rebotes entre todos los asteroides
            for (int i = 0; i < todosAsteroides.size(); i++) {
                ObjetoEspacial obj1 = todosAsteroides.get(i);
                for (int j = i + 1; j < todosAsteroides.size(); j++) {
                    ObjetoEspacial obj2 = todosAsteroides.get(j);
                    obj1.rebote(obj2);
                }
            }
        }

        batch.begin();
        // Dibujar balas
        for (Bala b : balas) {
            audioVisuales.dibujarBalas(b);
        }

        // Dibujar asteroides y manejar colisión con nave
        ArrayList<ObjetoEspacial> asteroidesParaEliminar = new ArrayList<>();
        for (ObjetoEspacial mc3 : todosAsteroides) {
            mc3.dibujo(batch);
            if (nave.checkCollision(mc3)) {
                mc3.setDestruir();
                if (mc3.getEstaDestruido()) {
                    asteroidesParaEliminar.add(mc3);
                }
            }
        }
        todosAsteroides.removeAll(asteroidesParaEliminar);

        batch.end();

        // mostrar pantalla gameover
        if (nave.estaDestruido()) {
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            audioVisuales.mostrarPantallaGameOver(nave);
            dispose();
        }

        // Nivel completado
        avanzarAlSiguienteNivel();    
    }

    public boolean agregarBala(Bala bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        //gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        
        audioVisuales.pararMusica();
    }
}
