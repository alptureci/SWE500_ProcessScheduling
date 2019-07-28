package main;

import java.util.ArrayList;
import java.util.Hashtable;

public class Stats
{
    public Hashtable<String, ArrayList<StatsPerRun>> StatsTable = new Hashtable<>();

    /**
     * Print out the average statistics for the given scheduling algorithm
     */
    public void printAvgOverallStats()
    {
        StatsTable.forEach((k, v) -> {
            System.out.format("%s (%d runs statistics): \n",k, v.size());
            double totalAvgTurnaround = 0;
            double totalAvgWaiting = 0;
            double totalAvgResponse = 0;
            double totalAvgThroughput = 0;
            double totalAvgQuanta = 0;
            for (StatsPerRun stat : v) {
                totalAvgTurnaround += stat.getAvgTurnaroundTime();
                totalAvgResponse += stat.getAvgServiceTime();
                totalAvgWaiting += stat.getAvgWaitingTime();
                totalAvgThroughput += stat.getAvgThroughput();
                totalAvgQuanta += stat.getQuanta();
            }
            System.out.format("    Turnaround: %-2.3f Response: %-2.3f Waiting: %-2.3f Throughput: %-2.3f/%-2.3f quantas\n",
                    totalAvgTurnaround/v.size(), totalAvgResponse/v.size(),
                    totalAvgWaiting/v.size(),  totalAvgThroughput/v.size(), totalAvgQuanta/v.size());
        } );

    }
}
