package com.thepross.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thepross.Constants;
import com.thepross.inputs.EntradaWelcome;
import com.thepross.MyGdxGame;

/**
 * Esta clase define objetos de pantalla de carga, para poder pasar a distintas etapas de Screens.
 * @author: Boris F. Calderon Sanchez
 */
public class ScreenWelcome extends ScreenBase {
    SpriteBatch batch;
    BitmapFont font;
    EntradaWelcome inputs;
    private float tiempo;
    Texture image;

    public ScreenWelcome(MyGdxGame game) {
        super(game);
        batch = new SpriteBatch();
        font = game.font24;
        inputs = new EntradaWelcome(game);
        image = game.getAssetManager().get("background2.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputs);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.06f, 0.01f, 0.15f, 0.0f);
        tiempo += delta;

        batch.begin();
        font.draw(batch, "Bienvenido!", Constants.BACKGROUND_WIDTH * 0.4f, Constants.BACKGROUND_HEIGHT * 0.55f);
        font.draw(batch, "Cargando...", Constants.BACKGROUND_WIDTH * 0.4f, Constants.BACKGROUND_HEIGHT * 0.50f);
        font.draw(batch, "Tiempo: " + (int) tiempo + "/4", Gdx.graphics.getWidth() * 0.02f, Gdx.graphics.getHeight() * 0.05f);
        batch.end();

        if (tiempo > 4.5) {
            game.setScreen(new ScreenPrincipal(game));
        }
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
