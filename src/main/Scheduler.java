package main;


import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * An abstract object representing a scheduling algorithm which schedules a process queue
 * @author
 */
public abstract class Scheduler {
    private ArrayList<StatsPerRun> overallStats;
    protected String scheduler;

    //    private Stats stats = new Stats();
    public Scheduler(String scheduler) {
        this.scheduler = scheduler;
        overallStats = new ArrayList<>();
    }

    /* A inner class to track statistics for a single run */
    public class StatsPerRun {
        private int totalTurnaroundTime;
        private int totalWaitingTime;
        private int totalResponseTime;
        private int processCount;
        private int quanta;

        public StatsPerRun() {
            this.totalTurnaroundTime = 0;
            this.totalWaitingTime = 0;
            this.totalResponseTime = 0;
            this.processCount = 0;
            this.quanta = 0;
        }

        public double getAvgTurnaroundTime () {
            return (double)totalTurnaroundTime / processCount;
        }

        public double getAvgWaitingTime () {
            return (double)totalWaitingTime / processCount;
        }

        public double getAvgResponseTime () {
            return (double)totalResponseTime / processCount;
        }

        public double getAvgThroughput() {
            return 100 * (double)processCount / quanta;
        }

        public void addWaitingTime(int time) {
            this.totalWaitingTime += time;
        }

        public void addResponseTime(int time) {
            this.totalResponseTime += time;
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
        public void printRoundAvgStats() {
            System.out.format("    Turnaround: %-2.3f Response: %-2.3f Waiting: %-2.3f Throughput: %-2.3f/150 quantas\n",
                    getAvgTurnaroundTime(), getAvgResponseTime(), getAvgWaitingTime(), getAvgThroughput());
            System.out.println();
        }
    }

    /**
     * Print a "time chart" of the results, e.g. ABCDABCD...
     * @param timeChart A list of Processes that have been scheduled
     */
    public static void printTimeChart(ArrayList<Character> timeChart)
    {
        int quanta = 0;
        System.out.print("    ");
        for (char c : timeChart) {
            System.out.print(c);
//            System.out.print(' ');
        }
        System.out.println();
    }

    /**
     * Add the result of single run into final result array
     */
    public void storeOneRoundStats (StatsPerRun stat) {
        overallStats.add(stat);
    }

    /**
     * Print out the average statistics for the given scheduling algorithm
     */
    public void printAvgStats()
    {
        double totalAvgTurnaround = 0;
        double totalAvgWaiting = 0;
        double totalAvgResponse = 0;
        double totalAvgThroughput = 0;
        for (StatsPerRun stat : overallStats) {
            totalAvgTurnaround += stat.getAvgTurnaroundTime();
            totalAvgResponse += stat.getAvgResponseTime();
            totalAvgWaiting += stat.getAvgWaitingTime();
            totalAvgThroughput += stat.getAvgThroughput();
        }
        System.out.format("%s (%d run statistics): \n",this.scheduler, overallStats.size());
        System.out.format("    Turnaround: %-2.3f Response: %-2.3f Waiting: %-2.3f Throughput: %-2.3f/150 quantas\n",
                totalAvgTurnaround/overallStats.size(), totalAvgResponse/overallStats.size(),
                totalAvgWaiting/overallStats.size(),  totalAvgThroughput/overallStats.size());
    }

    /**
     * Go through the process list sorted by arrival time
     * and schedule them according to the selected scheduling algorithm
     */
    public abstract void schedule(ArrayList<Process> q, int quantaNum);
}
