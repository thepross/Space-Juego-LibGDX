package com.thepross.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thepross.MyGdxGame;
import com.thepross.actores.Jugador;
import com.thepross.pantallas.ScreenJuego;
import com.thepross.pantallas.ScreenPrincipal;

/**
 * Esta clase define objetos para regenerar inputs en la Screen Principal del juego, para poder manejar todas las interacciones de este.
 * @author: Boris F. Calderon Sanchez
 */
public class EntradaPrincipal extends InputAdapter {

    MyGdxGame game;
    Jugador jugador;
    ScreenJuego screen;

    public EntradaPrincipal(MyGdxGame game, Jugador jugador, ScreenJuego screen) {
        this.game = game;
        this.jugador = jugador;
        this.screen = screen;
    }
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode)
        {
            case Input.Keys.UP:
                jugador.moverArriba(true);
                break;
            case Input.Keys.DOWN:
                jugador.moverAbajo(true);
                break;
            case Input.Keys.LEFT:
                if (jugador.direccion == 0) {
                    flipAnimacion(jugador.walkAnimation);
                }
                jugador.moverIzquierda(true);
                jugador.direccion = 1;
                break;
            case Input.Keys.RIGHT:
                if (jugador.direccion == 1) {
                    flipAnimacion(jugador.walkAnimation);
                }
                jugador.moverDerecha(true);
                jugador.direccion = 0;
                break;
            case Input.Keys.SPACE:
                game.getAssetManager().get("audio/laser.wav", Sound.class).play();
                jugador.shoot = true;
                jugador.playerLaser.get(jugador.currentLaser - 1).loop = true;
                jugador.currentLaser = jugador.currentLaser - 1;
                break;
            case Input.Keys.ENTER:
                if (jugador.ganador()) {
                    screen.initNivel(screen.getNivel() + 1);
                } else {
                    screen.initNivel(screen.getNivel());
                }
                break;
            case Input.Keys.ESCAPE:
                game.setScreen(new ScreenPrincipal(game));
                break;
        }
        return true;
    }

    public void flipAnimacion(Animation<TextureRegion> animation) {
        for (TextureRegion textureRegion : animation.getKeyFrames())
            textureRegion.flip(true,false);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode)
        {
            case Input.Keys.UP:
                jugador.moverArriba(false);
                break;
            case Input.Keys.DOWN:
                jugador.moverAbajo(false);
                break;
            case Input.Keys.LEFT:
                jugador.moverIzquierda(false);
                break;
            case Input.Keys.RIGHT:
                jugador.moverDerecha(false);
                break;
        }
        return true;
    }

}
