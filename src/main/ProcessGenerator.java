package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ProcessGenerator {
    static long seed = 3L;

    public static ArrayList<Process> generateSetOfProcesses(int numProcesses) {
        ArrayList<Process> processes = new ArrayList<>();

        Random random = new Random();
        //random.setSeed(seed);

        for (int i = 0; i < numProcesses; i++ ) {
            String name = Character.toString(('A'+ i % 26)) + ((char)('0' + i/26));
            int colorsSize = Process.processColors.size();

            int arrivalTime = random.nextInt(150);
            int serviceTime = random.nextInt(9) +1;
            int priority = random.nextInt(4) + 1;
            int color = random.nextInt(colorsSize);
            processes.add(new Process(name, arrivalTime, serviceTime, priority, color));

        }

        Collections.sort(processes);

        // Set the arrival Time of the first one to 0.
        if (processes.get(0).getArrivalTime() > 2) {
            processes.get(0).setArrivalTime(1);
        }

//        for (int j = 0; j < processes.size()-1; j++) {
//            if (processes.get(j).getExpectedEndTime() < processes.get(j+1).getArrivalTime()-2) {
//                processes.get(j).setServiceTime(processes.get(j+1).getArrivalTime() - 2 - processes.get(j).getArrivalTime());
//            }
//        }

//        for (int j = 0; j < processes.size(); j++)
//            processes.get(j).setName(Character.toString((char)('A'+ j % 26)) + ((char)(j/26)));

        return processes;
    }

//    public static ArrayList<Process> generate() {
//        ArrayList<Process> processes = new ArrayList<>();
//
//        Random random = new Random();
//        random.setSeed(seed);
//
//        int i = 0;
//        while (i < 100) {
//            char name = (char) ('A' + i);
//
//            int arrivalTime = random.nextInt(150);
//            int serviceTime = random.nextInt(10) +1;
//            int priority = random.nextInt(4) + 1;
//
//            processes.add(new Process(name, arrivalTime, serviceTime, priority));
//            i++;
//        }
//
//        Collections.sort(processes);
//
//        if (processes.get(0).getArrivalTime() > 2) {
//            processes.add(0, new Process((char)('A' + i), random.nextInt(3),
//                    random.nextInt(10)+1, random.nextInt(4)+1));
//
//        }
//
//        int sum = processes.get(0).getArrivalTime();
//
//        for (int j = 0; j < processes.size()-1; j++) {
//            sum = Math.max(sum, processes.get(j).getArrivalTime()) + processes.get(j).getServiceTime();
//            if (sum >= processes.get(j+1).getArrivalTime()) continue;
//            processes.add(j+1, new Process((char)('A'), random.nextInt(3)+sum,
//                    random.nextInt(10)+1, random.nextInt(4)+1));
////            i++;
//
//        }
//
//        sum = Math.max(sum, processes.get(processes.size()-1).getArrivalTime()) +
//                processes.get(processes.size()-1).getServiceTime();
//
//        while (sum < 98) {
//            processes.add(new Process('A', random.nextInt(3)+sum,
//                    random.nextInt(10)+1, random.nextInt(4)+1));
//            sum = Math.max(sum, processes.get(processes.size()-1).getArrivalTime()) +
//                    processes.get(processes.size()-1).getServiceTime();
//        }
//
//        for (int j = 0; j < processes.size(); j++)
//            processes.get(j).setName((char)('A'+j));
//
//        return processes;
//    }
//
//    public static void main (String[] args) {
//        ArrayList<Process> processes = ProcessGenerator.generate();
//        for (Process p : processes)
//            System.out.println(p);
//
//    }


}
