package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


public class ShortestJobFirst extends Scheduler{
	
	public ShortestJobFirst () {
        super("ShortestJobFirst");

    }

	@Override
    public void schedule (ArrayList<Process> q, int quantaNum) {
        super.schedule(q, quantaNum);

//        LinkedList<RunningProcess> waitingQueue = new LinkedList<>();

        /* use PriorityQueue here so it will poll among the ready processes
        the one with the shortest service time
         */
        PriorityQueue<Process> waitingQueue = new PriorityQueue<>(
                new Comparator<Process>() {
                    @Override
                    public int compare(Process p1, Process p2) {
                        if (p1.serviceTime < p2.serviceTime) return -1;
                        else if (p1.serviceTime > p2.serviceTime) return 1;
                        else // if serviceTime is equal, then we use FCFS;
                        {
                            if (p1.arrivalTime < p2.arrivalTime) return -1;
                            else if (p1.arrivalTime > p2.arrivalTime) return 1;
                            else
                                return 0;
                        }
                    }
                }
        );
        int qi = 0;

        int i = 0; // indicate which timechart it is now
        Process curProcess = null; // use curProcess to check which process is running, how long it runs
        while (i < quantaNum || curProcess != null)
        /*
        Two termination conditions: (1) It's still within the quantaNum;
        (2) the waitingQueue is not empty;
         */
        {
            /*
            At begining of each time slice (quanta), first check if there are any processes in q
            ready to be added into waitingQueue;
             */
            while (qi < q.size() && q.get(qi).getArrivalTime() <= i)
                waitingQueue.add(q.get(qi++));

            /*
            If curProcess is null, means the process has finished, and we will get the next process from
            waitingQueue. If it's not null, we will continue handle it.
             */
            if (curProcess == null)
        	    curProcess = waitingQueue.poll();

            if (curProcess != null) // if there is any process can be run for this time slice (quanta)
            {
                /*
                If time (i) is beyond the quantaNum, and curProcess is never run before (runningTime == 0);
                drop the process;
                 */
                if (i >= quantaNum && curProcess.runningTime == 0) {
                    curProcess = null;
                    continue;
                }
                /*
                else curProcess is run for the first time within quantaNum;
                 */
                else if (curProcess.runningTime == 0) {
                }
                timeChart.add(curProcess);
                currentRunStats.addQuanta();
                curProcess.runningTime++;
                curProcess.lastRunTime = i;
                // if runningTime equals to serviceTime, then curProcess can be finished (set to null)
                if (curProcess.runningTime == curProcess.serviceTime)
                    curProcess = null;
            }
            /*
            If curProcess is null (there is no process can be run this time slice), we still add '-' to this
            timeChart for this time slice.
             */
            else if (i < quantaNum)
                timeChart.addIdlePeriod();

            i++; // if program can come here, meaning the quanta has finished, increase i;

        }
        this.printCurrentRunStats(q);
	}
}
