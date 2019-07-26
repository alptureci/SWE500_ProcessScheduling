package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class HighestPriorityFirstPreemptive extends Scheduler {

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

    private static final int NUM_PRIORITY = 4;
    private ArrayList<LinkedList<RunningProcess>> waitingQueues;

    public HighestPriorityFirstPreemptive() {
        super("Highest Priority First-Preemptive");
        this.waitingQueues = new ArrayList<>(NUM_PRIORITY);
        for (int i = 0; i < NUM_PRIORITY; i++)
            waitingQueues.add(new LinkedList<>());
    }

    private RunningProcess nextProcessToRun () {
        RunningProcess curProcess = null;
        for (int wi = 0; wi < NUM_PRIORITY; wi++) {
            if (waitingQueues.get(wi).size() == 0) continue;
            curProcess = waitingQueues.get(wi).pollFirst();
            break;
        }
        return curProcess;
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        StatsPerRun stats = new StatsPerRun();
        ArrayList<Character> timeChart = new ArrayList<>();

        int qi = 0; // track which process has been added into
        RunningProcess curProcess = null;

        int i = 0;
        while (i < quantaNum || curProcess != null) {
//            System.out.println("before quantaNum");

            // first check the input list if any job should be added to waitingQueues
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process cur = q.get(qi);
                waitingQueues.get(cur.getPriority()-1).addLast(new RunningProcess(cur));
                qi++;

            }

            curProcess = nextProcessToRun();

            if (curProcess != null) {
//                RunningProcess curProcess = waitingQueue.pollFirst();
//                System.out.println(curProcess);

                if (curProcess.lastRunTime < curProcess.arrivalTime && i >= quantaNum) {
                    // if the process is never run before "quantaNum", drop it;
                    continue;
                }
                else if (curProcess.lastRunTime < curProcess.arrivalTime) { // the process is never run before
                    stats.addProcess();
                    stats.addResponseTime(i-curProcess.arrivalTime);
                }
                stats.addQuanta();
                timeChart.add(curProcess.name);
                stats.addWaitingTime(i - curProcess.lastRunTime - 1);
                stats.addTurnaroundTime(i-curProcess.lastRunTime);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.runningTime != curProcess.serviceTime)
                    waitingQueues.get(curProcess.priority-1).addLast(curProcess);
            }
            else if (i < quantaNum) {
                timeChart.add('-');
                stats.addQuanta();
            }
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
        HighestPriorityFirstPreemptive test = new HighestPriorityFirstPreemptive();
        test.schedule(processes, 100);
//        test.schedule(processes, 50);
        test.printAvgStats();


    }
}
