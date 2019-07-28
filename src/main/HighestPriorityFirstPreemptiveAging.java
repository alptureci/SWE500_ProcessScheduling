package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class HighestPriorityFirstPreemptiveAging extends Scheduler {

    public HighestPriorityFirstPreemptiveAging() {
        super("Highest Priority First-Preemptive, Aging");
        this.waitingQueues = new ArrayList<>(NUM_PRIORITY);
        for (int i = 0; i < NUM_PRIORITY; i++)
            waitingQueues.add(new LinkedList<>());
    }

    // check if the waitingQueues are empty or not;
    // if it's empty, return true;
    // otherwise, return false;
    private boolean isWaitingQueueEmpty() {
        for (int i = 0; i < NUM_PRIORITY; i++){
            if (waitingQueues.get(i).size() != 0)
                return false;
        }
        return true;
    }

    private void printWaitingQueue() {
        for (int i = 0; i < NUM_PRIORITY; i++){
            System.out.println(waitingQueues.get(i));
        }
    }

    // 1. choose the next running process;
    // 2. increase the waitcount by 1 for non-selected processes;
    private Process nextProcessToRun () {
        Process curProcess = null;

        for (int wi = 0; wi < NUM_PRIORITY; wi++) {
            int size = waitingQueues.get(wi).size();
            for (int j = 0; j < size; j++) {
                Process tmp = waitingQueues.get(wi).pollFirst();
                if (curProcess == null) {
                    curProcess = tmp;
                    continue;
                }
                if (tmp.priority > 1 && tmp.waitcount == 5 ) {
                    tmp.priority = tmp.priority - 1;
                    tmp.waitcount = 1;
                }
                else {
                    tmp.waitcount++;
                }
                waitingQueues.get(tmp.priority-1).addLast(tmp);
            }
        }
        return curProcess;
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);

        int qi = 0; // track which process has been added into
        Process curProcess = null;
        int i = 0;

        while (i < quantaNum || !isWaitingQueueEmpty()) {
//            System.out.format("quanta %d: \n", i);

            // if a job has arrived, add it to waiting queue
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process tmp = q.get(qi);
                waitingQueues.get(tmp.getPriority()-1).addLast(tmp);
                qi++;
            }
//            printWaitingQueue();

            curProcess = nextProcessToRun();

            if (curProcess != null) {
//                System.out.println(curProcess);
                if (curProcess.runningTime == 0 && i >= quantaNum) {
                    curProcess = null;
                    continue; // the process never runs before quantaNum, drop it;
                }
                else if (curProcess.runningTime == 0) { // the process is never run before quantaNum

                }
                currentRunStats.addQuanta();
                timeChart.add(curProcess.name);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                curProcess.waitcount = 0;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.runningTime != curProcess.serviceTime)
                    waitingQueues.get(curProcess.priority-1).addLast(curProcess);
            }
            else if (i < quantaNum) {
                currentRunStats.addQuanta();
                timeChart.addIdlePeriod();
            }
            i++;
        }
        this.printCurrentRunStats(q);
    }
}
