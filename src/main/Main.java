package main;

import java.util.ArrayList;

public class Main {
    private static final int RUNS = 5;
    private static final int QUANTA_NUM = 150;

    public static void main (String[] args) {
        Scheduler fcfs = new FirstComeFirstServed();
        Scheduler sjf = new ShortestJobFirst();
        Scheduler srt = new ShortestRemainingTimeFirst();
        Scheduler rr = new RoundRobin();
        Scheduler hpfnp = new HighestPriorityFirstNonPreemptive();
        Scheduler hpfp = new HighestPriorityFirstPreemptive();
        Scheduler hpfnpa = new HighestPriorityFirstNonPreemptiveAging();
        Scheduler hpfpa = new HighestPriorityFirstPreemptiveAging();

        for (int i = 0; i < RUNS; i++) {
            System.out.println("=================================");
            System.out.println("    Algorithm Test - Round " + i + ":   ");
            System.out.println("=================================");
            ArrayList<Process> processes = ProcessGenerator.generateDense();
//            ArrayList<Process> processes = ProcessGenerator.generate();

            for (Process p : processes)
                System.out.println(p);
            System.out.println();
            fcfs.schedule(processes, QUANTA_NUM);
            sjf.schedule(processes, QUANTA_NUM);
            srt.schedule(processes, QUANTA_NUM);
            rr.schedule(processes, QUANTA_NUM);
            hpfnp.schedule(processes, QUANTA_NUM);
            hpfp.schedule(processes, QUANTA_NUM);
            System.out.println("-------------------------------");
            System.out.println("    Extra Credit Algorithms");
            System.out.println("-------------------------------");
            hpfnpa.schedule(processes, QUANTA_NUM);
            hpfpa.schedule(processes, QUANTA_NUM);
            System.out.println();
        }
        System.out.println();
        System.out.println("================================");
        System.out.println("Final Average Statistics Report:");
        System.out.println("================================");

        fcfs.printAvgStats();
        sjf.printAvgStats();
        srt.printAvgStats();
        rr.printAvgStats();
        hpfnp.printAvgStats();
        hpfp.printAvgStats();
        System.out.println("-------------------------------");
        System.out.println("    Extra Credit Algorithms");
        System.out.println("-------------------------------");
        hpfnpa.printAvgStats();
        hpfpa.printAvgStats();

    }
}