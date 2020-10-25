package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class ActiveCounter {
    int number;
    Random random;
    int reciProbab;
    short left;
    short right;
    public ActiveCounter(int number){
        this.number = number;
        initialize();
    }

    private void initialize() {
        random = new Random();
        reciProbab = (int)Math.pow(2, 16);
    }
    private boolean toss(int prob){
        int v = (int)Math.pow(2, prob);
        if(prob == 0){
            return true;
        }
        return random.nextInt(v) == 0;
    }

    public void simulate(){
        int counter = number;
        left = Short.MIN_VALUE;
        right = Short.MIN_VALUE;
        while(counter > 0){
                if(toss(Math.abs(Short.MIN_VALUE - right))){
                    if(left == Short.MAX_VALUE){
                        left = 0;
                        right++;
                    }else{
                        left++;
                    }

            }

            counter--;

        }

        int fLeft = Math.abs(Short.MIN_VALUE - left);
        int fRight = Math.abs(Short.MIN_VALUE - right);
        System.out.print("Final result: ");
        System.out.println((int)(fLeft*Math.pow(2, fRight)));

    }


    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the value to count");
            int count = Integer.parseInt(reader.readLine());
            ActiveCounter activeCounter = new ActiveCounter(count);
            activeCounter.simulate();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
