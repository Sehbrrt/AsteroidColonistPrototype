package com.mygdx.game;

import com.badlogic.gdx.math.RandomXS128;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sehbrrt on 17.01.2016.
 */
public class AI {
    private ArrayList<Faction> factions;
    private ArrayList<Buildings>tempbuildings;
    private RandomXS128 rand;
    private ArrayList<Asteroid>asteroids;
    private HashMap<Integer, Colony>colonies;
    private int newcolast=-1;
    private Colony newColFrom;

    public AI(ArrayList<Faction> factions, ArrayList<Asteroid> asteroids, HashMap<Integer, Colony> colonies){
        this.factions=factions;
        this.rand=new RandomXS128();
        tempbuildings=new ArrayList<Buildings>();
        this.asteroids=asteroids;
        this.colonies=colonies;
    }

    public void removeFaction(Faction f){
        if(factions.contains(f))factions.remove(f);
    }

    public void addFaction(Faction f){
        if(!factions.contains(f))factions.add(f);
    }

    //called every turn
    public void next(HashMap<Integer, Colony>col){
        colonies=col;
        for(int i=0; i< Buildings.values().length;i++) {
            tempbuildings.add(Buildings.values()[i]);
        }
        for(Faction f: factions){
            for(Colony c:f.getColonies()){
               while(c.isfinishbuilding()){
                    //tempbuildings.retainAll(c.getBuildingses());
                   if(rand.nextBoolean()) {
                       for (int i = 0; i < Buildings.values().length; i++) {
                           if (Buildings.values()[i].name().contains("MINE") || Buildings.values()[i].name().contains("FARM")) {
                               if (c.getMinescount() < c.getMinesused().length - 1 && c.getMinesused().length > 0)
                                   c.setProducingbuilding(Buildings.values()[i]);
                               if (c.getProducingbuilding() != null) {
                                   System.out.println(c.getProducingbuilding().name());

                                   break;
                               }
                           }
                           if (!c.getBuildingses().contains(Buildings.values()[i])) {


                               c.setProducingbuilding(Buildings.values()[i]);

                               if (c.getProducingbuilding() == null) {
                                   System.out.println("cnull");

                               }

                           } else {
                              // System.out.println("notbuilding");
                           }
                           if (c.getProducingbuilding() != null) {
                               System.out.println(c.getProducingbuilding().name());

                               break;
                           }


                       }
                   } else{
                       //Units
                       if(rand.nextInt(10)>5){
                           if(c.getStatstock()[StatsTypes.COLONIST.ordinal()]==0) {

                               if (c.getStatstock()[StatsTypes.ENERGY.ordinal()] >= 30 && c.getStatstock()[StatsTypes.ORE.ordinal()] >= 10 &&
                                       c.getStatstock()[StatsTypes.POPULATION.ordinal()] >= 20 && c.getStatstock()[StatsTypes.WATER.ordinal()] >= 2) {
                                   c.setProducingbuilding(Buildings.COLONIST);
                                   if (c.getProducingbuilding() != null) {
                                       System.out.println(c.getProducingbuilding().name());

                                       break;
                                   }
                               }
                           }
                       }else{
                           if(c.getStatstock()[StatsTypes.ATTACK.ordinal()]<=20){
                           if(c.getStatstock()[StatsTypes.ENERGY.ordinal()]>=5&&c.getStatstock()[StatsTypes.ORE.ordinal()]>=2&&
                                   c.getStatstock()[StatsTypes.POPULATION.ordinal()]>=5&&c.getStatstock()[StatsTypes.WATER.ordinal()]>=2) {
                               c.setProducingbuilding(Buildings.BATTLESHIP);
                               if (c.getProducingbuilding() != null) {
                                   System.out.println(c.getProducingbuilding().name());

                                   break;
                               }
                           }

                           }
                       }


                   }

                 }
                if(c.getStatstock()[StatsTypes.ATTACK.ordinal()]>10){
                    System.out.println(c.getName()+",SHIPS:"+c.getStatstock()[StatsTypes.ATTACK.ordinal()]);
                    if(colonies.size()<asteroids.size()){
                        for(int i=0; i<asteroids.size();i++){
                            if(!f.getColonies().contains(colonies.get(i))) {
                                //AsteroidColonist.addColony(c.getFaction(), i, c.getFaction().getName()+ "_" + (c.getFaction().getColonies().size()+1));

                                break;
                            }
                        }
                    }
                }
                if(c.getStatstock()[StatsTypes.COLONIST.ordinal()]>0){
                    System.out.println(c.getName()+",COLONISTS:"+c.getStatstock()[StatsTypes.COLONIST.ordinal()]);
                    if(colonies.size()<asteroids.size()){
                        for(int i=0; i<asteroids.size();i++){
                            if(!colonies.containsValue(i)) {
                                //AsteroidColonist.addColony(c.getFaction(), i, c.getFaction().getName()+ "_" + (c.getFaction().getColonies().size()+1));
                                newcolast=i;
                                newColFrom=c;

                                c.getStatstock()[StatsTypes.COLONIST.ordinal()]-=1;
                                break;
                            }
                        }
                    }
                }
            }
            if(newcolast!=-1&&newColFrom!=null){
                AsteroidColonist.addColony(newColFrom.getFaction(), newcolast, newColFrom.getFaction().getName()+ "_" + (newColFrom.getFaction().getColonies().size()+1));
                newColFrom=null;
                newcolast=-1;
            }
        }
    }
}
