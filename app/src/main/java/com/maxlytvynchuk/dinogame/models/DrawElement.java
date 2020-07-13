package com.maxlytvynchuk.dinogame.models;
import android.graphics.Bitmap;

public class DrawElement {
    private int x;
    private int y;
    private int speed;
    private Bitmap bitmap;

    public DrawElement(int x, int y, int speed, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
