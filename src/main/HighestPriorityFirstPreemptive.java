package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class HighestPriorityFirstPreemptive extends Scheduler {

    public HighestPriorityFirstPreemptive() {
        super("Highest Priority First-Preemptive");
    }

    private Process nextProcessToRun () {
        Process curProcess = null;
        for (int wi = 0; wi < NUM_PRIORITY; wi++) {
            if (waitingQueues.get(wi).size() == 0) continue;
            curProcess = waitingQueues.get(wi).pollFirst();
            break;
        }
        return curProcess;
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);

        int qi = 0; // track which process has been added into
        Process curProcess = null;

        int i = 0;
        while (i < quantaNum || curProcess != null) {
//            System.out.println("before quantaNum");

            // first check the input list if any job should be added to waitingQueues
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process cur = q.get(qi);
                waitingQueues.get(cur.getPriority()-1).addLast(cur);
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
                }
                currentRunStats.addQuanta();
                timeChart.add(curProcess.name);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.runningTime != curProcess.serviceTime)
                    waitingQueues.get(curProcess.priority-1).addLast(curProcess);
            }
            else if (i < quantaNum) {
                this.timeChart.addIdlePeriod();
                currentRunStats.addQuanta();
            }
            i++;
        }
        this.printCurrentRunStats(q);
    }
}
