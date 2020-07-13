package com.maxlytvynchuk.dinogame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.maxlytvynchuk.dinogame.R;
import com.maxlytvynchuk.dinogame.activities.GameActivity;
import com.maxlytvynchuk.dinogame.models.DrawElement;

import java.util.Iterator;
import java.util.LinkedList;

public class GameView extends SurfaceView implements Runnable {
    private GameActivity activity;
    Thread thread;
    private boolean isPlaying;
    private SurfaceHolder surfaceHolder;

    // Screen
    private int screenX;
    private int screenY;


    // Dino
    private Bitmap[] dino;
    private int dinoX;
    private int dinoY;
    private boolean dinoInJump;
    private boolean dinoStep;

    // Ground
    private LinkedList<DrawElement> ground;

    // Cactus
    private LinkedList<DrawElement> cactus;
    // Speed
    private int gameSpeed;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        activity = (GameActivity) context;

        surfaceHolder = getHolder();
        // Screen
        this.screenX = screenX;
        this.screenY = screenY;

        // Speed
        gameSpeed = 30;

        //Dino
        dino = new Bitmap[6];
        dino[0] = BitmapFactory.decodeResource(getResources(), R.drawable.dino1);
        dino[0] = Bitmap.createScaledBitmap(dino[0], dino[0].getWidth(), dino[0].getHeight(), false);
        dino[1] = BitmapFactory.decodeResource(getResources(), R.drawable.dino2);
        dino[2] = BitmapFactory.decodeResource(getResources(), R.drawable.dino3);
        dino[3] = BitmapFactory.decodeResource(getResources(), R.drawable.dino4);
        dino[4] = BitmapFactory.decodeResource(getResources(), R.drawable.dino5);
        dino[5] = BitmapFactory.decodeResource(getResources(), R.drawable.dino6);
        dinoX = 80;
        dinoY = screenY - dino[0].getHeight() - 10;

        // Ground
        final int groundCount = (int) Math.ceil(screenX / 71);
        ground = new LinkedList<>();
        for (int i = 0; i < groundCount; i++) {
            Bitmap groundBitmap = createGroundBitmap();
            int groundX = i * groundBitmap.getWidth();
            DrawElement groundElement = new DrawElement(groundX, screenY - groundBitmap.getHeight(), gameSpeed, groundBitmap);
            ground.add(groundElement);
        }

