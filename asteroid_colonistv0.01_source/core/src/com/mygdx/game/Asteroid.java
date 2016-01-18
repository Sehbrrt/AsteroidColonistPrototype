package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sehbrrt on 31.12.2015.
 *
 * contains all information needed for an asteroid
 */
public class Asteroid {
    private ArrayList<Point> entries;
    private Texture asteroidtexture;
    private Texture rectTexture;
    private int xcoord;
    private int ycoord;
    private boolean isPortWest;
    private int width;
    private int height;


    private boolean showRes = false;


    private int[] resources;

    public Asteroid(ArrayList<Point> entries,Texture asteroidtexture, Texture recttexture, boolean isPortWest, int xcoord, int ycoord, int width, int height, int[]resources){
        this.entries = entries;
        this.asteroidtexture = asteroidtexture;
        this.rectTexture = recttexture;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.isPortWest = isPortWest;
        this.width = width;
        this.height = height;
        this.resources=resources;
    }

    public ArrayList<Point> getEntries() {
        return entries;
    }


    public Texture getAsteroidtexture() {
        return asteroidtexture;
    }

    public Texture getRectTexture() {
        return rectTexture;
    }

    public int getXcoord() {
        return xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public boolean getIsPortWest(){
        return isPortWest;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    public int[] getResources() {
        return resources;
    }

    public void setResources(int[] resources) {
        this.resources = resources;
    }


    public boolean isShowRes() {
        return showRes;
    }

    public void setShowRes(boolean showRes) {
        this.showRes = showRes;
    }
}
