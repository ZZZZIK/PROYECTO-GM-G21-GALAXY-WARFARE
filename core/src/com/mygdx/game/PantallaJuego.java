package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends PantallaBase {
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
        super(game);
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;

        //audioVisuales = new Audiovisuales(game);
        audioVisuales = Audiovisuales.obtenerInstancia(game);
        
        SpriteBatch batch = audioVisuales.crearPantalla();
        audioVisuales.generarAudioJuego();
        
        audioVisuales.crearFondo();

        MeteoritoFactory meteoritoFactory = new ImplementMeteoritoFactory();
        gestorMeteoros = new GestorMeteoritos(batch, velXAsteroides, velYAsteroides, audioVisuales, cantAsteroides, meteoritoFactory);
        
        
        
        gestorMeteoros.crearAsteroides();

        nave = audioVisuales.dibujarNave(vidas);
    }

    @Override
    protected void draw(float delta) {
        prepararEscenario();
        if (!nave.estaHerido()) {
            int puntosGanados = gestorMeteoros.colisionBalaAsteroide(nave); // Destrucción
            score += puntosGanados;
            gestorMeteoros.actualizarMovimientoAsteroides(); // Dentro del área
            gestorMeteoros.rebotesAsteroides(); // Calcular rebotes entre todos los asteroides
        }
        // Mostrar pantalla game over
        mostrarGameover();
        // Nivel completado
        avanzarAlSiguienteNivel();
    }

    private void avanzarAlSiguienteNivel() {
        if (!gestorMeteoros.hayMeteoritos()) {
            dispose();
            PantallaJuego siguienteNivel = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 3);
            siguienteNivel.resize(1200, 800);
            game.setScreen(siguienteNivel);
            
        }
    }

    private void mostrarGameover() {
        //if (nave.estaDestruido()) {
            
        if (nave.getEstaDestruido()){
            
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            dispose();
            audioVisuales.mostrarPantallaGameOver(nave);
            //dispose();
        }
    }

    private void prepararEscenario() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        SpriteBatch batch = game.getBatch();
        
        audioVisuales.dibujarFondo();
        dibujarNave();
        audioVisuales.dibujaEncabezado(nave, score, ronda);
        
        audioVisuales.mostrarTurbo(nave.isTurboActivado());
        
        gestorMeteoros.dibujarBalas();
        gestorMeteoros.dibujarAsteroide();
        gestorMeteoros.colisionConNave(nave); // Colisiones meteoritos con la nave
        
    }

    private void dibujarNave() {
        nave.draw(game.getBatch(), gestorMeteoros);
    }

    @Override
    public void show() {
        // gameMusic.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        audioVisuales.pararMusica();
    }
}