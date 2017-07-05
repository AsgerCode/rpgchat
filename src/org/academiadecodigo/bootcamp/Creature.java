package org.academiadecodigo.bootcamp;

/**
 * Created by codecadet on 05/07/2017.
 */
public class Creature {

    private int level;
    private int strength;
    private int intellect;
    private int agility;
    private int stamina;
    private double hp = 10 * (stamina * 0.5);

    public Creature(int level){
        this.level = level;
        strength = level * RandomNum.randomNum(3, 7);
        intellect = level * RandomNum.randomNum(3, 7);
        agility = level * RandomNum.randomNum(3, 7);
        stamina = level * RandomNum.randomNum(3, 7);
    }

    public void takeDamage(int damage){
        hp -= damage;

        if(hp <= 0){

        }
    }
}

