package main;

import java.util.ArrayList;

/* Class to track statistics for a single run */
public class StatsPerRun {
    private int totalTurnaroundTime;
    private int totalWaitingTime;
    private int totalServiceTime;
    private int processCount;
    private int quanta;
    private ArrayList<String> timeChart;

    public StatsPerRun() {
        this.totalTurnaroundTime = 0;
        this.totalWaitingTime = 0;
        this.totalServiceTime = 0;
        this.processCount = 0;
        this.quanta = 0;
        this.timeChart = new ArrayList<>();
    }

    public double getAvgTurnaroundTime () {
        return (double)totalTurnaroundTime / processCount;
    }

    public double getAvgWaitingTime () {
        return (double)totalWaitingTime / processCount;
    }

    public double getAvgResponseTime () {
        return (double) totalServiceTime / processCount;
    }

    public double getAvgThroughput() {
        return 150 * (double)processCount / quanta;
    }

    public void addWaitingTime(int time) {
        this.totalWaitingTime += time;
    }

    public void addServiceTime(int time) {
        this.totalServiceTime += time;
    }

    public void addTurnaroundTime(int time) {
        this.totalTurnaroundTime += time;
    }

    public void addProcess() {
        ++this.processCount;
    }

    public void addQuanta() {
        this.quanta++;
    }

    public int getProcessCount() {
        return processCount;
    }

    public int getQuanta() {
        return quanta;
    }

    /**
     * Print out the average statistics for the current round
     */
    public void printRoundAvgStats(ArrayList<Process> q) {
        calculateAvgStats(q);
        System.out.format("    Turnaround: %-2.3f Service: %-2.3f Waiting: %-2.3f Throughput: %-2.3f/%d quantas\n",
                getAvgTurnaroundTime(), getAvgResponseTime(), getAvgWaitingTime(), getAvgThroughput(), getQuanta());
        System.out.println();
    }


    public void calculateAvgStats(ArrayList<Process> q) {
        for (Process p : q)
        {
            if (p.lastRunTime > 0)
            {
                this.addServiceTime(p.serviceTime);
                this.addTurnaroundTime(p.lastRunTime - p.arrivalTime);
                this.addWaitingTime(p.lastRunTime - p.arrivalTime - p.serviceTime);
                this.addProcess();
            }
        }
    }
}
