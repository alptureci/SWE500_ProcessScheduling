package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ShortestRemainingTimeFirst extends Scheduler {

//    private ArrayList<LinkedList<RunningProcess>> waitingQueues;
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
    private Process nextProcessToRun () {
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
        super.schedule(q, quantaNum);
        int qi = 0; // track which process has been added into
        Process curProcess = null;
        int i = 0;

        while (i < quantaNum || !isWaitingQueueEmpty()) {

            // if a job has arrived, add it to waiting queue
            // also find out which waitingQueues should be
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i) {
                Process tmp = q.get(qi);
//                waitingQueues.get(tmp.getExpectedEndTime()-1).addLast(new RunningProcess(tmp));
                waitingQueues.add(tmp);
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
                }
                currentRunStats.addQuanta();
                timeChart.add(curProcess);
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
                timeChart.addIdlePeriod();
                currentRunStats.addQuanta();
            }
            i++;
        }
        this.printCurrentRunStats(q);
    }
}