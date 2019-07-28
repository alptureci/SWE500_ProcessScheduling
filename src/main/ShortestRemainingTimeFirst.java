package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ShortestRemainingTimeFirst extends Scheduler {

    /*
    In this algorithm, it will be easy to use PriorityQueue for the waitingQueue;
    as every time we poll a process from waitingQueue, it will be the one with shortest remaining time;
     */
    private PriorityQueue<Process> waitingQueue;

    public ShortestRemainingTimeFirst() {
        super("Shortest Remaining Time First");
        waitingQueue = new PriorityQueue<>(new Comparator<Process>() {
            /*
            Define a comparator here so PriorityQueue knows how to compare two processes;
             */
            @Override
            public int compare(Process p1, Process p2) {

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

    }

    // check if the waitingQueues are empty or not;  // if it's empty, return true;   // otherwise, return false;
    private boolean isWaitingQueueEmpty() {
        return waitingQueue.size() == 0;
    }

    // 1. choose the next running process;
    // 2. increase the waitcount by 1 for non-selected processes;
    private Process nextProcessToRun () {
        return waitingQueue.poll();
    }

    @Override
    public void schedule(ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);
        int qi = 0; // track which process has been added into
        Process curProcess = null;
        int i = 0;

        while (i < quantaNum || !isWaitingQueueEmpty()) {

            // if a job has arrived, add it to waiting queue
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process tmp = q.get(qi);
                waitingQueue.add(tmp);
                qi++;
            }

            curProcess = nextProcessToRun();

            if (curProcess != null) {
//                System.out.println(curProcess);
                if (curProcess.remainingTime == curProcess.serviceTime && i >= quantaNum) {
                    curProcess = null;
                    continue; // the process never runs before quantaNum, drop it;
                }
                else if (curProcess.remainingTime == curProcess.serviceTime) { // the process is never run before quantaNum
                }
                currentRunStats.addQuanta();
                timeChart.add(curProcess);
                curProcess.remainingTime--;
                curProcess.lastRunTime = i;
                // if the process is not finished, add it back to waitingQueue
                if (curProcess.remainingTime != 0)
                    waitingQueue.add(curProcess);
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