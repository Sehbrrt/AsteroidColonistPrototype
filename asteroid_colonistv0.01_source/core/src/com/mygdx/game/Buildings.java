package com.mygdx.game;

/**
 * Created by Sehbrrt on 11.01.2016.
 *
 * buildings with pure production value (has to be divided by a number with the production of the colony)
 */
public enum Buildings {
    WATER_RECYCLER(5, "1/4 less water consumed by population"),//population uses less water
    WATER_MINE(10, "+2 water * ressource pt, needs 1 space"),//water per turn
    ORE_MINE(10, "+1 ore * ressource pt, needs 1 space"),//ore per turn
    O_REFINERY(12, "+1 ore pt per 4 ore earned"),//more ore from existing ore
    HANGAR(12, "build ships, explorers, colonists,\n-5 energy, -5 ore"),//can build ships, explorers, colonists
    POWERPLANT(8, "2 energy pt"),//+energy
    SOLAR(5, "30 energy per 20 turns"),//+energy
    FACTORY(20, "4 energy pt"),//+en
    CLINIC(20, "3 water pt for the colony"),//+health
    SHIELD(20, "10 defense"),//+defense
    SHIELDII(40, "15 defense"),//+defense
    FARM(15, "2 pop * ressource pt, needs 1 space"),//+population per turn
    EXPLORER(10, "can explore new asteroids,\n-4 pop, -5 energy, -2 water, -2 ore"),
    COLONIST(20, "can build a new base,\n-10 pop, -5 energy, -2 water, -10 ore"),//+defense
    BATTLESHIP(10, "can explore new asteroids,\n-4 pop, -5 energy, -2 water, -2 ore"),
    DEPOT(50, "global goods -0.25 cheaper"),//better use of global stats for local
    OMEGA_REACTOR(70, "+1 prod,\n-30 energy, -5 ore");//+1production per turn(!)//could get overpowered//no stat exporting from it => just military?


    public final int prodValue;
    public final String effect;

     Buildings(int prodValue, String effect){
         this.prodValue = prodValue;
         this.effect = effect;
    }


}
