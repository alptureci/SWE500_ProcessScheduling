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
//        int index = 0;
//        waitingQueue.addLast(new RunningProcess(q.get(qi))); // add first process to waitingqueue
//        // for each process in q, compare its service time with other process in waitingqueue
//        // sort waitingqueue with shortest service time
//        for(qi = 1; qi < q.size(); qi++)
//        {
//
//        	for(index = 0; index < waitingQueue.size(); index++)
//        	{
//        		if(q.get(qi).getServiceTime()< waitingQueue.get(index).serviceTime)
//        			{
//        				waitingQueue.add(index, new RunningProcess(q.get(qi)));
//        				break;
//        			}
//        		if(q.get(qi).getServiceTime()== waitingQueue.get(index).serviceTime)
//        		{
//        			if(q.get(qi).getArrivalTime()< waitingQueue.get(index).arrivalTime)
//        				{
//        					waitingQueue.add(index, new RunningProcess(q.get(qi)));
//        					break;
//        				}
//        			if(q.get(qi).getArrivalTime()> waitingQueue.get(index).arrivalTime)
//        				{
//        					waitingQueue.add(index+1, new RunningProcess(q.get(qi)));
//        					break;
//        				}
//        		}
//        	}
//        	if(index == waitingQueue.size())
//        			waitingQueue.addLast(new RunningProcess(q.get(qi)));
//        }
        
// stats not working right. need to fix stats output        
        int i = 0; // indicate which timechart it is now
        Process curProcess = null; // use curProcess to check which process is running, how long it runs
        while (i < quantaNum || curProcess != null)
        /*
        Two termination conditions: (1) It's still within the quantaNum;
        (2) the waitingQueue is not empty;
         */
        {
//            System.out.println(curProcess);
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
                timeChart.add(curProcess.name);
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

//        	for(int j=0;j<curProcess.serviceTime;j++)
//        	{
//        	timeChart.add(curProcess.name);
//        	stats.addProcess();
//            stats.addQuanta();
//            stats.addWaitingTime(i - curProcess.lastRunTime - 1);
//            stats.addTurnaroundTime(i-curProcess.lastRunTime);
//            curProcess.runningTime++;
//            curProcess.lastRunTime = i;
//        	}
        }
        this.printCurrentRunStats(q);
	}
}
