package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class FirstComeFirstServed extends Scheduler {

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
            this.lastRunTime = this.arrivalTime - 1;
            this.runningTime = 0;
        }
        @Override
        public String toString() {
            return "RunningProcess{" +
                    "name=" + name +
                    ", arrivalTime=" + arrivalTime +
                    ", serviceTime=" + serviceTime +
                    ", lastRunTime=" + lastRunTime +
                    ", runningTime=" + runningTime +
                    '}';
        }

    }


    public FirstComeFirstServed () {
        super("FirstComeFirstServed");
    }

    @Override
    public void schedule (ArrayList<Process> q, int quantaNum) {
    	StatsPerRun stats = new StatsPerRun();
        LinkedList<RunningProcess> waitingQueue = new LinkedList<>();
        ArrayList<Character> timeChart = new ArrayList<>();

        int qi = 0;
        /*
        As it's non-preemptive algorithm, the process will not change until it's finished,
        we define a RunningProcess outside the loop to check its status (Running time) at different time slice
         */
        RunningProcess curProcess = null;
        for (int i = 0; i < quantaNum; i++) {
            stats.addQuanta();
            // first check the input list if any job should be added to waitingQueue
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                waitingQueue.addLast(new RunningProcess(q.get(qi++)));
            }

            /*
            If curProcess is finished (curProcess == null), and the waitingQueue is not empty, we poll a process from waitingQueue;

            Turn Around Time: Time Difference between completion time and arrival time.
            Turn Around Time = Completion Time - Arrival Time

            Waiting Time(W.T): Time Difference between turn around time and burst time.
            Waiting Time = Turn Around Time - Burst Time
             */
            if (curProcess == null && waitingQueue.size() != 0) {
                curProcess = waitingQueue.pollFirst();
                timeChart.add(curProcess.name);
                stats.addProcess();
                stats.addResponseTime(i - curProcess.arrivalTime);
                stats.addWaitingTime(i - curProcess.lastRunTime - 1);
                stats.addTurnaroundTime(i - curProcess.lastRunTime);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if curProcess is finished, reset it to null;
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            else if (curProcess != null) { // if curProcess is not finished
                timeChart.add(curProcess.name);
                stats.addWaitingTime(i - curProcess.lastRunTime - 1);
                stats.addTurnaroundTime(i - curProcess.lastRunTime);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if curProcess is finished, reset it to null;
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            else // curProcess is finished and no process in waitingQueue
                timeChart.add('-');
        }
        int i = quantaNum;
        /*
        As after quantaNum, the not running process will be dropped anyway, we only need to
        check if curProcess is finished or not.
         */
        while (curProcess != null) {
//        while (!waitingQueue.isEmpty()) {
//            RunningProcess curProcess = waitingQueue.pollFirst();
//            if (curProcess.lastRunTime < curProcess.arrivalTime) continue;
            timeChart.add(curProcess.name);
            stats.addQuanta();
            stats.addWaitingTime(i - curProcess.lastRunTime - 1);
            stats.addTurnaroundTime(i-curProcess.lastRunTime);
            curProcess.runningTime++;
            curProcess.lastRunTime = i;
            // if curProcess is finished, reset it to null;
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
        ArrayList<Process> processes = ProcessGenerator.generate();
        for (Process p : processes)
            System.out.println(p);
//        
        FirstComeFirstServed test = new FirstComeFirstServed();
//        test.schedule(processes, 20);
        test.schedule(processes, 50);
        test.printAvgStats();

    }
}
