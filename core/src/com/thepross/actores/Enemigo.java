package com.thepross.actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.thepross.pantallas.ScreenPrincipal;

/**
 * Esta clase define el objeto Enemigo la cual contiene la hitBox, texturas usadas en el juego principal.
 * @author: Boris F. Calderon Sanchez
 */
public class Enemigo extends Actor {

    private float xPosition, yPosition;
    float width, height;
    private float velocidad;
    public Animation<TextureRegion> walkAnimation;
    public Texture walkSheet;
    public TextureRegion regionIdle;
    private static final int COL = 8, ROW = 1;
    float stateTime;
    public Rectangle boundingBox;
    ShapeRenderer shapeRenderer;
    public boolean estaVivo;
    public boolean animacionExplosion;
    public TextureRegion region;
    private float tiempo;
    private boolean perseguir;
    public float perseguirPosition;
    public int direccion;
    public float DEFAULT_XPOSITION;
    public float DEFAULT_YPOSITION;

    public Enemigo(TextureRegion textura, float x, float y, float width, float height) {
        this.region = textura;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.boundingBox = new Rectangle(xPosition, yPosition, this.width, this.height);
        setBounds(this.xPosition, this.yPosition, this.width, this.height);
        this.estaVivo = true;
        crearAnimacionIdle();
        this.velocidad = 200;
        shapeRenderer = new ShapeRenderer();
        this.perseguir = false;
        this.direccion = 0;
        DEFAULT_XPOSITION = this.xPosition;
        DEFAULT_YPOSITION = this.yPosition;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.rect(boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
//        shapeRenderer.end();
        if (this.estaVivo) {
            boundingBox.set(this.xPosition, this.yPosition, this.width, this.height);
            batch.draw(region, this.xPosition, this.yPosition, this.width, this.height);
        } else {
            if (this.animacionExplosion) {
                if (stateTime <= 1.0f) {
                    batch.draw(regionIdle, this.xPosition, this.yPosition, this.width, this.height);
                } else {
                    this.animacionExplosion = false;
                }
            }
            boundingBox.set(this.xPosition, -2000, this.width, this.height);
        }
    }

    @Override
    public void act(float delta) {
        //this.yPosition = this.yPosition - velocidad * delta;
        if (this.animacionExplosion) {
            if (stateTime <= 1.0f) {
                stateTime += delta;
                regionIdle = walkAnimation.getKeyFrame(stateTime, false);
            } else {
                this.animacionExplosion = false;
            }
        }

        // LOOP MOVIMIENTO
        movimientoEstatico(delta);
        seguimiento(delta);

    }

    public void movimientoEstatico(float delta) {
        tiempo += delta;
        if (tiempo > 0.25f && direccion == 0) {
            this.xPosition = this.xPosition - 4;
            direccion = 1;
        }
        if (tiempo > 0.5f && direccion == 1) {
            this.xPosition = this.xPosition - 4;
            direccion = 2;
        }
        if (tiempo > 0.75f && direccion == 2) {
            this.xPosition = this.xPosition + 4;
            direccion = 3;
        }
        if (tiempo > 1.0f && direccion == 3) {
            this.xPosition = this.xPosition + 4;
            direccion = 0;
            tiempo = 0.0f;
        }
    }

    public void seguimiento(float delta) {
        if (perseguir && estaVivo) {
            if (perseguirPosition > this.xPosition) {
                this.xPosition += 1;
            } else {
                this.xPosition -= 1;
            }
            if (this.yPosition > 0) {
                this.yPosition = this.yPosition - velocidad * delta;
            } else {
                this.perseguir = false;
                this.yPosition = DEFAULT_YPOSITION;
                this.xPosition = DEFAULT_XPOSITION;
            }
        }
    }

    public void perseguir() {
        perseguir = true;
    }

    private void crearAnimacionIdle() {
        walkSheet = new Texture(Gdx.files.internal("explosion.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / COL,
                walkSheet.getHeight() / ROW);
        TextureRegion[] walkFrames = new TextureRegion[COL * ROW];
        int index = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation<TextureRegion>(0.1f, walkFrames);
        stateTime = 0f;
        regionIdle = walkAnimation.getKeyFrame(0);
    }

    public void estadoMuerto() {
        this.estaVivo = false;
        this.animacionExplosion = true;
    }

}
