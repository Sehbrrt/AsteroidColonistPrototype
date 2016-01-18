package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sehbrrt on 29.12.2015.
 */
public class MapGenerator {

    private int width;
    private int height;
    private Pixmap pixmap;
    private Texture pixmapTexture;
    private Pixmap pixmapRect;
    private Texture textureRect;
    private RandomXS128 rand;
    //min distance for points
    private int distance = 16;
    private ArrayList<Point> points;

    private Color GRAY1 = new Color(3333);

    private float flatteningdegr = 5f;
    private int stonenoise = 10;

    //test images ontop
    private Texture city;
    private Texture mine;
    private Texture portWest;
    private Texture portEast;

    public boolean showStructures = true;

    private int citywidth=14;
    private int cityheight=9;

    private int portwidth=12;
    private int portheight=5;

    //is the port west or east?
    private boolean isPortWest;

    //used for checking if new entry is not colliding with existing ones
    private boolean isFreeEntry = false;



    public MapGenerator(){
        this.width = 64;
        this.height = 64;
        points = new ArrayList<Point>();
        //long seed = 1214959234;
        //RandomXS128 testrandom = new RandomXS128();
        //seed=testrandom.nextLong();
        //rand = new RandomXS128(seed);


        //images
        city = new Texture("city1.png");
        mine = new Texture("mineice.png");
        portWest = new Texture("portWest1.png");
        portEast = new Texture("portEast1.png");

    }

    public void draw(SpriteBatch batch){
        batch.draw(pixmapTexture, 280, 190);
        //test ports
        //batch.draw(portWest, 280, 190);
        //batch.draw(portEast, 300, 190);

        if(showStructures){
             if(points.size()>2){
                for(int i=2; i<points.size(); i++)batch.draw(mine, 280+points.get(i).x, 190+height-points.get(i).y-1);
            }
            if(isPortWest)batch.draw(portWest, 280+points.get(1).x, 190+height-points.get(1).y-1);
            else batch.draw(portEast, 280+points.get(1).x, 190+height-points.get(1).y-1);
            batch.draw(city, 280+points.get(0).x, 190+height-points.get(0).y-1);

        }
    }

    public float[][] generateHeightMap(){
        float[][] hm = new float[width][height];

        for(int i=0; i<width; i+=2){
            for(int j=0; j<height; j+=2){
                hm[i][j] = (rand.nextBoolean()) ? 1.0f : -1.0f;
                hm[i+1][j]= hm[i][j];
                hm[i][j+1]= hm[i][j];
                hm[i+1][j+1]= hm[i][j];
            }
        }

        for(int i =0; i<width; i++){
            for(int j = 0; j<height; j++){
                if((rand.nextInt(10)<10-j) && (hm[i][j] >0)) hm[i][j] = -1.0f;
                if((rand.nextInt(10)<10-i) && (hm[i][j] >0)) hm[i][j] = -1.0f;
                if((rand.nextInt(10)<10-j) && (hm[i][height-j-1] >0)) hm[i][height-j-1] = -1.0f;
                if((rand.nextInt(10)<10-i) && (hm[width-i-1][j] >0)) hm[width-i-1][j] = -1.0f;




            }
        }

        //one red dot
        boolean found = false;
        int counter = 0;
       // System.out.println("vorwhilepoint");
        while(!found){
            counter ++;
            int i = rand.nextInt(width-distance-citywidth)+citywidth;
            int j = rand.nextInt(height-distance-cityheight)+cityheight;
            if(hm[i][j]>0.8&&hm[i+citywidth][j-cityheight]>0.8&&hm[i+citywidth][j]>0.8 &&hm[i][j-cityheight]>0.8 &&hm[i+(citywidth/2)][j-(cityheight/2)]>0.8
                    &&hm[i+(citywidth/4)][j]>0.8&&hm[i+(citywidth/4)][j-(cityheight/4)]>0.8
                    &&i>distance&&i<(width-distance)&&j>distance&&j<(height-distance)) {
                //pixmap.drawPixel(i, j, Color.RED.toIntBits());
                found = true;
                points.add(new Point(i, j));
                //System.out.println(points.get(0).x+".."+points.get(0).y);
            }

            //hardcoded if while loop is endless
            if(counter == 65000){
                //pixmap.drawPixel(width/2, height/2, Color.RED.toIntBits());
                found = true;
                points.add(new Point(width/2, height/2));
                //System.out.println(points.get(0).x+".."+points.get(0).y+"hardcoded");
            }
        }

        //more where red dot
        for(int i =1; i<width-1; i++) {
            for (int j = 1; j < height-1; j++) {
                //if(i>distance&&i<(width-distance)&&j>distance&&j<(height-distance)) {
                    if ((rand.nextInt(10) < 10 - j) && (hm[i][(points.get(0).y)] < 0)) hm[i][(points.get(0).y) - j] = +1.0f;
                    if ((rand.nextInt(10) < 10 - i) && (hm[(points.get(0).x)][j] < 0)) hm[(points.get(0).x)][j] = +1.0f;
                //}
            }
        }

        return hm;
    }

