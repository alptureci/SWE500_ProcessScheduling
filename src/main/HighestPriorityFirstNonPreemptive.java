package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class HighestPriorityFirstNonPreemptive extends Scheduler {

    public HighestPriorityFirstNonPreemptive() {
        super("Highest Priority First-Non Preemptive");
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);

        for (int i = 0; i < NUM_PRIORITY; i++)
            waitingQueues.add(new LinkedList<>());

        int qi = 0; // track which process has been added into

        Process curProcess = null;

        for (int i = 0; i < quantaNum; i++) {
            currentRunStats.addQuanta();
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process tmp = q.get(qi);
                waitingQueues.get(tmp.getPriority()-1).addLast(tmp);
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
                }
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            else
                timeChart.addIdlePeriod();
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
            currentRunStats.addQuanta();
            currentRunStats.addWaitingTime(i - curProcess.lastRunTime - 1);
            currentRunStats.addTurnaroundTime(i-curProcess.lastRunTime);
            curProcess.runningTime++;
            curProcess.lastRunTime = i;
            // if the process is not finished, add it back to waitingQueue
            if (curProcess.runningTime == curProcess.serviceTime)
                curProcess = null;
            i++;
        }

        System.out.println(this.algorithmName);
        timeChart.printTimeChart();
        currentRunStats.printRoundAvgStats(q);
    }
}
