package com.mygdx.game;

import java.util.ArrayList;

/**
 * Created by Sehbrrt on 11.01.2016.
 */

public class Colony {
   // private int asteroidnumber;
    private Faction faction;
    private Asteroid asteroid;
    private ArrayList<Buildings> buildingses;
    private int colonysize;

    private Buildings producingbuilding;
    private int turnsToFinish;

    private int[] productioninvested = new int[Buildings.values().length];

    private int[] statstock = new int[StatsTypes.values().length];
    private int[] statsperturn = new int[StatsTypes.values().length];

    private int proddivid = 5;

    private boolean isfinishbuilding = true;

    //building/balancing attributes
    private int personsperwater = 16;
    private float waterpopconsumption = 1;
    private float popperturnnegativewater = 1/2;
    private int waterproduction = 0;

    private int oreprodafter4 = 1;

    private int energyafter20= 0;

    private int productionafter10=0;

    private boolean isSpaceport = false;


    private int turns = 0;
    private float globaltolocal=0.5f;

    private int temppoppt = 1;


    private String name;



    public MineType[] minesused;
    private int minescount;


    public boolean isAttacked() {
        return isAttacked;
    }

    public void setIsAttacked(boolean isAttacked) {
        this.isAttacked = isAttacked;
    }

    public Colony getAttacker() {
        return attacker;
    }

    public void setAttacker(Colony attacker) {
        this.attacker = attacker;
    }

    public int getEnemyships() {
        return enemyships;
    }

    public void setEnemyships(int enemyships) {
        this.enemyships = enemyships;
    }

    private boolean isAttacked = false;
    private Colony attacker;
    private int enemyships = 0;




    public Colony(Faction faction, Asteroid asteroid, String name){
        //this.asteroidnumber = asteroidnumber;
        this.faction = faction;
        this.asteroid = asteroid;
        this.asteroid.setShowRes(true);
        this.name = name;
        buildingses = new ArrayList<Buildings>();
        colonysize = 1;
        statstock[StatsTypes.POPULATION.ordinal()]=1;
        statstock[StatsTypes.DEFENSE.ordinal()]=10;
        statstock[StatsTypes.PRODUCTION.ordinal()]=1;
        statstock[StatsTypes.WATER.ordinal()]=1;
        statsperturn[StatsTypes.POPULATION.ordinal()]=1;
        minescount=0;
        minesused = new MineType[asteroid.getEntries().size()-2];
        //System.out.println(asteroid.getEntries()+"asteroidentries");
        for(int i=0; i<minesused.length;i++){
            minesused[i]=MineType.NO_MINE;
        }
        //System.out.println(minesused.length+"minesund");

    }

