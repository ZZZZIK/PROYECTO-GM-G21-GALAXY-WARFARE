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
	
	 
        private Nave4 nave;
        private ArrayList<MeteoritoComun > asteroides = new ArrayList<>();
        private ArrayList<Bala> balas = new ArrayList<>();


        
        
        

	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
            this.game = game;
            this.ronda = ronda;
            this.score = score;
            this.velXAsteroides = velXAsteroides;
            this.velYAsteroides = velYAsteroides;
            this.cantAsteroides = cantAsteroides;

            batch = game.getBatch();
            camera = new OrthographicCamera();	
            camera.setToOrtho(false, 800, 640);

            // Inicializar assets; música de fondo y efectos de sonido
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
            gameMusic.setLooping(true);
            gameMusic.setVolume(0.0f);
            gameMusic.play();

            // Cargar imagen de la nave, 64x64   
            nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30, new Texture(Gdx.files.internal("MainShip3.png")),
                             Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                             new Texture(Gdx.files.internal("Rocket2.png")), 
                             Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
            nave.setVidas(vidas);

            // Crear asteroides
            Random r = new Random();
            for (int i = 0; i < cantAsteroides; i++) {
                MeteoritoComun meteorito = new MeteoritoComun(
                    r.nextInt((int)Gdx.graphics.getWidth()),
                    50 + r.nextInt((int)Gdx.graphics.getHeight() - 50),
                    40 + r.nextInt(20),
                    velXAsteroides + r.nextInt(4),
                    velYAsteroides + r.nextInt(4), 
                    new Texture(Gdx.files.internal("aGreyMedium4.png"))
                );
                asteroides.add(meteorito);
            }
        }
        public void dibujaEncabezado() {
            CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
            game.getFont().getData().setScale(2f);		
            game.getFont().draw(batch, str, 10, 30);
            game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);
            game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
        }

        
        public void render(float delta) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            dibujaEncabezado();
            
            nave.draw(batch,this);

            if (!nave.estaHerido()) {
                // Colisiones entre balas y asteroides y su destrucción
                for (int i = 0; i < balas.size(); i++) {
                    Bala b = balas.get(i);
                    b.update();
                    for (int j = 0; j < asteroides.size(); j++) {
                        MeteoritoComun mc = asteroides.get(j);
                        if (b.verificarColision(mc.getSprite())) {
                            score += 10;
                            balas.remove(b);
                            asteroides.remove(mc);
                            break; // Rompe el bucle interno, ya que la bala colisionó con un solo asteroide
                        }
                    }
                }

                // Actualizar movimiento de asteroides dentro del área
                for (MeteoritoComun mc : asteroides) {
                    mc.update();
                }

                // Colisiones entre asteroides y sus rebotes
               
                for (int i = 0; i < asteroides.size(); i++) {
                    ObjetoEspacial obj1 = asteroides.get(i);
                    for (int j = i + 1; j < asteroides.size(); j++) {
                        ObjetoEspacial obj2 = asteroides.get(j);
                        obj1.rebote(obj2);
                    }
                }
               
                
                
                
                
            }

            // Dibujar balas
            for (Bala b : balas) {
                b.dibujo(batch);
            }

            // Dibujar asteroides y manejar colisión con nave
            for (int i = 0; i < asteroides.size(); i++) {
                MeteoritoComun mc = asteroides.get(i);
                mc.dibujo(batch);
                if (nave.checkCollision(mc)) {
   
                    asteroides.remove(mc);
                    i--; // Decrementar el índice para evitar saltarse elementos después de eliminarlos
                }
            }

            if (nave.estaDestruido()) {
                if (score > game.getHighScore()) {
                    game.setHighScore(score);
                }
                Screen ss = new PantallaGameOver(game);
                ss.resize(1200, 800);
                game.setScreen(ss);
                dispose();
            }
            
            // Nivel completado
            if (asteroides.isEmpty()) {
                Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                                              velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 5);
                ss.resize(1200, 720);
                game.setScreen(ss);
                dispose();
            }

            batch.end();
        }

 
        public boolean agregarBala(Bala bb) {
            return balas.add(bb);
        }

        @Override
        public void show() {
            gameMusic.play();
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
            gameMusic.dispose();
        }
}
    
	