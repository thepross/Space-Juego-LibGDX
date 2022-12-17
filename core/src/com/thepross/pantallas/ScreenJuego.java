package com.thepross.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thepross.Constants;
import com.thepross.MyGdxGame;
import com.thepross.actores.Enemigo;
import com.thepross.actores.Jugador;
import com.thepross.inputs.EntradaPrincipal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase define objetos que contienen el juego principal, iniciando desde NAVES, HUD y AUDIO.
 * @author: Boris F. Calderon Sanchez
 */
public class ScreenJuego extends ScreenBase {
    private List<Texture> texturasJugador;
    private Stage stage;
    private Jugador jugador;
    private List<Enemigo> listEnemigos;
    private EntradaPrincipal entradaPrincipal;

    // HUD y BACKGROUND
    TextureRegion background, enemigo, laser;
    Texture heartLive, heartDead;
    int backgroundOffset;
    BitmapFont font;
    SpriteBatch batch;
    int countEnemigos;
    int countEnemigosFinal;
    int countVidas;
    int nivel;
    private MyGdxGame game;

    // AUDIO Y MUSICA
    Music backgroundMusic;

    public ScreenJuego(MyGdxGame game) {
        super(game);
        this.game = game;
        stage = new Stage();
        // HUD y BACKGROUND
        font = game.font24;
        heartDead = game.getAssetManager().get("ui/live-0.png", Texture.class);
        heartLive = game.getAssetManager().get("ui/live-1.png", Texture.class);
        batch = new SpriteBatch();
        background = new TextureRegion(game.getAssetManager().get("space-background.png", Texture.class), 256, 256, 128, 256);
        backgroundOffset = 0;

        // JUGADOR
        texturasJugador = new LinkedList<>();
        texturasJugador.add(game.getAssetManager().get("space-ships.png", Texture.class));
        texturasJugador.add(game.getAssetManager().get("space-ships.png", Texture.class));
        texturasJugador.add(game.getAssetManager().get("space-shooter.png", Texture.class));

        // ENEMIGOS
        listEnemigos = new ArrayList<Enemigo>();
        enemigo = new TextureRegion(game.getAssetManager().get("space-ships.png", Texture.class), 32, 40, 8, 8);
        laser = new TextureRegion(game.getAssetManager().get("space-shooter.png", Texture.class), 8, 0, 4, 6);
        int resetx = 1;
        int resety = 100;
        for (int i = 1; i <= Constants.DEFAULT_COUNT_ENEMIGOS_NIVEL_1; i++) {
            listEnemigos.add(new Enemigo(enemigo, 50 * resetx, Constants.BACKGROUND_HEIGHT - resety, 32, 32));
            if (i % 10 == 0) {
                resetx = 1;
                resety+=32;
            } else {
                resetx++;
            }
        }

        jugador = new Jugador(this.game, texturasJugador, (Constants.BACKGROUND_WIDTH / 2.0f) - 24, 48, 48, 48, listEnemigos, laser, Constants.DEFAULT_COUNT_VIDAS);
        countEnemigos = Constants.DEFAULT_COUNT_ENEMIGOS_NIVEL_1;
        countEnemigosFinal = countEnemigos;
        countVidas = Constants.DEFAULT_COUNT_VIDAS;
        nivel = 1;
        entradaPrincipal = new EntradaPrincipal(game, jugador, this);

        backgroundMusic = game.getAssetManager().get("audio/bgm.mp3", Music.class);
        backgroundMusic.setVolume(0.6f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {
        stage.addActor(jugador);
        for (Enemigo e : listEnemigos) {
            stage.addActor(e);
        }
        Gdx.input.setInputProcessor(entradaPrincipal);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.07f, 0.07f, 0.11f, 0.0f);
        drawHUD();

        if (!jugador.gameOver()) {
            if (jugador.ganador()) {
                drawGanador();
                stage.draw();
            } else {
                stage.act();
                stage.draw();
            }
        } else {
            drawGameOver();
            stage.draw();
        }
    }

    @Override
    public void hide() {
        jugador.remove();
        for (Enemigo e : listEnemigos) {
            e.remove();
        }
        stage.dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void drawHUD() {
        batch.begin();
        backgroundOffset+=2;
        if (backgroundOffset % Constants.BACKGROUND_HEIGHT == 0) {
            backgroundOffset = 0;
        }
        batch.draw(background, 0, -backgroundOffset, Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT);
        batch.draw(background, 0, -backgroundOffset + Constants.BACKGROUND_HEIGHT, Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT);
        font.draw(batch, jugador.cantidadEnemigosVivos() + "/" + countEnemigosFinal, Constants.BACKGROUND_WIDTH * 0.87f, Constants.BACKGROUND_HEIGHT * 0.98f);
        font.draw(batch, "Nivel: " + nivel, Constants.BACKGROUND_WIDTH * 0.87f, 40.0f);
        int i = 1;
        for (i = 1; i <= jugador.vida; i++) {
            batch.draw(heartLive, Constants.BACKGROUND_WIDTH * 0.04f * i, Constants.BACKGROUND_HEIGHT * 0.95f, heartLive.getWidth() + 10, heartLive.getHeight() + 10);
        }
        if (i < countVidas + 1) {
            for (int j = i; j <= countVidas; j++) {
                batch.draw(heartDead, Constants.BACKGROUND_WIDTH * 0.04f * j, Constants.BACKGROUND_HEIGHT * 0.95f, heartDead.getWidth() + 10, heartDead.getHeight() + 10);
            }
        }
        batch.end();
    }

    public void drawGameOver() {
        batch.begin();
        font.getData().setScale(2.0f, 2.0f);
        font.draw(batch, "GAME OVER", Constants.BACKGROUND_WIDTH * 0.25f, Constants.BACKGROUND_HEIGHT * 0.52f);
        font.getData().setScale(1.0f, 1.0f);
        font.draw(batch, "Presiona ENTER para reiniciar.", Constants.BACKGROUND_WIDTH * 0.25f, Constants.BACKGROUND_HEIGHT * 0.45f);
        batch.end();
    }

    public void drawGanador() {
        batch.begin();
        font.getData().setScale(2.0f, 2.0f);
        font.draw(batch, "GANASTE!!", Constants.BACKGROUND_WIDTH * 0.3f, Constants.BACKGROUND_HEIGHT * 0.52f);
        font.getData().setScale(1.0f, 1.0f);
        font.draw(batch, "Presiona ENTER para el siguiente nivel.", Constants.BACKGROUND_WIDTH * 0.18f, Constants.BACKGROUND_HEIGHT * 0.45f);
        batch.end();
    }

    public int getNivel() {
        return this.nivel;
    }

    public void initNivel(int nivel){
        hide();
        stage = new Stage();
        font = game.font24;
        batch = new SpriteBatch();
        background = new TextureRegion(game.getAssetManager().get("space-background.png", Texture.class), 256, 256, 128, 256);
        backgroundOffset = 0;

        listEnemigos = new ArrayList<Enemigo>();
        int resetx = 1;
        int resety = 100;
        // NIVELES
        switch (nivel) {
            case 1:
                countEnemigos = Constants.DEFAULT_COUNT_ENEMIGOS_NIVEL_1;
                break;
            case 2:
                countEnemigos = Constants.DEFAULT_COUNT_ENEMIGOS_NIVEL_2;
                break;
            case 3:
                countEnemigos = Constants.DEFAULT_COUNT_ENEMIGOS_NIVEL_3;
                break;
            case 4:
                countEnemigos = Constants.DEFAULT_COUNT_ENEMIGOS_NIVEL_4;
                break;
            case 5:
                countEnemigos = Constants.DEFAULT_COUNT_ENEMIGOS_NIVEL_5;
                break;
        }
        for (int i = 1; i <= countEnemigos; i++) {
            listEnemigos.add(new Enemigo(enemigo, 50 * resetx, Constants.BACKGROUND_HEIGHT - resety, 32, 32));
            if (i % 10 == 0) {
                resetx = 1;
                resety+=32;
            } else {
                resetx++;
            }
        }
        if (this.nivel == nivel) {
            jugador = new Jugador(this.game, texturasJugador, (Constants.BACKGROUND_WIDTH / 2.0f) - 24, 48, 48, 48, listEnemigos, laser, Constants.DEFAULT_COUNT_VIDAS);
        } else {
            jugador = new Jugador(this.game, texturasJugador, (Constants.BACKGROUND_WIDTH / 2.0f) - 24, 48, 48, 48, listEnemigos, laser, jugador.vida);
        }
        countVidas = Constants.DEFAULT_COUNT_VIDAS;
        countEnemigosFinal = countEnemigos;
        this.nivel = nivel;
        entradaPrincipal = new EntradaPrincipal(game, jugador, this);
        show();
    }

}
