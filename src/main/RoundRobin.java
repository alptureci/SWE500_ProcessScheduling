package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class RoundRobin extends Scheduler {

    private LinkedList<Process> waitingQueue = new LinkedList<>();

    public RoundRobin () {
        super("RoundRobin");
    }

    @Override
    public void schedule (ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);
        waitingQueue.clear();

        int qi = 0;
        for (int i = 0; i < quantaNum; i++) {
            currentRunStats.addQuanta();
            // first check the input list if any job should be added to waitingQueue
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                waitingQueue.addLast(q.get(qi++));
            }

            if (waitingQueue.size() != 0) {
                Process curProcess = waitingQueue.pollFirst();
                timeChart.add(curProcess);
                if (curProcess.lastRunTime < curProcess.arrivalTime) { // the process is never run before
                }
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.runningTime != curProcess.serviceTime)
                    waitingQueue.addLast(curProcess);
            }
            else
                timeChart.addIdlePeriod();
        }

        int i = quantaNum;
        while (!waitingQueue.isEmpty()) {
            Process curProcess = waitingQueue.pollFirst();
            if (curProcess.lastRunTime < curProcess.arrivalTime) continue;
            currentRunStats.addQuanta();
            timeChart.add(curProcess);
            curProcess.runningTime++;
            curProcess.lastRunTime = i;
            // if the process is not finished, add it back to waitingQueue
            if (curProcess.runningTime != curProcess.serviceTime)
                waitingQueue.addLast(curProcess);
            i++;
        }
        this.printCurrentRunStats(q);
    }
}
