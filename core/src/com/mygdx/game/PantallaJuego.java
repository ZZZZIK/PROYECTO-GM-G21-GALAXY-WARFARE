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
        private ArrayList<Bala> balas = new ArrayList<>();
         // Combinar asteroides normales y de 3 hits en una sola lista
        private ArrayList<ObjetoEspacial> todosAsteroides = new ArrayList<>();
       
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

            // importamos nuevo fondo
            backgroundTexture = new Texture("Background.png");
            
            
            // Inicializar assets; música de fondo y efectos de sonido
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("RushjetDanooct.mp3"));
            explosionSound = Gdx.audio.newSound(Gdx.files.internal("destruirMeterorito.mp3"));
            explosionSound.setVolume(1, 0.1f);
            
            
            gameMusic.setLooping(true);
            gameMusic.setVolume(0.1f);
            gameMusic.play();

            // Cargar imagen de la nave, 64x64   
            nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30, new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("herido.mp3")), 
                new Texture(Gdx.files.internal("bala.png")),
                Gdx.audio.newSound(Gdx.files.internal("tirarBala.mp3")));
            nave.setVidas(vidas);

            // Crear asteroides
            Random r = new Random();
            for (int i = 0; i < cantAsteroides; i++) {
                MeteoritoComun ball = new MeteoritoComun(r.nextInt(Gdx.graphics.getWidth()),
                            50 + r.nextInt(Gdx.graphics.getHeight() - 50),
                            800+ r.nextInt(30), velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4), 
                            new Texture(Gdx.files.internal("meteoro.png")));
                    todosAsteroides.add(ball);
                
                if (i % 5 == 4) {
                    Meteorito3hits strongBall = new Meteorito3hits(r.nextInt(Gdx.graphics.getWidth()),
                            50 + r.nextInt(Gdx.graphics.getHeight() - 50),
                            35+ r.nextInt(30), velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4), 
                            new Texture(Gdx.files.internal("meteorito3tiros.png")));
                    todosAsteroides.add(strongBall);
                }else if(i % 10 == 0){
                    MeteoritoVida meteoroVida = new MeteoritoVida(r.nextInt(Gdx.graphics.getWidth()),
                            50 + r.nextInt(Gdx.graphics.getHeight() - 50),
                            30+ r.nextInt(30), velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4), 
                            new Texture(Gdx.files.internal("meteoroSalud.png")));
                    todosAsteroides.add(meteoroVida);
                }
            }    
            
            
            
        }
        public void dibujaEncabezado() {
            float screenHeight = 640;
            float yOffset = -140;

            CharSequence str = "Vidas: " + nave.getVidas() + "   Nivel: " + ronda;
            game.getFont().getData().setScale(2.5f);
            game.getFont().draw(batch, str, 120, screenHeight - yOffset);
            game.getFont().draw(batch, "Score: " + this.score, 850, screenHeight - yOffset);
            game.getFont().draw(batch, "HighScore: " + game.getHighScore(), (800 / 2) + 100 , screenHeight - yOffset);
        }
        
        public void render(float delta) {
            ScreenUtils.clear(0, 0, 0.2f, 1);
            batch.begin();
            //dibujaEncabezado();
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dibujaEncabezado();
            batch.end();

            batch.begin();
            
            nave.draw(batch,this);

            if (!nave.estaHerido()) {

                // Colisiones entre balas y asteroides y su destrucción
                for (int i = 0; i < balas.size(); i++) {
                    Bala b = balas.get(i);
                    b.update();
                    for (int j = 0; j < todosAsteroides.size(); j++) {
                        ObjetoEspacial meteoro = todosAsteroides.get(j);
                        if (b.verificarColision(meteoro.getSprite())) {              
                            // Si el objeto es un asteroide
                            if (meteoro instanceof MeteoritoComun || meteoro instanceof Meteorito3hits || meteoro instanceof MeteoritoVida) {
                                if (meteoro instanceof MeteoritoComun) {
                                    score += 10;
                                } else if (meteoro instanceof Meteorito3hits) {
                                    score += 20;
                                } else if(meteoro instanceof MeteoritoVida){
                                    score += 10;
                                    nave.ganarVida();
                                }
                                
                                meteoro.setDestruir();

                                if (meteoro.getEstaDestruido()) {
                                    explosionSound.play();
                                    todosAsteroides.remove(j);
                                    j--; // Ajustar el índice después de eliminar un asteroide
                                }
                            }
                            b.setDestruir();
                            if (b.getEstaDestruido()) {
                                balas.remove(b);
                                i--; // Ajustar el índice después de eliminar una bala
                            }
                            break;
                        }  
                    }       
                }    
                // Actualizar movimiento de asteroides dentro del área
                for(ObjetoEspacial meteoro: todosAsteroides){
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

            // Dibujar balas
            for (Bala b : balas) {
                b.dibujo(batch);
            }
            
            // Dibujar asteroides y manejar colisión con nave
  
            for (int i = 0; i < todosAsteroides.size(); i++) {
                ObjetoEspacial  mc3 = todosAsteroides.get(i);
                mc3.dibujo(batch);
                if (nave.checkCollision(mc3)) {
                    mc3.setDestruir();
                    
                    
                    if(mc3.getEstaDestruido()){
                        todosAsteroides.remove(mc3);
                        i--; // Decrementar el índice para evitar saltarse elementos después de eliminarlos
                    }
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
            
            batch.end();  
            // Nivel completado
            if(todosAsteroides.size()==0){
                Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                                              velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 3);
                ss.resize(1200, 800);
                game.setScreen(ss);
                dispose();
            }
                   
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
            explosionSound.dispose();
            gameMusic.dispose();
        }
}
    