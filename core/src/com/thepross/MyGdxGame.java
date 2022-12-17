package com.thepross;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.thepross.pantallas.ScreenWelcome;

/**
 * Esta clase define objetos que contiene el archivo principal y la creación de instancias para los recursos(assets).
 * @author: Boris F. Calderon Sanchez
 */
public class MyGdxGame extends Game {

	private AssetManager assetManager;
	public BitmapFont font24;

	public AssetManager getAssetManager() {
		return assetManager;
	}

	@Override
	public void create () {
		initFonts();

		Gdx.graphics.setWindowedMode(Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT);
		assetManager = new AssetManager();
		assetManager.load("jugador.png", Texture.class);
		assetManager.load("player-idle.png", Texture.class);
		assetManager.load("player-run.png", Texture.class);
		assetManager.load("player-run-shoot.png", Texture.class);
		assetManager.load("background2.png", Texture.class);
		assetManager.load("skeleton-idle.png", Texture.class);
		assetManager.load("skeleton-walk.png", Texture.class);
		assetManager.load("player-shoot.png", Texture.class);
		assetManager.load("player-shoot-laser.png", Texture.class);
		assetManager.load("background-grass.jpg", Texture.class);
		assetManager.load("far.png", Texture.class);
		assetManager.load("space-background.png", Texture.class);
		assetManager.load("space-shooter.png", Texture.class);
		assetManager.load("space-ships.png", Texture.class);

		assetManager.load("img/splash.png", Texture.class);
		assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
		assetManager.load("ui/live-0.png", Texture.class);
		assetManager.load("ui/live-1.png", Texture.class);
		assetManager.load("logo.png", Texture.class);
		// audio y musica
		assetManager.load("audio/bgm.mp3", Music.class);
		assetManager.load("audio/hit.wav", Sound.class);
		assetManager.load("audio/laser.wav", Sound.class);
		assetManager.finishLoading();

		setScreen(new ScreenWelcome(this));

	}

	private void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 24;
		params.color = Color.WHITE;
		font24 = generator.generateFont(params);
	}

	@Override
	public void render() {
		super.render();
	}
}