    //everyturn
    public void next(){
        turns++;

        //----------------------STATS----------------------
        //waterconsumption
        //System.out.println((-(int)((statstock[StatsTypes.POPULATION.ordinal()]/personsperwater)*waterpopconsumption)));
        statstock[StatsTypes.WATER.ordinal()]= (-(int)((statstock[StatsTypes.POPULATION.ordinal()]/personsperwater)*waterpopconsumption));
        //population decline from water -
      //  if(statstock[StatsTypes.WATER.ordinal()]<0){
      //      //statstock[StatsTypes.POPULATION.ordinal()]-= statsperturn[StatsTypes.POPULATION.ordinal()];
      //      statsperturn[StatsTypes.POPULATION.ordinal()]=0;
      //      if(statstock[StatsTypes.WATER.ordinal()]<10)statsperturn[StatsTypes.POPULATION.ordinal()]=-1;
      //  } else{
      //      statsperturn[StatsTypes.POPULATION.ordinal()]=temppoppt;
       //     temppoppt=statsperturn[StatsTypes.POPULATION.ordinal()];
      //  }
        //if(statstock[StatsTypes.WATER.ordinal()]<-20)statstock[StatsTypes.POPULATION.ordinal()]-=1;

        //ore
        if(turns%4==0)statstock[StatsTypes.ORE.ordinal()]+=oreprodafter4;

        //energy
        if(turns%20==0)statstock[StatsTypes.ENERGY.ordinal()]+=energyafter20;

        //production
        if(turns%10==0)statstock[StatsTypes.PRODUCTION.ordinal()]+=productionafter10;

        if(statstock[StatsTypes.WATER.ordinal()]<0)statstock[StatsTypes.POPULATION.ordinal()]-=statsperturn[StatsTypes.POPULATION.ordinal()];

        if( statstock[StatsTypes.ATTACK.ordinal()]>0&&statstock[StatsTypes.HEALTH.ordinal()]>0) statstock[StatsTypes.ATTACK.ordinal()]-= (statstock[StatsTypes.HEALTH.ordinal()]-(statstock[StatsTypes.HEALTH.ordinal()]/statstock[StatsTypes.DEFENSE.ordinal()]));
        else statstock[StatsTypes.POPULATION.ordinal()]-= (statstock[StatsTypes.HEALTH.ordinal()]);

        if(statstock[StatsTypes.POPULATION.ordinal()]<0&&isAttacked){
            faction.removeColony(this);
            faction = attacker.getFaction();
            isAttacked=false;

            statstock[StatsTypes.POPULATION.ordinal()]=4;
            statstock[StatsTypes.ATTACK.ordinal()]=statstock[StatsTypes.HEALTH.ordinal()];
            statstock[StatsTypes.HEALTH.ordinal()]=0;
            //System.out.println(this.getName());
            //if(this==null) System.out.println("dead?");
           // System.out.println(attacker.getFaction().getName());
           // if(attacker.getFaction()==null) System.out.println("asddefgh");


            attacker.getFaction().addColony(this);
            attacker=null;
        }

        //updating values
        for(int i=0; i<statstock.length;i++){
            statstock[i]+=statsperturn[i];
        }
        //---------------------------------------------------

        //buildingproduction
        if(turnsToFinish>0){
           // System.out.println("ah");
            isfinishbuilding=false;
            turnsToFinish-=(1+((statstock[StatsTypes.PRODUCTION.ordinal()])/10));
            productioninvested[producingbuilding.ordinal()] +=(1+((statstock[StatsTypes.PRODUCTION.ordinal()])/10));
            if(turnsToFinish<=0)finishBuilding();

        }
        //citysize
        if(statstock[StatsTypes.POPULATION.ordinal()]>200&&statstock[StatsTypes.POPULATION.ordinal()]<2000)colonysize=2;
        if(statstock[StatsTypes.POPULATION.ordinal()]>2000)colonysize=3;
    }

    public int getColonysize(){
        return colonysize;
    }

   /* public int getAsteroidnumber(){
        return asteroidnumber;
    }*/

    public Faction getFaction(){
        return faction;
    }

