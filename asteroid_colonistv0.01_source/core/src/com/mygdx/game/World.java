package com.mygdx.game;


import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sehbrrt on 31.12.2015.
 *
 * contains all asteroids together (=map)
 */
public class World {

    MapGenerator mapGenerator;

    private ArrayList<Asteroid> asteroids;
    private HashMap<Integer, Colony> colonies;
    private int mapwidth;
    private int mapheight;
    private int asteroidcount;

    private RandomXS128 rand;
    private long seed;

    //test images ontop
    private Texture city1;
    private Texture city2;
    private Texture city3;
    private Texture mineIce;
    private Texture mineOre;
    private Texture farm;
    private Texture portWest;
    private Texture portEast;

    private Texture city1over;
    private Texture city2over;
    private Texture city3over;
    private Texture mineIceover;
    private Texture mineOreover;
    private Texture farmover;
    private Texture portWestover;
    private Texture portEastover;

    private Texture explored_under;
    private Texture foundice;
    private Texture foundore;
    private Texture foundfood;


    public boolean showStructures = true;

    //starting locations for point 0 0
    private int x0location = 30;//280
    private int y0location = 130;//190

    //used for drawing the asteroids
    private Asteroid tempAst;

    private boolean found = false;
    private boolean isFreeEntry = true;

    private int randX;
    private int randY;
    private int randLocX;
    private int randLocY;

    private BitmapFont bmf;

    private UI ui;

    private Faction player1;

    private ArrayList<Faction> npcfactions;

    private AI ai;

    //used for identifiing the colonies with the player
    public static String PLAYER_FACTION= "PLAYER1";

    private int npcfactionscount = 5;

    public World(int mapwidth, int mapheight, int asteroidcount){
        bmf = AsteroidColonist.getBmf();

        rand = new RandomXS128();
        seed = rand.nextLong();

        mapGenerator = new MapGenerator();

        this.asteroids = new ArrayList<Asteroid>();
        this.npcfactions = new ArrayList<Faction>();
        this.mapwidth = mapwidth;
        this.mapheight = mapheight;
        this.asteroidcount = asteroidcount;
        this.colonies = new HashMap<Integer, Colony>();


        //images
        city1 = new Texture("city1.png");
        city2 = new Texture("city2.png");
        city3 = new Texture("city3.png");
        mineIce = new Texture("mineice.png");
        mineOre = new Texture("mineore.png");
        farm= new Texture("farm.png");
        portWest = new Texture("portWest1.png");
        portEast = new Texture("portEast1.png");

        city1over = new Texture("city1over.png");
        city2over = new Texture("city2over.png");
        city3over = new Texture("city3over.png");
        mineIceover = new Texture("mineover.png");
        mineOreover = new Texture("mineover.png");
        farmover = new Texture("farmover.png");
        portWestover = new Texture("portWest1over.png");
        portEastover = new Texture("portEast1over.png");

        explored_under = new Texture("explored_under1.png");
        foundice=new Texture("foundice.png");
        foundore=new Texture("foundore.png");
        foundfood=new Texture("foundfood.png");




        generateWorld();
    }

