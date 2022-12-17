package com.thepross.actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.thepross.MyGdxGame;

import java.util.List;

/**
 * Esta clase define el objeto Laser la cual contiene la hitBox, texturas usadas en el juego principal.
 * @author: Boris F. Calderon Sanchez
 */
public class Laser extends Actor {

    MyGdxGame game;
    private float xPosition, yPosition;
    private float width, height;
    private float velocidad;
    public boolean loop;
    public int direccion;
    public TextureRegion region;
    Rectangle boundingBox;
    public boolean estado;
    private List<Enemigo> listEnemigos;
    ShapeRenderer shapeRenderer;

    public Laser(MyGdxGame game, TextureRegion textura, float x, float y, float width, float height, List<Enemigo> listEnemigos) {
        this.game = game;
        this.region = textura;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.boundingBox = new Rectangle(xPosition, yPosition, this.width, this.height);
        setBounds(this.xPosition, this.yPosition, this.width, this.height);
        this.estado = true;
        this.velocidad = 800;
        this.direccion = 0;
        this.listEnemigos = listEnemigos;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        draw(batch, parentAlpha, this.xPosition, this.yPosition, this.width, this.height);
    }

    public void draw(Batch batch, float parentAlpha, float xPosition, float yPosition, float width, float height) {
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.rect(boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
//        shapeRenderer.end();
        if (estado) {
            if (!loop) {
                this.xPosition = xPosition + 10;
                this.yPosition = yPosition;
            } else {
                batch.draw(region, this.xPosition, this.yPosition, this.width, this.height);
            }
            boundingBox.set(this.xPosition, this.yPosition, this.width, this.height);
        } else {
            boundingBox.set(this.xPosition, -1000, this.width, this.height);
        }
    }

    @Override
    public void act(float delta) {
        if (loop) {
            if (this.yPosition > Gdx.graphics.getHeight()) {
                estado = false;
                loop = false;
            }
            // colision
            for (Enemigo enemigo : listEnemigos) {
                if (interseccion(enemigo.boundingBox)) {
                    game.getAssetManager().get("audio/hit.wav", Sound.class).play();
                    estado = false;
                    loop = false;
                    enemigo.estadoMuerto();
                }
            }
            this.yPosition = this.yPosition + this.velocidad * delta;
        }
    }

    public boolean interseccion(Rectangle otroRectangle) {
        return boundingBox.overlaps(otroRectangle);
    }

}
