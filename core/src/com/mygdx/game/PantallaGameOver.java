package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaGameOver implements Screen {
	private SpaceNavigation game;
	private OrthographicCamera camera;
        
        private Texture[] animationFrames;
        private float frameDuration = 0.4f; // Duración de cada frame en segundos
        private float elapsedTime = 0f;
        private int currentFrameIndex = 0;
        private Music backgroundMusic;

	public PantallaGameOver(SpaceNavigation game) {
		this.game = game;
        
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 800);
                
                animationFrames = new Texture[5];
                animationFrames[0] = new Texture(Gdx.files.internal("gameover1.jpg"));
                animationFrames[1] = new Texture(Gdx.files.internal("gameover2.jpg"));
                animationFrames[2] = new Texture(Gdx.files.internal("gameover3.jpg"));
                animationFrames[3] = new Texture(Gdx.files.internal("gameover4.jpg"));
                animationFrames[4] = new Texture(Gdx.files.internal("gameover5.jpg"));
                
                // Cargar la música
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("gameOver.mp3"));
                backgroundMusic.setLooping(false); // Hacer que la música se repita
                backgroundMusic.setVolume(0.3f); // Ajustar el volumen
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		// Actualiza el índice del frame actual basado en el tiempo transcurrido
                elapsedTime += delta;
                if (elapsedTime >= frameDuration) {
                    elapsedTime -= frameDuration;
                    currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
                }
	
		game.getBatch().begin();
                
                // Dibujar la animación
                game.getBatch().draw(animationFrames[currentFrameIndex], 0, 0, 1200, 800); // Ajusta las coordenadas según sea necesario
                
                game.getBatch().end();
                
		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(game,1,3,0,1,1,10);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
	}
 
	
	@Override
	public void show() {
            backgroundMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
            backgroundMusic.stop();
	}

	@Override
	public void dispose() {
            for (Texture frame : animationFrames) {
                frame.dispose(); // Liberar las texturas de los frames
            }
            backgroundMusic.dispose();
	}
        
}
