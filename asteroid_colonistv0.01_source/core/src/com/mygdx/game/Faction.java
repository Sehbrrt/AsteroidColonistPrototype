package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Sehbrrt on 16.01.2016.
 *
 * information for a player
 */
public class Faction {
    private ArrayList<Colony> colonies;
    private Color factioncolor;

    private String name;

    private int[] statstock = new int[StatsTypes.values().length];
    private int[] statsperturn = new int[StatsTypes.values().length];

    public Faction(String name, Color factioncolor){
        this.factioncolor = factioncolor;
        this.name = name;
        colonies = new ArrayList<Colony>();

        statstock[StatsTypes.POPULATION.ordinal()]=20;
        statstock[StatsTypes.PRODUCTION.ordinal()]=0;
        statstock[StatsTypes.WATER.ordinal()]=30;
        statstock[StatsTypes.ORE.ordinal()]=30;
        statstock[StatsTypes.ENERGY.ordinal()]=30;
    }

    //called every turn
    public void next(){
        for(int i=0; i<statstock.length;i++){
            statstock[i]=0;
            statsperturn[i]=0;
            for(int j=0; j<colonies.size();j++){
                statstock[i]+=colonies.get(j).getStatstock()[i];
                statsperturn[i]+=colonies.get(j).getStatsperturn()[i];
            }
        }
    }

    public ArrayList<Colony> getColonies() {
        return colonies;
    }

    public Color getFactioncolor() {
        return factioncolor;
    }

    public String getName() {
        return name;
    }

    public int[] getStatstock() {
        return statstock;
    }

    public int[] getStatsperturn() {
        return statsperturn;
    }

    public void addColony(Colony c){

        colonies.add(c);
        //startcolony size 3
        if(colonies.size()==1)colonies.get(0).setColonysize(3);
    }

    public void removeColony(Colony c){
        colonies.remove(c);
    }

}
