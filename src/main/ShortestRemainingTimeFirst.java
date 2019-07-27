package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class ShortestRemainingTimeFirst extends Scheduler {

    private class RunningProcess {
        private char name;
        private int arrivalTime;
        private int serviceTime;
//        private int priority;
        private int lastRunTime; // last running quanta
//        private int runningTime; // how long the process has been running
//        private int waitcount;
//        private int expectedEndTime;
        private int remainingTime;

        public RunningProcess(Process p) {
            this.name = p.getName();
            this.arrivalTime = p.getArrivalTime();
            this.serviceTime = p.getServiceTime();
//            this.priority = p.getPriority();
//            this.startTime = -1;
            this.lastRunTime = this.arrivalTime - 1;
            this.remainingTime = p.getServiceTime(); // initially the remainingTime is the serviceTime;
//            this.runningTime = 0;
//            this.waitcount = 0;
//            this.expectedEndTime = p.getExpectedEndTime();
        }

        @Override
        public String toString() {
            return "RunningProcess{" +
                    "name=" + name +
                    ", arrivalTime=" + arrivalTime +
                    ", serviceTime=" + serviceTime +
//                    ", priority=" + priority +
                    ", lastRunTime=" + lastRunTime +
//                    ", runningTime=" + runningTime +
                    ", remainingTime=" + remainingTime +
//                    ", expectedEndTime=" + expectedEndTime +
                    '}';
        }
    }

    private static final int SHORTEST_REMAINING_TIME = 0;
//    private ArrayList<LinkedList<RunningProcess>> waitingQueues;
    /*
    In this algorithm, it will be easy to use PriorityQueue for the waitingQueue;
    as every time we poll a process from waitingQueue, it will be the one with shortest remaining time;
     */
    private PriorityQueue<RunningProcess> waitingQueues;

    public ShortestRemainingTimeFirst() {
        super("Shortest Remaining Time First");
        waitingQueues = new PriorityQueue<>(new Comparator<RunningProcess>() {
            /*
            Define a comparator here so PriorityQueue knows how to compare two processes;
             */
            @Override
            public int compare(RunningProcess p1, RunningProcess p2) {

                if (p1.remainingTime < p2.remainingTime) return -1;
                else if (p1.remainingTime > p2.remainingTime) return 1;
                else { // if two processes have the same remainingTime, we use FCFS
                    if (p1.arrivalTime < p2.arrivalTime) return -1;
                    else if (p1.arrivalTime > p2.arrivalTime) return 1;
                    else
                        return 0;
                }
            }
        });
//        this.waitingQueues = new ArrayList<>(SHORTEST_REMAINING_TIME);
//        for (int i = 0; i < SHORTEST_REMAINING_TIME; i++)
//            waitingQueues.add(new LinkedList<>());
    }

    // check if the waitingQueues are empty or not;  // if it's empty, return true;   // otherwise, return false;
    private boolean isWaitingQueueEmpty() {
//        for (int i = 0; i < SHORTEST_REMAINING_TIME; i++){
//            if (waitingQueues.get(i).size() != 0)
//                return false;
//        }
//        return true;
        return waitingQueues.size() == 0;
    }

    private void printWaitingQueue() {
//        for (int i = 0; i < SHORTEST_REMAINING_TIME; i++){
//            System.out.println(waitingQueues.get(i));
//        }
    }

    // 1. choose the next running process;
    // 2. increase the waitcount by 1 for non-selected processes;
    private RunningProcess nextProcessToRun () {
//        RunningProcess curProcess = null;
//
//        for (int wi = 0; wi < SHORTEST_REMAINING_TIME; wi++) {
//            int size = waitingQueues.get(wi).size();
//            for (int j = 0; j < size; j++) {
//                RunningProcess tmp = waitingQueues.get(wi).pollFirst();
//                if (curProcess == null) {
//                    curProcess = tmp;
//                    continue;
//                }
//                else if (tmp.expectedEndTime > 1 ) {
//                    tmp.expectedEndTime = tmp.expectedEndTime - 1;
//                }
//                waitingQueues.get(tmp.expectedEndTime-1).addLast(tmp);
//            }
//        }
//        return curProcess;
        return waitingQueues.poll();
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        Scheduler.StatsPerRun stats = new Scheduler.StatsPerRun();
        ArrayList<Character> timeChart = new ArrayList<>();

        int qi = 0; // track which process has been added into

        RunningProcess curProcess = null;

        int i = 0;

        while (i < quantaNum || !isWaitingQueueEmpty()) {

            // if a job has arrived, add it to waiting queue
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process tmp = q.get(qi);
//                waitingQueues.get(tmp.getExpectedEndTime()-1).addLast(new RunningProcess(tmp));
                waitingQueues.add(new RunningProcess(tmp));
                qi++;
            }
//            printWaitingQueue();

            curProcess = nextProcessToRun();

            if (curProcess != null) {
//                System.out.println(curProcess);
                if (curProcess.remainingTime == curProcess.serviceTime && i >= quantaNum) {
                    curProcess = null;
                    continue; // the process never runs before quantaNum, drop it;
                }
                else if (curProcess.remainingTime == curProcess.serviceTime) { // the process is never run before quantaNum
                    stats.addProcess();
                    stats.addResponseTime(i-curProcess.arrivalTime);
                }
                stats.addQuanta();
                timeChart.add(curProcess.name);
                stats.addWaitingTime(i - curProcess.lastRunTime - 1);
                stats.addTurnaroundTime(i-curProcess.lastRunTime);
//                curProcess.expectedEndTime++;
                curProcess.remainingTime--;
                curProcess.lastRunTime = i;
//                curProcess.waitcount = 0;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.remainingTime != 0)
                    waitingQueues.add(curProcess);
//                    waitingQueues.get(curProcess.expectedEndTime-1).addLast(curProcess);
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
//        ArrayList<Process> processes = ProcessGenerator.generate();
        ArrayList<Process> processes = ProcessGenerator.generateSetOfProcesses(20);
        for (Process p : processes)
            System.out.println(p);
        ShortestRemainingTimeFirst test = new ShortestRemainingTimeFirst();
        test.schedule(processes, 50);
        test.printAvgStats();
    }

}