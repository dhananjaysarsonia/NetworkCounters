package com.company;

import java.awt.image.ImageProducer;
import java.io.*;
import java.util.HashMap;
import java.util.Random;

public class CountMin {

    private int nHash;
    private int nCounters;
    private int[][] counters;
    private int counterSize;
    private String[] flows;
    private Random random;
    private int[] hashes;
    private static final int IP = 0;
    private static final int COUNT = 1;


    //num flows, number of counters, counter width
    public CountMin( String[] flows, int nCounters, int counterSize){
        this.nCounters = nCounters;
        this.counterSize = counterSize;
        this.nHash = nCounters;
        this.flows = flows;
        initialize();
    }


    private void initialize() {
        random = new Random();
        hashes = random.ints( 1, Integer.MAX_VALUE).distinct().limit(nHash).toArray();
        counters = new int[nCounters][counterSize];
    }

    private String[] getFlowKV(String kv){
        return kv.split("\\s+");
    }

    private void insert(String kv){
        String[] map = getFlowKV(kv);
        for(int i = 0; i < nHash; i++){
            counters[i][getIndex(map[IP], i)]+= Integer.parseInt(map[COUNT]);
        }
    }

    private int lookUp(String ipString){
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < nHash; i++){
            min = Math.min(min, counters[i][getIndex(ipString, i)]);
        }
        return min;
    }

    private int getErrorForFlow(int index){
        String[] map = getFlowKV(flows[index]);
        return lookUp(map[IP]) - Integer.valueOf(map[COUNT]);
    }
    private double calAndPrintAverage(){
        double sum = 0;
        for(int i = 0; i < flows.length; i++){
            sum+= getErrorForFlow(i);
        }
        return sum/flows.length;
    }


    private void simulate(){
        //insertion
        for(int i = 0; i < flows.length; i++){

            insert(flows[i]);
        }
        //print error
        System.out.println(calAndPrintAverage());
    }



    private int getIndex(String element, int index){
        int converted = Math.abs(element.hashCode());
        return (hashes[index] ^ converted)% counterSize;
    }

    public static void main(String[] args) {
        try {
            String path = "project3input.txt";
            int nCounters = 3;
            int counterSize = 3000;
            //input Reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter name of file after placing it into the project directory");
            path = reader.readLine();
            System.out.println("Please enter the number of counters");
            nCounters = Integer.parseInt(reader.readLine());

            System.out.println("Please enter the size of each counter");
            counterSize = Integer.parseInt(reader.readLine());


            //File Reader

            FileInputStream fileInputStream = new FileInputStream(path);
            BufferedReader br = new BufferedReader( new InputStreamReader(fileInputStream));
            String string;
            boolean first = true;
            String[] input = null;

            int index = 0;
            while((string = br.readLine()) != null){
                if(first){
                    first = false;
                    input = new String[Integer.parseInt(string)];
                }else{
                    input[index] = string;
                    index++;
                }
            }

            CountMin countMin = new CountMin(input,nCounters,counterSize);
            countMin.simulate();






        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
