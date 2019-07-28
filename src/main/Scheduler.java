package main;


import java.util.ArrayList;
import java.util.LinkedList;

/**
 * An abstract object representing a scheduling algorithm which schedules a process queue
 * @author
 */
public abstract class Scheduler {
    protected Stats overallStats = new Stats();
    protected String algorithmName;
    protected StatsPerRun currentRunStats;
    protected LinkedList<Process> waitingQueue;
    protected ArrayList<LinkedList<Process>> waitingQueues;
    protected TimeChart timeChart;
    protected static final int NUM_PRIORITY = 4;

    //    private Stats stats = new Stats();
    public Scheduler(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void printCurrentRunStats(ArrayList<Process> q)
    {
        System.out.println(this.algorithmName);
        timeChart.printTimeChart();
        currentRunStats.printRoundAvgStats(q);
    }

    public void printAvgOverAllStats()
    {
        overallStats.printAvgOverallStats();
    }
    /**
     * Go through the process list sorted by arrival time
     * and schedule them according to the selected scheduling algorithm
     */
    public void schedule(ArrayList<Process> q, int quantaNum)
    {
        for (Process p : q)
        {
            p.runningTime = 0;
            p.lastRunTime = 0;
            p.waitcount = 0;
        }
        this.waitingQueue = new LinkedList<>();
        this.waitingQueues = new ArrayList<>();
        this.timeChart = new TimeChart();
        this.currentRunStats = new StatsPerRun();
        if (overallStats.StatsTable.containsKey(this.algorithmName))
        {
            overallStats.StatsTable.get(this.algorithmName).add(currentRunStats);
        }
        else
        {
            overallStats.StatsTable.put(this.algorithmName, new ArrayList<>() {{add(currentRunStats);}});
        }
    }
}
