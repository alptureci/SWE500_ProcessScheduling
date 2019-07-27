package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class HighestPriorityFirstNonPreemptive extends Scheduler {
    private static final int NUM_PRIORITY = 4;

    private class RunningProcess {
        private char name;
        private int arrivalTime;
        private int serviceTime;
        private int priority;
        private int lastRunTime; // last running quanta
        private int runningTime; // how long the process has been running

        public RunningProcess(Process p) {
            this.name = p.getName();
            this.arrivalTime = p.getArrivalTime();
            this.serviceTime = p.getServiceTime();
            this.priority = p.getPriority();
//            this.startTime = -1;
            this.lastRunTime = this.arrivalTime - 1;
            this.runningTime = 0;
        }

        @Override
        public String toString() {
            return "RunningProcess{" +
                    "name=" + name +
                    ", arrivalTime=" + arrivalTime +
                    ", serviceTime=" + serviceTime +
                    ", priority=" + priority +
                    ", lastRunTime=" + lastRunTime +
                    ", runningTime=" + runningTime +
                    '}';
        }
    }

    public HighestPriorityFirstNonPreemptive() {
        super("Highest Priority First-Non Preemptive");
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        Scheduler.StatsPerRun stats = new Scheduler.StatsPerRun();
        ArrayList<Character> timeChart = new ArrayList<>();

        ArrayList<LinkedList<RunningProcess>> waitingQueues = new ArrayList<>(NUM_PRIORITY);

        for (int i = 0; i < NUM_PRIORITY; i++)
            waitingQueues.add(new LinkedList<>());

        int qi = 0; // track which process has been added into

        RunningProcess curProcess = null;

        for (int i = 0; i < quantaNum; i++) {
            stats.addQuanta();
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process tmp = q.get(qi);
                waitingQueues.get(tmp.getPriority()-1).addLast(new RunningProcess(tmp));
                qi++;

            }

            if (curProcess == null) {
                for (int wi = 0; wi < NUM_PRIORITY; wi++) {
                    if (waitingQueues.get(wi).size() == 0) continue;
                    curProcess = waitingQueues.get(wi).pollFirst();
                    break;
                }
            }

            if (curProcess != null) {
//                System.out.println(curProcess);
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
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            else
                timeChart.add('-');
        }

//        RunningProcess curProcess = null;
//        for (int wi = 0; wi < NUM_PRIORITY; wi++) {
//            if (waitingQueues.get(wi).size() == 0) continue;
//            curProcess = waitingQueues.get(wi).pollFirst();
//            break;
//        }
        int i = quantaNum;
        while (curProcess != null) {
//            System.out.println(curProcess);
//            RunningProcess curProcess = waitingQueue.pollFirst();
            if (curProcess.lastRunTime < curProcess.arrivalTime) {
                curProcess = null;
                continue;
            }
            timeChart.add(curProcess.name);
            stats.addQuanta();
            stats.addWaitingTime(i - curProcess.lastRunTime - 1);
            stats.addTurnaroundTime(i-curProcess.lastRunTime);
            curProcess.runningTime++;
            curProcess.lastRunTime = i;
            // if the process is not finished, add it back to waitingQueue
            if (curProcess.runningTime == curProcess.serviceTime)
                curProcess = null;
            i++;
        }

        System.out.println(this.scheduler);
        printTimeChart(timeChart);
        stats.printRoundAvgStats();
        storeOneRoundStats(stats);

    }

    public static void main (String[] args) {
        ArrayList<Process> processes = ProcessGenerator.generateSetOfProcesses(20);
        for (Process p : processes)
            System.out.println(p);
        HighestPriorityFirstNonPreemptive test = new HighestPriorityFirstNonPreemptive();
        test.schedule(processes, 50);
//        test.schedule(processes, 50);
        test.printAvgStats();


    }
}
