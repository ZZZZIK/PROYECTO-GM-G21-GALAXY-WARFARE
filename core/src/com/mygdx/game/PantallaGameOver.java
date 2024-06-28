package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver extends PantallaBase {
    private Texture[] animationFrames;
    private float frameDuration = 0.4f;
    private float elapsedTime = 0f;
    private int currentFrameIndex = 0;
    private Music backgroundMusic;

    public PantallaGameOver(SpaceNavigation game) {
        super(game);
        animationFrames = new Texture[5];
        animationFrames[0] = new Texture(Gdx.files.internal("gameover1.jpg"));
        animationFrames[1] = new Texture(Gdx.files.internal("gameover2.jpg"));
        animationFrames[2] = new Texture(Gdx.files.internal("gameover3.jpg"));
        animationFrames[3] = new Texture(Gdx.files.internal("gameover4.jpg"));
        animationFrames[4] = new Texture(Gdx.files.internal("gameover5.jpg"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("gameOver.mp3"));
        backgroundMusic.setLooping(false);
        backgroundMusic.setVolume(0.3f);
    }

    @Override
    protected void draw(float delta) {
        elapsedTime += delta;
        if (elapsedTime >= frameDuration) {
            elapsedTime -= frameDuration;
            currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
        }
        game.getBatch().draw(animationFrames[currentFrameIndex], 0, 0, 1200, 800);

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            Screen ss = new PantallaJuego(game, 1, 3, 0, 1, 1, 10);
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
    public void hide() {
        backgroundMusic.stop();
    }

    @Override
    public void dispose() {
        for (Texture frame : animationFrames) {
            frame.dispose();
        }
        backgroundMusic.dispose();
    }
}
