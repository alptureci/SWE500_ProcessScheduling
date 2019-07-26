package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class RoundRobin extends Scheduler {

//    private final String NAME = "RoundRobin";

    private class RunningProcess {
        private char name;
        private int arrivalTime;
        private int serviceTime;
        private int lastRunTime; // last running quanta
        private int runningTime; // how long the process has been running

        public RunningProcess(Process p) {
            this.name = p.getName();
            this.arrivalTime = p.getArrivalTime();
            this.serviceTime = p.getServiceTime();
//            this.startTime = -1;
            this.lastRunTime = this.arrivalTime - 1;
            this.runningTime = 0;
        }
    }

    public RoundRobin () {
        super("RoundRobin");

    }

    @Override
    public void schedule (ArrayList<Process> q, int quantaNum) {
        StatsPerRun stats = new StatsPerRun();

        LinkedList<RunningProcess> waitingQueue = new LinkedList<>();
        ArrayList<Character> timeChart = new ArrayList<>();

        int qi = 0;
        for (int i = 0; i < quantaNum; i++) {
            stats.addQuanta();
            // first check the input list if any job should be added to waitingQueue
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                waitingQueue.addLast(new RunningProcess(q.get(qi++)));
            }

            if (waitingQueue.size() != 0) {
                RunningProcess curProcess = waitingQueue.pollFirst();
                timeChart.add(curProcess.name);
                if (curProcess.lastRunTime < curProcess.arrivalTime) { // the process is never run before
                    stats.addProcess();
                    stats.addResponseTime(i-curProcess.arrivalTime);
                }
                stats.addWaitingTime(i - curProcess.lastRunTime - 1);
                stats.addTurnaroundTime(i-curProcess.lastRunTime);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.runningTime != curProcess.serviceTime)
                    waitingQueue.addLast(curProcess);
            }
            else
                timeChart.add('-');
        }

        int i = quantaNum;
        while (!waitingQueue.isEmpty()) {
            RunningProcess curProcess = waitingQueue.pollFirst();
            if (curProcess.lastRunTime < curProcess.arrivalTime) continue;
            timeChart.add(curProcess.name);
            stats.addQuanta();
            stats.addWaitingTime(i - curProcess.lastRunTime - 1);
            stats.addTurnaroundTime(i-curProcess.lastRunTime);
            curProcess.runningTime++;
            curProcess.lastRunTime = i;
            // if the process is not finished, add it back to waitingQueue
            if (curProcess.runningTime != curProcess.serviceTime)
                waitingQueue.addLast(curProcess);
            i++;
        }
        System.out.println(this.scheduler);
        printTimeChart(timeChart);
        stats.printRoundAvgStats();
        storeOneRoundStats(stats);


    }
    public static void main (String[] args) {
        ArrayList<Process> processes = ProcessGenerator.generate();
        for (Process p : processes)
            System.out.println(p);
        RoundRobin test = new RoundRobin();
        test.schedule(processes, 20);
        test.schedule(processes, 50);
        test.printAvgStats();


    }

}
