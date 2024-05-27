package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Audiovisuales;

public class PantallaJuego implements Screen {
    private SpaceNavigation game;
    private SpriteBatch batch;
    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private Nave4 nave;
    private Audiovisuales audioVisuales;
    private GestorMeteoritos gestorMeteoros;

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;

        audioVisuales = new Audiovisuales(game);
        batch = audioVisuales.crearPantalla();
        audioVisuales.generarAudioJuego();
        audioVisuales.crearFondo();
        
        gestorMeteoros = new GestorMeteoritos(batch,velXAsteroides,velYAsteroides, audioVisuales, cantAsteroides);
        gestorMeteoros.crearAsteroides();
        
        nave = audioVisuales.dibujarNave(vidas);
    }

    public void avanzarAlSiguienteNivel() {
        if (!gestorMeteoros.hayMeteoritos()) {
            PantallaJuego siguienteNivel = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 3);
            siguienteNivel.resize(1200, 800);
            game.setScreen(siguienteNivel);
            dispose();
        }
    }

    public void mostrarGameover() {
        if (nave.estaDestruido()) {
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            audioVisuales.mostrarPantallaGameOver(nave);
            dispose();
        }
    }

    public void dibujarNave() {
        nave.draw(batch, gestorMeteoros);
    }

    public void prepararEscenario() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        batch.begin();
        audioVisuales.dibujarFondo();
        dibujarNave();
        audioVisuales.dibujaEncabezado(nave, score, ronda);
        gestorMeteoros.dibujarBalas();
        gestorMeteoros.dibujarAsteroide();
        gestorMeteoros.colisionConNave(nave); // Colisiones meteoritos con la nave
        batch.end();
    }
   
    public void render(float delta) {
        prepararEscenario();
        if (!nave.estaHerido()) {
            int puntosGanados = gestorMeteoros.colisionBalaAsteroide(nave); //destrucción
            score += puntosGanados;
            gestorMeteoros.actualizarMovimientoAsteroides(); //dentro del área
            gestorMeteoros.rebotesAsteroides(); //calcular rebotes entre todos los asteroides
        }
        // Mostrar pantalla gameover
        mostrarGameover();
        // Nivel completado
        avanzarAlSiguienteNivel();
    }

    @Override
    public void show() {
        // gameMusic.play();
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