        // Cactus
        final int cactusCount = 4;
        int cactusLastPos = screenX + 10;
        cactus = new LinkedList<>();
        for (int i = 0; i < cactusCount; i++) {
            int cactusRandomX = cactusLastPos + 600 + (int) (Math.random() * screenX);
            int cactusRandomY = screenY - (1 + (int) Math.floor(Math.random() * 10)) - 10;
            int randomCactusType = (int) Math.floor(Math.random());
            Bitmap cactusBitmap;
            if (randomCactusType == 0) {
                cactusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cactus1);
            } else {
                cactusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cactus2);
            }
            cactusRandomY -= cactusBitmap.getHeight();
            DrawElement newCactus = new DrawElement(cactusRandomX, cactusRandomY, gameSpeed, cactusBitmap);
            cactus.add(newCactus);
            cactusLastPos = cactusRandomX;
        }
    }

    // Return Bitmap of random ground from 3 variants
    public Bitmap createGroundBitmap() {
        Bitmap groundPart;
        int rand = (int) (Math.random() * 300);
        if (rand <= 5) {
            groundPart = BitmapFactory.decodeResource(getResources(), R.drawable.land1);
        } else if (rand <= 10) {
            groundPart = BitmapFactory.decodeResource(getResources(), R.drawable.land3);
        } else {
            groundPart = BitmapFactory.decodeResource(getResources(), R.drawable.land2);
        }
        return groundPart;
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void drawGround(Canvas canvas) {
        // Ground
        DrawElement firstGround = ground.get(0);
        Iterator groundIterator = ground.iterator();
        while (groundIterator.hasNext()) {
            DrawElement groundElement = (DrawElement) groundIterator.next();
            canvas.drawBitmap(groundElement.getBitmap(), groundElement.getX(), groundElement.getY(), null);
            groundElement.setX(groundElement.getX() - groundElement.getSpeed());
        }
        if (firstGround.getX() <= -firstGround.getBitmap().getWidth()) {
            ground.removeFirst();
            Bitmap newGroundBitmap = createGroundBitmap();
            DrawElement lastGround = ground.getLast();
            int newGroundX = lastGround.getX() + lastGround.getBitmap().getWidth();
            int newGroundY = screenY - newGroundBitmap.getHeight();
            DrawElement newGround = new DrawElement(newGroundX, newGroundY, gameSpeed, newGroundBitmap);
            ground.add(newGround);
        }
    }

    private void drawCactus(Canvas canvas){
        Iterator<DrawElement> cactusIterator = cactus.iterator();
        int cactusFirstPos = (cactus.getFirst()).getX();
        while (cactusIterator.hasNext()) {
            DrawElement currentCactus = cactusIterator.next();
            canvas.drawBitmap(currentCactus.getBitmap(), currentCactus.getX(), currentCactus.getY(), null);
            currentCactus.setX(currentCactus.getX() - gameSpeed);
        }
        if (cactusFirstPos < 0) {
            cactus.removeFirst();
            DrawElement lastCactus = cactus.getLast();
            int cactusRandomX = Math.max(lastCactus.getX() + 600, screenX + (int) Math.floor(Math.random() * screenX));
            int cactusRandomY = screenY - (1 + (int) Math.floor(Math.random() * 10)) - 10;
            int randomCactusType = (int) Math.floor(Math.random() * 2);
            Bitmap cactusBitmap;
            if (randomCactusType == 0) {
                cactusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cactus1);
            } else {
                cactusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cactus2);
            }
            cactusRandomY -= cactusBitmap.getHeight();
            DrawElement newCactus = new DrawElement(cactusRandomX, cactusRandomY, gameSpeed, cactusBitmap);
            cactus.add(newCactus);
        }
    }

    private void checkForCollisions(){
        // Check cactus collisions

        for (DrawElement current : cactus) {
            if (dinoX + dino[0].getWidth() >= current.getX() && (dinoX + dino[0].getWidth()) <= (current.getY() + current.getBitmap().getHeight())
                    && (dinoY > current.getY() - current.getBitmap().getHeight())) {
                activity.gameOver();
                break;
            }
        }
    }

    private void drawDino(Canvas canvas){
        if (dinoInJump) {
            if (dinoY <= screenY - dino[0].getHeight() - 350) {
                dinoInJump = false;
            } else {
                dinoY -= 50;
            }
            canvas.drawBitmap(dino[2], dinoX, dinoY, null);
        } else {
            if (dinoY < screenY - dino[0].getHeight() - 10) {
                dinoY += 50;
            }
            if (dinoY != screenY - dino[0].getHeight() - 10) {
                canvas.drawBitmap(dino[2], dinoX, dinoY, null);
            } else if (dinoStep) {
                canvas.drawBitmap(dino[0], dinoX, dinoY, null);
                dinoStep = false;
            } else {
                canvas.drawBitmap(dino[1], dinoX, dinoY, null);
                dinoStep = true;
            }
        }
    }

    private void draw() {

        if (surfaceHolder.getSurface().isValid()) {

            // Get canvas
            Canvas canvas = getHolder().lockCanvas();

            // Clear previous drawing
            canvas.drawColor(Color.WHITE);

            // Draw Ground
            drawGround(canvas);

            // Draw Cactus
            drawCactus(canvas);

            // Draw Dino
            drawDino(canvas);

            // Draw canvas
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        checkForCollisions();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    private void sleep() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!dinoInJump && dinoY == screenY - dino[0].getHeight() - 10) {
                dinoInJump = true;
            }
        }
        return true;
    }
}