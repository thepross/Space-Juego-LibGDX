package com.thepross.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.thepross.MyGdxGame;
import com.thepross.pantallas.ScreenPrincipal;

/**
 * Esta clase define objetos para regenerar inputs en la Screen Welcome, para saltar esta.
 * @author: Boris F. Calderon Sanchez
 */
public class EntradaWelcome extends InputAdapter {

    MyGdxGame game;
    public EntradaWelcome(MyGdxGame game) {
        this.game = game;
    }
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            game.setScreen(new ScreenPrincipal(game));
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }
}