    public void generateWorld(){
        asteroids.clear();
        ui = AsteroidColonist.getUI();
        //generate asteroids with random length and height

            //int anzprozeile = ((asteroidcount*100)/mapwidth)*((asteroidcount*100)/mapheight);
            //System.out.println("anz"+Math.sqrt(anzprozeile));
            //anzprozeile =(int) Math.sqrt(anzprozeile);

            randX = rand.nextInt(32) * 2 + 32;
            randY = rand.nextInt(32) * 2 + 32;
            randLocX = rand.nextInt(10);
            randLocY = rand.nextInt(10);

             asteroids.add(mapGenerator.bindPixmapFromHeightmap(randX, randY, rand.nextLong(), randLocX, randLocY));

           // ui.addCityButton("AST "+0, x0location+asteroids.get(0).getXcoord(),  y0location+asteroids.get(0).getYcoord()+asteroids.get(0).getHeight());


       // ui.addCityButton("FREE" + 0, x0location + asteroids.get(0).getXcoord(), y0location + asteroids.get(0).getYcoord() + asteroids.get(0).getHeight());

        int lastwidth = randX;
            int lastheigth = randY;

            int lastRandLocX;
            int lastRandLocY;
            for(int x=1; x<asteroidcount; x++) {
                randX = rand.nextInt(32) * 2 + 32;
                randY = rand.nextInt(32) * 2 + 32;
                //randLocX = rand.nextInt(mapwidth - randX);
                //randLocY = rand.nextInt(mapheight - randY);
                int counter = 0;
                if(randLocX<mapwidth) lastRandLocX = randLocX;
                else lastRandLocX = 0;
                if(randLocY<mapheight)lastRandLocY = randLocY;
                else lastRandLocY=0;
                found = false;
                boolean isfree = true;
                while(!found) {
                    // asteroids.add(mapGenerator.bindPixmapFromHeightmap(randX, randY, rand.nextLong(), randLocX, randLocY));
                    // found = true;
                    counter++;
                    isfree = true;

                    //randLocX = rand.nextInt(mapwidth);
                    //randLocY = rand.nextInt(mapheight);
                    //randLocX = rand.nextInt(asteroidmax)+asteroidmax*(x%xused);
                    //randLocY = rand.nextInt(asteroidmax)+asteroidmax*(x/xused);
                    for (int entry = 0; entry < asteroids.size(); entry++) {
                        if (getDiff(x0location + asteroids.get(entry).getXcoord(), x0location + randLocX) > asteroids.get(entry).getWidth()+24
                                || getDiff(y0location + asteroids.get(entry).getYcoord(), y0location + randLocY) > asteroids.get(entry).getHeight()+24
                                || getDiff(x0location + asteroids.get(entry).getXcoord(), x0location + randLocX) < -asteroids.get(entry).getWidth()-24
                                || getDiff(y0location + asteroids.get(entry).getYcoord(), y0location + randLocY) < -asteroids.get(entry).getHeight()-24) {


                        } else {
                            isfree = false;
                            if (randLocX < (lastRandLocX + 100) && randLocX < mapwidth) {
                                randLocX += rand.nextInt(80);
                                if (randLocY < mapheight) randLocY += rand.nextInt(80) - 40;
                            } else if (randLocY < (lastRandLocX + 100) && randLocY < mapheight) {
                                randLocY += rand.nextInt(80);
                                if (randLocX < mapwidth) randLocX += rand.nextInt(80) - 40;
                            }
                            if (randLocX > mapwidth) {
                                randLocX = rand.nextInt(20) - 10;
                                randLocY += rand.nextInt(120);
                                //lastRandLocX = 0;
                            }
                            if (randLocY > mapheight){
                                randLocY = 0;
                                //lastRandLocY=0;
                            }
                            //if(randLocY<(lastRandLocY+100))randLocY+=rand.nextInt(10);
                        }
                       // System.out.println("randl " + randLocX);


                        //System.out.println("lastr " + lastRandLocX);
                        //isFreeEntry = false;
                        // asteroids.remove(entry);
                        // System.out.println("awd");
                    }
                    if (isfree) {

                        asteroids.add(mapGenerator.bindPixmapFromHeightmap(randX, randY, rand.nextLong(), randLocX, randLocY));
                        found = true;
                        lastwidth = randX;
                        lastheigth = randY;
                        //randLocX=0;
                        //randLocY=0;
                        //lastRandLocX = randLocX;
                        //lastRandLocY = randLocY;
                       // System.out.println("-----");
                    }

                    //hardcoded if not found
                    if(counter > 100){
                        randLocX = rand.nextInt(mapwidth-100);
                        randLocY = rand.nextInt(mapheight-100);
                       // asteroids.add(mapGenerator.bindPixmapFromHeightmap(randX, randY, rand.nextLong(), randLocX, randLocY));
                        found = true;
                        x--;
                        lastwidth = randX;
                        lastheigth = randY;
                        //randLocX=0;
                        //randLocY=0;
                        //lastRandLocX = randLocX;
                        //lastRandLocY = randLocY;
                        //System.out.println("????-----hardcoded if not found");

                    }

                }




                //randLocX = rand.nextInt(20);
                //randLocY = rand.nextInt(20);

               // asteroids.add(mapGenerator.bindPixmapFromHeightmap(randX, randY, rand.nextLong(), randLocX+100*(x%3), randLocY+100*(x/3)));
               // bmf.draw(batch, "Asteroid Nr."+i, x0location +tempAst.getXcoord(),  y0location + tempAst.getYcoord()+tempAst.getHeight());


            }
        for(int j=0; j<asteroidcount; j++) {
            ui.addCityButton("FREE"+j, x0location+asteroids.get(j).getXcoord(),  y0location+asteroids.get(j).getYcoord()+asteroids.get(j).getHeight());

        }
       // ui.addCityButton("UNCOLONIZED"+0, x0location+asteroids.get(0).getXcoord(),  y0location+asteroids.get(0).getYcoord()+asteroids.get(0).getHeight());
       /*  int asteroidnumber = 0;
        colonies.put(asteroidnumber, new Colony(PLAYER_FACTION, asteroids.get(asteroidnumber)));
        colonies.get(asteroidnumber).getBuildingses().add(Buildings.HANGAR);
        colonies.get(asteroidnumber).getBuildingses().add(Buildings.WATER_RECYCLER);
        if (asteroids.get(asteroidnumber).getEntries().size()>2)colonies.get(asteroidnumber).getBuildingses().add(Buildings.ORE_MINE);
        asteroidnumber = 1;
        colonies.put(asteroidnumber, new Colony(PLAYER_FACTION, asteroids.get(asteroidnumber)));
        colonies.get(asteroidnumber).getBuildingses().add(Buildings.CLINIC);
        colonies.get(asteroidnumber).getBuildingses().add(Buildings.WATER_RECYCLER);
        ui.updateColonies(colonies);
        */

        //----------------------------------STARTCOLONIES------------------------------

        float rf = 0.2f;
        Color color = new com.badlogic.gdx.graphics.Color( java.awt.Color.HSBtoRGB(1f,1f,1f) );
        color.a=1;

        player1 = new Faction("PLAYER1", Color.RED);

        int randlocstart1 = rand.nextInt(asteroidcount);
        colonies.put(randlocstart1, new Colony(player1, asteroids.get(randlocstart1), "PLAYER_1"));
                player1.addColony(colonies.get(randlocstart1));
      // if(colonies.get(randlocstart1)!=null) System.out.println(colonies.get(randlocstart1).getName());


       // colonies.get(randlocstart1).addBuilding(Buildings.WATER_RECYCLER);
       // colonies.get(randlocstart1).addBuilding(Buildings.HANGAR);
       // if(colonies.get(randlocstart1).getMinesused().length>0)colonies.get(randlocstart1).addBuilding(Buildings.WATER_MINE);

        ui.updateColonies(colonies);
        ui.updateCityButtons(asteroids.indexOf(colonies.get(randlocstart1).getAsteroid()), colonies.get(randlocstart1).getName());
        ui.setPlayerFaction(player1);

        boolean isfound=false;
        while(!isfound){
            randlocstart1 = rand.nextInt(asteroidcount);
            if(!colonies.containsKey(randlocstart1))isfound=true;
        }
        colonies.put(randlocstart1, new Colony(player1, asteroids.get(randlocstart1), "PLAYER_1_2"));
        player1.addColony(colonies.get(randlocstart1));
      //  if(colonies.get(randlocstart1)!=null) System.out.println(colonies.get(randlocstart1).getName());


        colonies.get(randlocstart1).addBuilding(Buildings.WATER_RECYCLER);

        ui.updateColonies(colonies);
                ui.updateCityButtons(asteroids.indexOf(colonies.get(randlocstart1).getAsteroid()), colonies.get(randlocstart1).getName());
       // ui.updateColonieButtons();

        //npcfactions:
        for(int i=0; i<npcfactionscount; i++){
            rf = 1-((1/(float)npcfactionscount)*i)-(1/(float)npcfactionscount);
            color = new com.badlogic.gdx.graphics.Color( java.awt.Color.HSBtoRGB(rf,1f,1f) );
            color.a=1;
            npcfactions.add(new Faction("NPC#"+i, color));
            isfound = false;
            while(!isfound){
                randlocstart1 = rand.nextInt(asteroidcount);
                if(!colonies.containsKey(randlocstart1))isfound=true;
            }
            colonies.put(randlocstart1, new Colony(npcfactions.get(i), asteroids.get(randlocstart1), "NPC#"+i+"BASE"));
            npcfactions.get(i).addColony(colonies.get(randlocstart1));
            //colonies.get(randlocstart1).addBuilding(Buildings.WATER_RECYCLER);
            //if(colonies.get(randlocstart1).getMinesused().length>0)colonies.get(randlocstart1).addBuilding(Buildings.WATER_MINE);
            ui.updateCityButtons(randlocstart1, colonies.get(randlocstart1).getName());

        }
        ui.updateColonies(colonies);

        //for(HashMap.Entry<Integer,Colony> entry : colonies.entrySet()){
        //    System.out.println(entry.getValue().getFaction());
       // }





        ai=new AI(npcfactions, asteroids, colonies);


        //System.out.println(asteroids.size());

    }



