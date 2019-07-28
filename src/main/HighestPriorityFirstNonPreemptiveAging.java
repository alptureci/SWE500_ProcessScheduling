package main;


import java.util.ArrayList;
import java.util.LinkedList;

public class HighestPriorityFirstNonPreemptiveAging extends Scheduler {

    private ArrayList<LinkedList<Process>> waitingQueues = new ArrayList<>(NUM_PRIORITY);



    public HighestPriorityFirstNonPreemptiveAging() {
        super("Highest Priority First-Non Preemptive, Aging");
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

    // print out waiting queues, for debugging purpose only
    private void printWaitingQueue() {
        for (int i = 0; i < NUM_PRIORITY; i++){
            System.out.println(waitingQueues.get(i));
        }
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);
        waitingQueues.clear();
        for (int i = 0; i < NUM_PRIORITY; i++)
            waitingQueues.add(new LinkedList<>());

        int qi = 0; // track which process has been added into
        Process curProcess = null;

        int i = 0;

        while (i < quantaNum || !isWaitingQueueEmpty()) {
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process tmp = q.get(qi);
                waitingQueues.get(tmp.getPriority()-1).addLast(tmp);
                qi++;
            }

            // 1. if curProcess is not finished, finish curProcess; if finished, select the next process
            // 2. Increase the waitcount for non-selected process;
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

            if (curProcess != null) {
//                System.out.println(curProcess);
                if (curProcess.runningTime == 0 && i >= quantaNum) {
                    curProcess = null;
                    continue; // the process never runs before quantaNum, drop it;
                }
                else if (curProcess.runningTime == 0) { // the process is never run before quantaNum
                }
                currentRunStats.addQuanta();
                timeChart.add(curProcess);
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                curProcess.waitcount = 0;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            else if (i < quantaNum) {
                timeChart.addIdlePeriod();
                currentRunStats.addQuanta();
            }
            i++;
        }
        this.printCurrentRunStats(q);
    }
}
