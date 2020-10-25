package com.company;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CounterSketch {

    private int nHash;
    private int nCounters;
    private int[][] counters;
    private int counterSize;
    private String[] flows;
    private Random random;
    private int[] hashes;
    private static final int IP = 0;
    private static final int COUNT = 1;
    private static final int INDEX = 0;
    PriorityQueue<int[]> q;
    public CounterSketch(String[] flows, int nCounters, int counterSize){
        this.nCounters = nCounters;
        this.counterSize = counterSize;
        this.nHash = nCounters;
        this.flows = flows;
        initialize();

    }
    private void initialize() {
        random = new Random();
        q = new PriorityQueue<>((a,b) -> {
            return a[COUNT] - b[COUNT];
        });
        hashes = random.ints( 1, Integer.MAX_VALUE).distinct().limit(nHash).toArray();
        counters = new int[nCounters][counterSize];
    }

    private String[] getFlowKV(String kv){
        return kv.split("\\s+");
    }

    private void insert(String kv){
        String[] map = getFlowKV(kv);
        for(int i = 0; i < nHash; i++){
            if(isNegative(map[IP], i)){
                counters[i][getIndex(map[IP], i)]-= Integer.parseInt(map[COUNT]);
            }else{
                counters[i][getIndex(map[IP], i)]+= Integer.parseInt(map[COUNT]);
            }

        }
    }

    private int lookUp(String ipString){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < nHash; i++){
            list.add(Math.abs(counters[i][getIndex(ipString, i)]));
        }
        Collections.sort(list);

        if(list.size()%2 == 0){
            int mid = list.size()/2;
            return (list.get(mid) + list.get(mid + 1))/2;
        }else{
            int mid = list.size();
            return list.get(mid / 2);
        }

    }

    private int getErrorForFlow(int index){
        String[] map = getFlowKV(flows[index]);
        int actual = Integer.parseInt(map[COUNT]);
        int found = lookUp(map[IP]);

        int[] qValue = new int[2];
        qValue[COUNT] = found;
        qValue[INDEX] = index;
        q.offer(qValue);
        while(q.size() > 100){
            q.poll();
        }

        return Math.abs(actual - found);
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

        System.out.println("Average " + calAndPrintAverage());
        List<int[]> topList = new ArrayList<>();
        while(!q.isEmpty()){
            topList.add(q.poll());
        }
        Collections.reverse(topList);
        //lets print q
        System.out.println("Top 100 elements ");
        for(int[] item : topList){
            int index = item[INDEX];
            int found = item[COUNT];
            String[] map = getFlowKV(flows[index]);
            System.out.println("IP address: "+ map[IP] + " Found count:" + found + " Actual Count " + map[COUNT]);
        }
    }



    private int getIndex(String element, int index){
        int converted = Math.abs(element.hashCode());
        return (hashes[index] ^ converted)% counterSize;
    }
    private boolean isNegative(String element, int index){
        int converted = element.hashCode();
        //int msb = getMSB(converted);

        return converted < 1;
    }









    private int getMSB(int v){
        String string = Long.toBinaryString( Integer.toUnsignedLong(v) | 0x100000000L ).substring(1);
        int MSB = 1; //as it is signed
        return string.charAt(MSB) - '0';
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

            CounterSketch counterSketch = new CounterSketch(input,nCounters, counterSize);
            counterSketch.simulate();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