    private int getDiff(int x, int y){
        return (x>y) ? x-y :y-x;
    }

    //called every turn
    public void next(){
        for(int i: colonies.keySet())colonies.get(i).next();
        player1.next();
        for(Faction f: npcfactions)f.next();
        ai.next(colonies);
        ui.nextColonyStats();
    }

    public void draw(SpriteBatch batch){


        for(int i=0; i<asteroids.size(); i++){
            batch.setColor(Color.WHITE);
            tempAst = asteroids.get(i);
            batch.draw(tempAst.getAsteroidtexture(), x0location + tempAst.getXcoord(), y0location + tempAst.getYcoord());
             //bmf.draw(batch, "Asteroid Nr.", x0location +tempAst.getXcoord(),  y0location + tempAst.getYcoord());
            //bmf.draw(batch, "Asteroid Nr."+i, x0location +tempAst.getXcoord(),  y0location + tempAst.getYcoord()+tempAst.getHeight());
            if(colonies.containsKey(i)){
           // if(colonies.get(i).getColonysize()==1) {
              //  batch.setColor(60,1,1,5);
               if(colonies.get(i).getFaction().getName().equals(PLAYER_FACTION)) batch.setColor(player1.getFactioncolor());
                else if(colonies.get(i).getFaction().getName().contains("NPC"))batch.setColor(npcfactions.get(Integer.parseInt(colonies.get(i).getFaction().getName().split("#")[1])).getFactioncolor());
                batch.draw(tempAst.getRectTexture(), x0location + tempAst.getXcoord(), y0location + tempAst.getYcoord());
                batch.setColor(Color.WHITE);

                if (showStructures) {
                    if (asteroids.get(i).getEntries().size() > 2) {
                        for (int j = 2; j < tempAst.getEntries().size(); j++)

                            if(colonies.get(i).getMinesused()[j-2]== MineType.MINE_ORE)
                                batch.draw(mineOre, x0location + tempAst.getXcoord() + tempAst.getEntries().get(j).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(j).y - 1);
                            else if(colonies.get(i).getMinesused()[j-2]== MineType.MINE_WATER)
                                batch.draw(mineIce, x0location + tempAst.getXcoord() + tempAst.getEntries().get(j).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(j).y - 1);
                            else if(colonies.get(i).getMinesused()[j-2]== MineType.FARM)
                                batch.draw(farm, x0location + tempAst.getXcoord() + tempAst.getEntries().get(j).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(j).y - 1);

                    }
                    if(colonies.get(i).getBuildingses().contains(Buildings.HANGAR)) {
                        if (asteroids.get(i).getIsPortWest())
                            batch.draw(portWest, x0location + tempAst.getXcoord() + tempAst.getEntries().get(1).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(1).y - 1);
                        else
                            batch.draw(portEast, x0location + tempAst.getXcoord() + tempAst.getEntries().get(1).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(1).y - 1);
                     }
                    if(colonies.get(i).isAttacked())batch.setColor(Color.RED);
                    switch(colonies.get(i).getColonysize()){
                        case 1:     batch.draw(city3, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1);
                            break;
                        case 2:batch.draw(city2, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1);
                            break;
                        case 3:batch.draw(city1, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1);
                            break;

                    }


                    if(colonies.get(i).getFaction().getName().equals(PLAYER_FACTION)) batch.setColor(player1.getFactioncolor());
                    else if(colonies.get(i).getFaction().getName().contains("NPC"))batch.setColor(npcfactions.get(Integer.parseInt(colonies.get(i).getFaction().getName().split("#")[1])).getFactioncolor());
                    if (asteroids.get(i).getEntries().size() > 2) {
                        for (int j = 2; j < tempAst.getEntries().size(); j++)
                            if(colonies.get(i).getMinesused()[j-2]== MineType.MINE_ORE)
                                batch.draw(mineOreover, x0location + tempAst.getXcoord() + tempAst.getEntries().get(j).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(j).y - 1);
                            else if(colonies.get(i).getMinesused()[j-2]== MineType.MINE_WATER)
                                batch.draw(mineIceover, x0location + tempAst.getXcoord() + tempAst.getEntries().get(j).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(j).y - 1);
                            else if(colonies.get(i).getMinesused()[j-2]== MineType.FARM)
                                batch.draw(farmover, x0location + tempAst.getXcoord() + tempAst.getEntries().get(j).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(j).y - 1);
                    }
                        if(colonies.get(i).getBuildingses().contains(Buildings.HANGAR)) {
                        if (asteroids.get(i).getIsPortWest())
                            batch.draw(portWestover, x0location + tempAst.getXcoord() + tempAst.getEntries().get(1).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(1).y - 1);
                        else
                            batch.draw(portEastover, x0location + tempAst.getXcoord() + tempAst.getEntries().get(1).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(1).y - 1);
                    }
                    switch(colonies.get(i).getColonysize()){
                        case 1:     batch.draw(city3over, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1);
                            break;
                        case 2:batch.draw(city2over, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1);
                            break;
                        case 3:batch.draw(city1over, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1);
                            break;

                    }  batch.setColor(Color.WHITE);
                }

            }

            //ressourcefound
            if(asteroids.get(i).isShowRes()) {
                batch.draw(explored_under, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1 - 1 - explored_under.getHeight());
                if (asteroids.get(i).getResources()[0] > 0) {
                    batch.draw(foundice, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1 - 1 - explored_under.getHeight());
                    if (asteroids.get(i).getResources()[0] > 1) {
                        batch.draw(foundice, 1 + x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1 - 1 - explored_under.getHeight());
                    }
                }
                if (asteroids.get(i).getResources()[1] > 0) {
                    batch.draw(foundore, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1 - 1 - explored_under.getHeight());
                    if (asteroids.get(i).getResources()[1] > 1) {
                        batch.draw(foundore, 1 + x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1 - 1 - explored_under.getHeight());
                    }
                }
                if (asteroids.get(i).getResources()[2] > 0) {
                    batch.draw(foundfood, x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1 - 1 - explored_under.getHeight());
                    if (asteroids.get(i).getResources()[2] > 1) {
                        batch.draw(foundfood, 1 + x0location + tempAst.getXcoord() + tempAst.getEntries().get(0).x, y0location + tempAst.getYcoord() + tempAst.getAsteroidtexture().getHeight() - tempAst.getEntries().get(0).y - 1 - 1 - explored_under.getHeight());
                    }
                }
            }
        }
    }

    //full colony
    public void addColonyFull(Faction f, int asteroid, String name){
        Colony c = new Colony(f, asteroids.get(asteroid), name);
        colonies.put(asteroids.indexOf(c.getAsteroid()), c);
        colonies.get(asteroids.indexOf(c.getAsteroid())).getFaction().addColony(c);
        ui.updateColonies(colonies);
        ui.updateColonieButtons(colonies.get(asteroids.indexOf(c.getAsteroid())));
       /* ui.addCityButton(colonies.get(asteroids.indexOf(c.getAsteroid())).getName(), colonies.get(asteroids.indexOf(c.getAsteroid())).getAsteroid().getXcoord(),
                colonies.get(asteroids.indexOf(c.getAsteroid())).getAsteroid().getYcoord());*/
        ui.updateCityButtons(asteroids.indexOf(c.getAsteroid()), colonies.get(asteroids.indexOf(c.getAsteroid())).getName());
    }

    public  void showAsteroidRes(int ast, boolean isshow){
        asteroids.get(ast).setShowRes(isshow);
    }

    public  ArrayList<Asteroid> getAsteroidRes(){
        return asteroids;
    }




}
