package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ProcessGenerator {

    public static ArrayList<Process> generateDense() {
        ArrayList<Process> processes = new ArrayList<>();

        Random random = new Random();
//        random.setSeed(0);

        int i = 0;
        while (i < 25) {
            char name = (char) ('A' + i);
//            int arrivalTime = random.nextInt(Integer.MAX_VALUE) % 100;
//            int serviceTime = random.nextInt(Integer.MAX_VALUE) % 15+1;
////            if (serviceTime == 0) serviceTime += 1;
//            int priority = random.nextInt(Integer.MAX_VALUE) % 4 + 1;

            int arrivalTime = random.nextInt(150);
            int serviceTime = random.nextInt(10) +1;
//            if (serviceTime == 0) serviceTime += 1;
            int priority = random.nextInt(4) + 1;

            processes.add(new Process(name, arrivalTime, serviceTime, priority));
            i++;
        }

        Collections.sort(processes);

//        if ()
        if (processes.get(0).getArrivalTime() > 2)
            processes.get(0).setArrivalTime(0);

        for (int j = 0; j < processes.size()-1; j++) {
            if (processes.get(j).getExpectedEndTime() < processes.get(j+1).getArrivalTime()-2) {
                processes.get(j).setServiceTime(processes.get(j+1).getArrivalTime() - 2 - processes.get(j).getArrivalTime());
            }
        }


        for (int j = 0; j < processes.size(); j++)
            processes.get(j).setName((char)('A'+j));

        return processes;
    }

    public static ArrayList<Process> generate() {
        ArrayList<Process> processes = new ArrayList<>();

        Random random = new Random();
//        random.setSeed(54);

        int i = 0;
        while (i < 10) {
            char name = (char) ('A' + i);
//            int arrivalTime = random.nextInt(Integer.MAX_VALUE) % 100;
//            int serviceTime = random.nextInt(Integer.MAX_VALUE) % 15+1;
////            if (serviceTime == 0) serviceTime += 1;
//            int priority = random.nextInt(Integer.MAX_VALUE) % 4 + 1;

            int arrivalTime = random.nextInt(100);
            int serviceTime = random.nextInt(10) +1;
//            if (serviceTime == 0) serviceTime += 1;
            int priority = random.nextInt(4) + 1;

            processes.add(new Process(name, arrivalTime, serviceTime, priority));
            i++;
        }

        Collections.sort(processes);

        if (processes.get(0).getArrivalTime() > 2) {
            processes.add(0, new Process((char)('A' + i), random.nextInt(3),
                    random.nextInt(10)+1, random.nextInt(4)+1));

        }

        int sum = processes.get(0).getArrivalTime();

        for (int j = 0; j < processes.size()-1; j++) {
            sum = Math.max(sum, processes.get(j).getArrivalTime()) + processes.get(j).getServiceTime();
            if (sum >= processes.get(j+1).getArrivalTime()) continue;
            processes.add(j+1, new Process((char)('A'), random.nextInt(3)+sum,
                    random.nextInt(10)+1, random.nextInt(4)+1));
//            i++;

        }

        sum = Math.max(sum, processes.get(processes.size()-1).getArrivalTime()) +
                processes.get(processes.size()-1).getServiceTime();

        while (sum < 98) {
            processes.add(new Process('A', random.nextInt(3)+sum,
                    random.nextInt(10)+1, random.nextInt(4)+1));
            sum = Math.max(sum, processes.get(processes.size()-1).getArrivalTime()) +
                    processes.get(processes.size()-1).getServiceTime();
        }

        for (int j = 0; j < processes.size(); j++)
            processes.get(j).setName((char)('A'+j));

        return processes;
    }

    public static void main (String[] args) {
        ArrayList<Process> processes = ProcessGenerator.generate();
        for (Process p : processes)
            System.out.println(p);

    }


}
