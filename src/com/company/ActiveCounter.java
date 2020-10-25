package com.company;

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
        reciProbab = (int)Math.pow(2,16);
    }
    private boolean toss(){
        return random.nextInt(reciProbab) == 0;
    }

    public void simulate(){
        int counter = number;
        int probOn = 0;
        int shortCounter = 0;
        while(counter > 0){

            if(probOn < 2){
                if(left == Short.MAX_VALUE){
                    probOn++;
                }
                if(probOn == 2){
                    left = 0;
                    right++;
                }else{
                    left++;
                }
            }else{
                if(toss()){
                    if(shortCounter < 2){
                        if(left == Short.MAX_VALUE){
                            left = 0;
                            shortCounter++;
                        }
                        if(shortCounter == 2){

                            right++;
                        }else{
                            left++;
                        }
                    }else{
                        shortCounter = 0;
                        left++;

                    }
                }
            }



            counter--;
        }


        //int fValue = left * (int)Math.pow(2, right);
        System.out.println(left * (int)Math.pow(2, right));


    }


    public static void main(String[] args) {
        ActiveCounter activeCounter = new ActiveCounter(1000000);
        activeCounter.simulate();

    }

}
