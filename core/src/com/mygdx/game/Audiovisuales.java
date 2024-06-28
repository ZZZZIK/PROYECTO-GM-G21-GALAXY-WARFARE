package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;

public class Audiovisuales {
    // para patrón de diseño Singleton
    private static Audiovisuales instanciaUnica;

    private Sound explosionSound;
    private Music gameMusic;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture backgroundTexture;
    private SpaceNavigation game;

    private float minX = Gdx.graphics.getWidth() / 4f; 
    private float maxX = Gdx.graphics.getWidth() * 3.5f / 4f; 
    private float minY = Gdx.graphics.getHeight() / 4f; 
    private float maxY = Gdx.graphics.getHeight() * 3.5f / 4.0f; 

    // Constructor PATRON SINGLETON
    private Audiovisuales(SpaceNavigation game) {
        this.game = game;
        
        
    }

    // Método para obtener la instancia única
    public static Audiovisuales obtenerInstancia(SpaceNavigation game) {
        if (instanciaUnica == null) {
            instanciaUnica = new Audiovisuales(game);
        }
        return instanciaUnica;
    }
    
    public void generarAudioJuego() {
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("destruirMeterorito.mp3"));
        explosionSound.setVolume(1, 0.1f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("RushjetDanooct.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.1f);
        gameMusic.play();
        
    }

    public void reproducirSonidoExplosion() {
        explosionSound.play();
    }

    public void desaparecerFondo() {
        backgroundTexture.dispose();
    }

    public SpriteBatch crearPantalla() {
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        return batch;
    }

    public void crearFondo() {
        backgroundTexture = new Texture("Background.png");
    }

    public void dibujarFondo() {
        batch.setProjectionMatrix(camera.combined);
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void dibujaEncabezado(Nave4 nave, int score, int ronda) {
        float screenHeight = 640;
        float yOffset = -140;

        CharSequence str = "Vidas: " + nave.getVidas() + "   Nivel: " + ronda;
        game.getFont().getData().setScale(2.5f);
        game.getFont().draw(batch, str, 120, screenHeight - yOffset);
        game.getFont().draw(batch, "Score: " + score, 850, screenHeight - yOffset);
        game.getFont().draw(batch, "HighScore: " + game.getHighScore(), (800 / 2) + 100, screenHeight - yOffset);
    }

    public void reproducirMusica() {
        gameMusic.play();
    }

    public void pararMusica() {
        gameMusic.dispose();
    }

    public Nave4 dibujarNave(int vidas) {
        Nave4 nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30, new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("herido.mp3")),
                new Texture(Gdx.files.internal("bala.png")),
                Gdx.audio.newSound(Gdx.files.internal("tirarBala.mp3")));
        nave.setVidas(vidas);
        return nave;
    }

    public MeteoritoComun dibujarMeteoritoComun(int velXAsteroides, int velYAsteroides) {
        Random r = new Random();
        MeteoritoComun ball = new MeteoritoComun(
                minX + r.nextFloat(maxX - minX), 
                minY + r.nextFloat(maxY - minY), 
                800 + r.nextInt(30), velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4),
                new Texture(Gdx.files.internal("meteoro.png")));
        return ball;  
    }

    public Meteorito3hits dibujarMeteorito3hits(int velXAsteroides, int velYAsteroides) {
        Random r = new Random();
        Meteorito3hits strongBall = new Meteorito3hits(
                minX + r.nextFloat(maxX - minX),
                minY + r.nextFloat(maxY - minY),
                35 + r.nextInt(30), velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4),
                new Texture(Gdx.files.internal("meteorito3tiros.png")));
        return strongBall;
    }

    public MeteoritoVida dibujarMeteoritoVida(int velXAsteroides, int velYAsteroides) {
        Random r = new Random();
        MeteoritoVida meteoroVida = new MeteoritoVida(
                minX + r.nextFloat(maxX - minX), 
                minY + r.nextFloat(maxY - minY),
                30 + r.nextInt(30), velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4),
                new Texture(Gdx.files.internal("meteoroSalud.png")));
        return meteoroVida;
    }

    public void dibujarBalas(Bala b) {
        b.dibujo(batch);
    }

    public void mostrarPantallaGameOver(Nave4 nave) {
        Screen ss = new PantallaGameOver(game);
        ss.resize(1200, 800);
        game.setScreen(ss);
       
        //gameMusic.dispose();
    
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}