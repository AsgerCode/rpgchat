package org.academiadecodigo.bootcamp;

/**
 * Created by codecadet on 05/07/2017.
 */
public class RandomNum {

    public static int randomNum(int min, int max) {
        min = (int) Math.ceil(min);
        max = (int) Math.floor(max);
        return (int) Math.floor(Math.random() * (max - min)) + min;
    }

}
