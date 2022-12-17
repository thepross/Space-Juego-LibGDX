package com.thepross.actores;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.thepross.Constants;
import com.thepross.MyGdxGame;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Esta clase define el objeto Jugador(NAVE) la cual contiene la hitBox, texturas y balas usadas en el juego principal.
 * @author: Boris F. Calderon Sanchez
 */
public class Jugador extends Actor {

    private static final int COL = 6, ROW = 1;

    MyGdxGame game;
    private List<Texture> texturas;
    public boolean arriba, abajo, izquierda, derecha;
    public int direccion;
    public int vida;
    public int velocidad;

    float xPosition, yPosition;
    float width, height;
    Rectangle boundingBox;

    public Animation<TextureRegion> walkAnimation;
    Texture walkSheet;
    public TextureRegion regionIdle;
    float stateTime;

    public TextureRegion regionIzq;
    public TextureRegion region;
    public TextureRegion regionDer;
    public TextureRegion regionLaser;
    // laser
    public LinkedList<Laser> playerLaser;
    public Texture texturaLaser;
    public int currentLaser;
    public boolean shoot;
    private float tiempo;
    private List<Enemigo> listEnemigos;
    private int currentEnemigo;
    Random numAleatorio;

    public Jugador(MyGdxGame game, List<Texture> texturas, float x, float y, float width, float height, List<Enemigo> listEnemigos, TextureRegion regionLaser, int vida) {
        this.game = game;
        this.texturas = texturas;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        crearAnimacionIdle();
        this.boundingBox = new Rectangle(xPosition, yPosition, this.width, this.height);
        this.setBounds(this.xPosition, this.yPosition, this.width, this.height);
        this.listEnemigos = listEnemigos;
        this.direccion = 0;
        this.vida = vida;
        this.velocidad = 200;

        // LASER
        texturaLaser = texturas.get(2);
        playerLaser = new LinkedList<>();
        this.regionLaser = regionLaser;
        for (int i = 1; i <= 100; i++) {
            playerLaser.add(new Laser(this.game, this.regionLaser, 0, 0, 16, 32, this.listEnemigos));
        }
        currentLaser = 100;
        shoot = false;
        currentEnemigo = 0;

        regionIzq = new TextureRegion(texturas.get(0), 0, 0, 8, 8);
        this.region = new TextureRegion(texturas.get(0), 8, 0, 8, 8);
        regionDer = new TextureRegion(texturas.get(0), 16, 0, 8, 8);
    }


    public boolean interseccion(Rectangle otroRectangle) {
        return boundingBox.overlaps(otroRectangle);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        boundingBox.set(xPosition, yPosition, width, height);

        if (currentLaser == 0) {
            recargarLaser();
        }
        for (Laser laser : playerLaser) {
            laser.draw(batch, parentAlpha, xPosition, yPosition, width, height);
        }

        if (izquierda) batch.draw(regionIzq, xPosition, yPosition, width, height);
        if (derecha) batch.draw(regionDer, xPosition, yPosition, width, height);
        if (!izquierda && !derecha) batch.draw(region, xPosition, yPosition, width, height);

    }

    @Override
    public void act(float delta) {
        // COLISION
        for (Enemigo enemigo : listEnemigos) {
            if (interseccion(enemigo.boundingBox)) {
                game.getAssetManager().get("audio/hit.wav", Sound.class).play();
                enemigo.estadoMuerto();
                this.vida--;
            }
        }
        movimiento(delta);
        setPosition(xPosition, yPosition);

        for (Laser laser : playerLaser) {
            laser.act(delta);
        }

        // MOVIMIENTO DE LOS ENEMIGOS CADA 3 SEGUNDOS
        movimientoEnemigos(delta, 3);

    }

    public void recargarLaser() {
        playerLaser = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            playerLaser.add(new Laser(this.game, this.regionLaser, 0, 0, 16, 32, this.listEnemigos));
        }
        currentLaser = 100;
    }

    public void movimientoEnemigos(float delta, float tiempoRestart) {
        listEnemigos.get(currentEnemigo).perseguirPosition = this.xPosition;
        tiempo += delta;
        if (tiempo > tiempoRestart) {
            numAleatorio = new Random();
            currentEnemigo = numAleatorio.nextInt(listEnemigos.size());
            while (!listEnemigos.get(currentEnemigo).estaVivo && !ganador()) {
                currentEnemigo = numAleatorio.nextInt(listEnemigos.size());
            }
            listEnemigos.get(currentEnemigo).perseguir();
            tiempo = 0;
        }
    }

    public boolean ganador() {
        for (Enemigo enemigo : listEnemigos) {
            if (enemigo.estaVivo) return false;
        }
        return true;
    }

    public void movimiento(float delta) {
        if (arriba) {
            if (yPosition < Constants.BACKGROUND_HEIGHT - height)
                yPosition += this.velocidad * delta;
        }
        if (abajo) {
            if (yPosition >= 0)
                yPosition -= this.velocidad * delta;
        }
        if (izquierda) {
            if (xPosition >= 0)
                xPosition -= this.velocidad * delta;
        }
        if (derecha) {
            if (xPosition < Constants.BACKGROUND_WIDTH - width)
                xPosition += this.velocidad * delta;
        }

    }

    public void moverArriba(boolean b) {
        if (abajo && b) abajo = false;
        arriba = b;
    }

    public void moverAbajo(boolean b) {
        if (arriba && b) arriba = false;
        abajo = b;
    }

    public void moverIzquierda(boolean b) {
        if (derecha && b) derecha = false;
        izquierda = b;
    }

    public void moverDerecha(boolean b) {
        if (izquierda && b) izquierda = false;
        derecha = b;
    }


    private void crearAnimacionIdle() {
        walkSheet = texturas.get(0);
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

    public boolean gameOver() {
        return this.vida <= 0;
    }

    public int cantidadEnemigosVivos() {
        int count = 0;
        for (Enemigo enemigo : listEnemigos) {
            if (enemigo.estaVivo) count++;
        }
        return count;
    }

}