    public ArrayList<Buildings> getBuildingses(){
        return buildingses;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public void setColonysize(int colonysize) {
        this.colonysize = colonysize;
    }



    public void setBuildingses(ArrayList<Buildings> buildingses) {
        this.buildingses = buildingses;
    }

    public void setProducingbuilding(Buildings producingbuilding){
        this.producingbuilding = producingbuilding;
        turnsToFinish = producingbuilding.prodValue;
        isfinishbuilding=false;
    }

    public void addBuilding(Buildings b){
        buildingses.add(b);
        addBuildingEffect(b);
    }

    public int getTurnsToFinish(){
        return turnsToFinish;
    }

    public Buildings getProducingbuilding(){
        return producingbuilding;
    }

    public void finishBuilding(){
        if( addBuildingEffect(producingbuilding))buildingses.add(producingbuilding);
        producingbuilding = null;
        turnsToFinish=0;
        isfinishbuilding=true;
    }

    //production for all buildings is saved locally in colony
    public void setProductioninvested(int location, int valueadd){
        productioninvested[location]+=valueadd;
    }

    public int[] getProductioninvested(){
        return productioninvested;
    }

    public int[] getStatstock() {
        return statstock;
    }

    public int[] getStatsperturn() {
        return statsperturn;
    }

    public void addStatstock(StatsTypes type, int value){
        statstock[type.ordinal()]+=value;
    }

    public void addStatsperturn(StatsTypes type, int value){
        statsperturn[type.ordinal()]+=value;
    }

    public boolean isfinishbuilding() {
        return isfinishbuilding;
    }

    public Asteroid getAsteroid(){
        return asteroid;
    }
/*WATER_RECYCLER(5, "1/4 less water consumed by population"),//population uses less water
    WATER_MINE(10, "+2 water pt"),//water per turn
    ORE_MINE(10, "+1 ore pt"),//ore per turn
    O_REFINERY(12, "+1 ore pt per 4 ore earned"),//more ore from existing ore
    HANGAR(12, "build ships, explorers, colonists,\n-5 energy, -5 ore"),//can build ships, explorers, colonists
    POWERPLANT(7, "2 energy pt"),//+energy
    SOLAR(7, "30 energy per 20 turns"),//+energy
    FACTORY(20, "+1 prod per 10 rounds"),//+prod
    CLINIC(20, " 10 health"),//+health for the colony
    FARM(15, "2 pop pt"),//+population per turn
    SHIELD(20, "20 defense"),//+defense
    EXPLORER(10, "can explore new asteroids,\n-4 pop, -5 energy, -2 water, -2 ore"),
    COLONIST(20, "can build a new base,\n-10 pop, -5 energy, -2 water, -10 ore"),//+defense
    BATTLESHIP(10, "can explore new asteroids,\n-4 pop, -5 energy, -2 water, -2 ore"),
    DEPOT(50, "global goods -0.25 cheaper"),//better use of global stats for local
    OMEGA_REACTOR(70, "+1 prod pt,\n-30 energy, -5 ore");//+1production per turn(!)//could get overpowered//no stat exporting from it => just military?*/
    //add the effect of the building that is finished
    public boolean addBuildingEffect(Buildings building){
        //System.out.println("mines:"+minescount+" "+name);
        switch(building){
            case WATER_RECYCLER : waterpopconsumption=0.75f;
                statsperturn[StatsTypes.WATER.ordinal()]++;
                break;
            case WATER_MINE :  if(minesused.length>minescount){
                statsperturn[StatsTypes.WATER.ordinal()]+=2*asteroid.getResources()[0];
                //System.out.println(minesused.length+","+minescount);
                minesused[minescount]=MineType.MINE_WATER;
                minescount++;
            }else return false;

               // waterproduction+=2;
                break;
            case ORE_MINE : if(minesused.length>minescount){
                statsperturn[StatsTypes.ORE.ordinal()]+=1*asteroid.getResources()[1];
                //System.out.println(minesused.length+","+minescount);
                minesused[minescount]=MineType.MINE_ORE;
                minescount++;
            } else return false;

                break;
            case O_REFINERY: oreprodafter4=1;
                break;
            case HANGAR: isSpaceport=true;
                break;
            case POWERPLANT: statsperturn[StatsTypes.ENERGY.ordinal()]+=2;
                break;
            case SOLAR: energyafter20=30;
                break;
            case FACTORY: statsperturn[StatsTypes.ENERGY.ordinal()]+=4;
                break;
            case CLINIC: statsperturn[StatsTypes.WATER.ordinal()]+=3;
                break;
            case SHIELD: statstock[StatsTypes.DEFENSE.ordinal()]+=10;
                break;
            case SHIELDII: statstock[StatsTypes.DEFENSE.ordinal()]+=30;
                break;
            case FARM:if(minesused.length>minescount){
                statsperturn[StatsTypes.POPULATION.ordinal()]+=1*asteroid.getResources()[2];
                minesused[minescount]=MineType.FARM;
                minescount++;
            }else return false;

                break;
            case EXPLORER:if(statstock[StatsTypes.ENERGY.ordinal()]>=5&&statstock[StatsTypes.ORE.ordinal()]>=2&&
                        statstock[StatsTypes.POPULATION.ordinal()]>=6&&statstock[StatsTypes.WATER.ordinal()]>=2) {
                    statstock[StatsTypes.POPULATION.ordinal()] -= 4;
                    statstock[StatsTypes.ENERGY.ordinal()] -= 5;
                    statstock[StatsTypes.WATER.ordinal()] -= 2;
                    statstock[StatsTypes.ORE.ordinal()] -= 2;
                    statstock[StatsTypes.EXPLORER.ordinal()] += 1;
                    productioninvested[Buildings.EXPLORER.ordinal()] = 0;
                }else return false;
                break;
            case COLONIST: if(statstock[StatsTypes.ENERGY.ordinal()]>=30&&statstock[StatsTypes.ORE.ordinal()]>=10&&
                        statstock[StatsTypes.POPULATION.ordinal()]>=20&&statstock[StatsTypes.WATER.ordinal()]>=2) {
                    statstock[StatsTypes.POPULATION.ordinal()] -= 10;
                    statstock[StatsTypes.ENERGY.ordinal()] -= 30;
                    statstock[StatsTypes.WATER.ordinal()] -= 2;
                    statstock[StatsTypes.ORE.ordinal()] -= 10;
                    statstock[StatsTypes.COLONIST.ordinal()] += 1;
                    productioninvested[Buildings.COLONIST.ordinal()] = 0;
                }else return false;
                break;
            case BATTLESHIP: if(statstock[StatsTypes.ENERGY.ordinal()]>=5&&statstock[StatsTypes.ORE.ordinal()]>=2&&
                        statstock[StatsTypes.POPULATION.ordinal()]>=5&&statstock[StatsTypes.WATER.ordinal()]>=2) {
                    statstock[StatsTypes.POPULATION.ordinal()] -= 4;
                    statstock[StatsTypes.ENERGY.ordinal()] -= 20;
                    statstock[StatsTypes.WATER.ordinal()] -= 2;
                    statstock[StatsTypes.ORE.ordinal()] -= 2;
                    statstock[StatsTypes.ATTACK.ordinal()] += 1;
                    productioninvested[Buildings.BATTLESHIP.ordinal()] = 0;
                }else return false;
                break;
            case DEPOT:  globaltolocal+=0.25f;
                break;
            case OMEGA_REACTOR:  if(statstock[StatsTypes.ENERGY.ordinal()]>=30&&statstock[StatsTypes.ORE.ordinal()]>=5){
                statstock[StatsTypes.PRODUCTION.ordinal()]+=1;
                statstock[StatsTypes.ENERGY.ordinal()]-=30;
                statstock[StatsTypes.ORE.ordinal()]-=5;
                 }else return false;
                break;



        }
        return true;
    }

    public boolean getIsSpacePort(){
        return isSpaceport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MineType[] getMinesused() {
       // if(minesused.length>0)System.out.println(minesused[0]);
        MineType[] a = minesused;
        return a;
    }

    public MineType getMinesused(int a) {
        //if(minesused.length>0)System.out.println(minesused[0]);
        return minesused[a];
    }

    public int getMinescount(){
        return minescount;
    }

    public void sendFight(Colony tocolony){
        statstock[StatsTypes.ATTACK.ordinal()]-=1;
        //attack
        if(!faction.getName().equals(tocolony.getFaction().getName())){
            tocolony.addStatstock(StatsTypes.HEALTH, 1);
            tocolony.attacker=this;
            tocolony.isAttacked=true;
        } else{
            //move ships
            tocolony.addStatstock(StatsTypes.ATTACK, 1);
        }

    }




}
