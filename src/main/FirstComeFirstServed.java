package main;

import java.util.ArrayList;
import java.util.LinkedList;


public class FirstComeFirstServed extends Scheduler {

    public FirstComeFirstServed () {
        super("FirstComeFirstServed");
    }

    @Override
    public void schedule (ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);
        int qi = 0;
        /*
        As it's non-preemptive algorithm, the process will not change until it's finished,
        we define a RunningProcess outside the loop to check its status (Running time) at different time slice
         */
        Process curProcess = null;
        for (int i = 0; i < quantaNum; i++) {
            // first check the input list if any job should be added to waitingQueue
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                waitingQueue.addLast(q.get(qi++));
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
                this.timeChart.add(curProcess.name);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if curProcess is finished, reset it to null;
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            else if (curProcess != null) { // if curProcess is not finished
                timeChart.add(curProcess.name);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if curProcess is finished, reset it to null;
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            else // curProcess is finished and no process in waitingQueue
                timeChart.addIdlePeriod();
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
            currentRunStats.addQuanta();
            curProcess.runningTime++;
            curProcess.lastRunTime = i;
            // if curProcess is finished, reset it to null;
            if (curProcess.runningTime == curProcess.serviceTime)
                curProcess = null;
            i++;
        }
        this.printCurrentRunStats(q);
    }
}