    public float[][] rendMap(float[][] hm, int x){
        float [][] hmCopy = hm;


            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < height - 1; j++) {
                    float sum = (hm[i - x][j - x] + hm[i][j - x] + hm[i + x][j - x] + hm[i - x][j] + hm[i + x][j] + hm[i - x][j + x] + hm[i][j + x] + hm[i + x][j + x]) / (flatteningdegr);
                    hmCopy[i][j] = sum;
                }
            }

        return hmCopy;

    }

    public Asteroid bindPixmapFromHeightmap(int asteroidwidth, int asteroidheight, long seed, int xlocation, int ylocation){
        //set back for new asteroid
        width = asteroidwidth;
        height = asteroidheight;
        points = new ArrayList<Point>();
        flatteningdegr = 5f;
        //RandomXS128 testrandom = new RandomXS128(seed);
        //seed=testrandom.nextLong();
        rand = new RandomXS128(seed);
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.fill();
        pixmap.setColor(Color.CYAN);
        pixmapRect = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmapRect.fill();
        pixmapRect.setColor(Color.CYAN);


        float[][] hm = generateHeightMap();
        hm = rendMap(hm, 1);
        hm = rendMap(hm, 1);
        flatteningdegr = 10f;
        hm = rendMap(hm, 1);
        hm = rendMap(hm, 1);
        hm = rendMap(hm, 1);


        //-------------------------------------------------------------------------------------------------------------
        //COLORING
        int col;

        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
               //if(hm[i][j]>-0.4f) pixmap.drawPixel(i, j, 1668);
                if(hm[i][j]>-0.4f) {
                    //the division for rarer color change
                    //  col = (int)(hm[i][j]/1+1 + rand.nextFloat());
                    pixmap.drawPixel(i, j, Color.WHITE.toIntBits());
                    //   pixmap.drawPixel(i, j, 1000+col+10*col+100*col);
                   // if ((int) (hm[i][j] * 10) > -0.6) {
                        if ((int) (hm[i][j] * 10) > -0.4) {
                            if ((int) (hm[i][j] * 10) > -0.2) {
                                if ((int) (hm[i][j] * 10) > 0) {
                                    if ((int) (hm[i][j] * 10) > 0.8) {
                                        if ((int) (hm[i][j] * 10) > 2.2) {
                                            if ((int) (hm[i][j] * 10) > 5.2) {
                                                if ((int) (hm[i][j] * 10) > 10) {
                                                    if ((int) (hm[i][j] * 10) > 14) {
                                                        pixmap.drawPixel(i, j, 8555 + rand.nextInt(stonenoise));
                                                    } else pixmap.drawPixel(i, j, 9333 + rand.nextInt(stonenoise));
                                                } else pixmap.drawPixel(i, j, 7555 + rand.nextInt(stonenoise));
                                            } else pixmap.drawPixel(i, j, 8333 + rand.nextInt(stonenoise));
                                        } else pixmap.drawPixel(i, j, 7333 + rand.nextInt(stonenoise));
                                    } else pixmap.drawPixel(i, j, 6555 + rand.nextInt(stonenoise));
                                } else pixmap.drawPixel(i, j, 6333 + rand.nextInt(stonenoise));
                            } else pixmap.drawPixel(i, j, 5333 + rand.nextInt(stonenoise));
                        } else pixmap.drawPixel(i, j, 4555 + rand.nextInt(stonenoise));
                   // } else pixmap.drawPixel(i, j, 3777 + rand.nextInt(stonenoise));
                }

                //else pixmap.drawPixel(i, j, Color.CLEAR.toIntBits());
            }
        }

        //tests to see where city is placed
        //pixmap.drawPixel(points.get(0).x+(citywidth/2), points.get(0).y-(cityheight/2), Color.WHITE.toIntBits());
        //pixmap.drawPixel(points.get(0).x+citywidth, points.get(0).y-cityheight, Color.WHITE.toIntBits());

        //-----------------------------------------------------------------------------------------------------
        //Spaceport
        //method like finding the red spot city place
        // add this before the fields at second entry of the list, random direction (north/south/east/west)
        // then first pixel that is solid (free space in the direction)(and has enough space (hangarwidth/length) plus checking if colliding with city
        // after that update the field entry search because now there are 2 entries before

        // often not found => because in distance limits => no limits and try catch


        boolean found = false;
        int counter = 0;
        boolean isspacefree = true;
        //System.out.println("vorwhilespaceport");
        while(!found){
            counter ++;
            isspacefree = true;
            isPortWest = rand.nextBoolean();
            int i = rand.nextInt(width-portwidth)+portwidth;
            int j = rand.nextInt(height-portheight)+portheight;
            if(isPortWest) {
                //look if not colliding with city and free space to the WEST
                //AND if not colliding with city
                try {
                    if (hm[i][j] < -0.4 && hm[i + portwidth][j - portheight] > -0.4 && hm[i + portwidth][j] > -0.4 && hm[i][j - portheight] < -0.4 && hm[i + (portwidth / 2)][j - (portheight / 2)] < -0.4
                            && hm[i + (portwidth / 4)][j] < -0.4 && hm[i + (portwidth / 4)][j - (portheight / 4)] < -0.4 &&

                            (getDiff(points.get(0).x, i) > citywidth || getDiff(points.get(0).y, j) > cityheight
                                    || getDiff(points.get(0).x, i) < -citywidth || getDiff(points.get(0).y, j) < -cityheight)
                            ) {

                        //test if space is free (port has access to space in a line)
                        for (int x = 0; x < i; x++) {
                            if (hm[x][j] > -0.4) isspacefree = false;
                        }

                        if (isspacefree) {
                            //pixmap.drawPixel(i, j, Color.RED.toIntBits());
                            found = true;
                            points.add(new Point(i, j));
                            //System.out.println(points.get(0).x + ".." + points.get(0).y + "abkbaksd_WEST");
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    try {
                        //if out of bounds of the asteroid base, but with the end on the asteroid
                        if (hm[i + portwidth][j - portheight] > -0.4 &&hm[i + portwidth][j] > -0.4) {
                            //pixmap.drawPixel(i, j, Color.RED.toIntBits());
                            found = true;
                            points.add(new Point(i, j));
                            //System.out.println(points.get(0).x + ".." + points.get(0).y + "abkbaksd_WEST_caught");
                        }
                    } catch(ArrayIndexOutOfBoundsException e2){
                        //not wanted
                    }

                }
            } else{
                //look if not colliding with city and free space to the EAST
                //AND if not colliding with city
                try{
                    if(hm[i][j]>-0.4&&hm[i+portwidth][j-portheight]<-0.4&&hm[i+portwidth][j]<-0.4 &&hm[i][j-portheight]>-0.4 &&hm[i+(portwidth/2)][j-(portheight/2)]<-0.4
                            &&hm[i+(portwidth/4)][j]<-0.4&&hm[i+(portwidth/4)][j-(portheight/4)]<-0.4&&

                            (getDiff(points.get(0).x, i) > citywidth || getDiff(points.get(0).y, j) > cityheight
                                    || getDiff(points.get(0).x, i) < -citywidth || getDiff(points.get(0).y, j) < -cityheight)
                            ) {

                        //test if space is free (port has access to space in a line)
                        for(int x=width-1; x>i; x--){
                            if(hm[x][j]>-0.4) isspacefree = false;
                        }

                        if(isspacefree) {
                            //pixmap.drawPixel(i, j, Color.RED.toIntBits());
                            found = true;
                            points.add(new Point(i, j));
                            //System.out.println(points.get(0).x + ".." + points.get(0).y + "abkbaksd_EAST");
                        }
                    }
                    //if out of bounds of the asteroid base, but with the end on the asteroid
                }catch (ArrayIndexOutOfBoundsException e){
                    try {
                        if (hm[i][j] > -0.4&&hm[i][j-portheight] > -0.4) {
                            //pixmap.drawPixel(i, j, Color.RED.toIntBits());
                            found = true;
                            points.add(new Point(i, j));
                            //System.out.println(points.get(0).x + ".." + points.get(0).y + "abkbaksd_EAST_caught");
                        }
                    } catch(ArrayIndexOutOfBoundsException e1){
                        //not wanted
                    }
                }

            }

            //hardcoded if while loop is endless
            if(counter == 65000){
                pixmap.drawPixel(width/4, height/4, Color.RED.toIntBits());
                found = true;
                points.add(new Point(width/4, height/4));
               // System.out.println(points.get(0).x+".."+points.get(0).y+"spaceport NOT FOUND");
            }
        }

        //test to see where ports are out of bounds
       // pixmap.drawRectangle(0,0,width,height);


        //-----------------------------------------------------------------------------------------------------
        //FINDING FIELDS
        int fieldheight = 8;
        int fieldwidth = 8;
        //finding fields - bad efficiency?
        for(int x=0; x< 300; x++){
            //int i = rand.nextInt(width-distance-citywidth)+citywidth;
            int i = rand.nextInt(width-fieldwidth);
            //int j = rand.nextInt(height-distance-cityheight)+cityheight;
            int j = rand.nextInt(height-fieldheight)+fieldheight;
            //System.out.println(i+", "+j);
            if(hm[i][j]>0.0&&hm[i+fieldwidth][j-fieldheight]>0.0&&hm[i+fieldwidth][j]>0.0 &&hm[i][j-fieldheight]>0.0 &&hm[i+(fieldwidth/2)][j-(fieldheight/2)]>0.0
                  ) {

                //add a field
                //look if the new field is not in the way of another city/field/etc entry
                isFreeEntry=false;
                //check city
                if (getDiff(points.get(0).x, i) > citywidth || getDiff(points.get(0).y, j) > cityheight
                        || getDiff(points.get(0).x, i) < -citywidth || getDiff(points.get(0).y, j) < -cityheight) isFreeEntry=true;
                //check port
                if (getDiff(points.get(1).x, i) > portwidth || getDiff(points.get(1).y, j) > portheight
                        || getDiff(points.get(1).x, i) < -portwidth || getDiff(points.get(1).y, j) < -portheight){

                } else isFreeEntry = false;
                //check other entries
                if(points.size()>2) {
                    for (int entry = 2; entry < points.size(); entry++) {
                        if (getDiff(points.get(entry).x, i) > fieldwidth || getDiff(points.get(entry).y, j) > fieldheight
                                || getDiff(points.get(entry).x, i) < -fieldwidth || getDiff(points.get(entry).y, j) < -fieldheight) {


                        } else isFreeEntry = false;
                    }
                }

                if(isFreeEntry){

                    //points.add(new Point(i, j));
                    //pixmap.drawPixel(i, j, Color.RED.toIntBits());
                    pixmapRect.setColor(Color.WHITE.toIntBits());
                    pixmapRect.drawRectangle(i, j - fieldheight + 1, fieldwidth, fieldheight);
                    //pixmap.drawPixel(i + (fieldwidth / 2), j - (fieldheight / 2), Color.WHITE.toIntBits());
                    //pixmap.drawPixel(i + fieldwidth, j - fieldheight, Color.WHITE.toIntBits());
                    points.add(new Point(i, j));
                    //System.out.println(points.get(0).x + ".." + points.get(0).y + "bla");
                } else{
                    //test for not used fields (because of overlapping)
                    //pixmap.setColor(Color.WHITE.toIntBits());
                    //pixmap.drawRectangle(i, j - fieldheight + 1, fieldwidth, fieldheight);
                }
            }
        }

        pixmapTexture = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
        textureRect = new Texture(pixmapRect, Pixmap.Format.RGBA8888, false);
       // System.out.println(points.get(1).x+"xpoint0");

        //resources density
        int[] res = new int[3];
        int re;
        for(int i=0; i<res.length; i++){
            re= rand.nextInt(4);
            res[i]=0;
            if(re>0){
                res[i]++;
                if(re>2)res[i]++;
            }
        }

        return new Asteroid(points, pixmapTexture,textureRect, isPortWest, xlocation, ylocation, asteroidwidth, asteroidheight, res);




    }

    private int getDiff(int x, int y){
        return (x>y) ? x-y :y-x;
    }

}
