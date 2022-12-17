package com.thepross.pantallas;

import static com.badlogic.gdx.Gdx.app;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.thepross.Constants;
import com.thepross.actores.Enemigo;
import com.thepross.actores.Laser;
import com.thepross.inputs.EntradaPrincipal;
import com.thepross.MyGdxGame;
import com.thepross.actores.Jugador;
import com.thepross.inputs.EntradaWelcome;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase define objetos que contiene el menu, las opciones, logo y background del juego.
 * @author: Boris F. Calderon Sanchez
 */
public class ScreenPrincipal extends ScreenBase {

    private MyGdxGame game;
    private Stage stage;

    Texture logo;
    TextureRegion background;
    int backgroundOffset;
    BitmapFont font;
    SpriteBatch batch;

    private TextButton buttonJugar, buttonSalir;
    private Skin skin;
    private ShapeRenderer shapeRenderer;

    EntradaWelcome inputs;

    public ScreenPrincipal(MyGdxGame game) {
        super(game);
        this.game = game;
        stage = new Stage();
        batch = new SpriteBatch();
        logo = game.getAssetManager().get("logo.png", Texture.class);
        background = new TextureRegion(game.getAssetManager().get("space-background.png", Texture.class), 256, 256, 128, 256);
        backgroundOffset = 0;
        font = new BitmapFont();
        inputs = new EntradaWelcome(game);
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(game.getAssetManager().get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", game.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));
        initButtons();
        stage.addActor(buttonJugar);
        stage.addActor(buttonSalir);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.07f, 0.07f, 0.11f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawBackground();
        stage.act();
        stage.draw();

    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    public void drawBackground() {
        batch.begin();
        backgroundOffset+=2;
        if (backgroundOffset % Constants.BACKGROUND_HEIGHT == 0) {
            backgroundOffset = 0;
        }
        batch.draw(background, 0, -backgroundOffset, Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT);
        batch.draw(background, 0, -backgroundOffset + Constants.BACKGROUND_HEIGHT, Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT);
        batch.draw(logo, 0, Constants.BACKGROUND_HEIGHT - logo.getHeight(), Constants.BACKGROUND_WIDTH, logo.getHeight());
        batch.end();
    }

    public void initButtons() {
        buttonJugar = new TextButton("Jugar", skin, "default");
        buttonJugar.setSize(280, 60);
        buttonJugar.setPosition((Constants.BACKGROUND_WIDTH / 2.0f) - (buttonJugar.getWidth() / 2), Constants.BACKGROUND_HEIGHT / 2.0f);
        buttonJugar.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ScreenJuego(game));
            }
        });

        buttonSalir = new TextButton("Salir", skin, "default");
        buttonSalir.setSize(280, 60);
        buttonSalir.setPosition((Constants.BACKGROUND_WIDTH / 2.0f) - (buttonJugar.getWidth() / 2), Constants.BACKGROUND_HEIGHT / 2.5f);
        buttonSalir.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.exit();
            }
        });
    }
}
