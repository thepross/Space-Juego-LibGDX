package com.thepross.pantallas;

import com.badlogic.gdx.Screen;
import com.thepross.MyGdxGame;

public abstract class ScreenBase implements Screen {
    protected MyGdxGame game;

    public ScreenBase(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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

    }
}
