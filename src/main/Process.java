package main;

public class Process implements Comparable<Process> {
    public String name;
    public int arrivalTime;
    public int serviceTime;
    public int priority;
    public int lastRunTime; // last running quanta
    public int runningTime; // how long the process has been running
    public int remainingTime;
    public int waitcount;

    public void setName(String name) {
        this.name = name;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Process(String name, int arrivalTime, int serviceTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.priority = priority;
        this.remainingTime = serviceTime;

//            this.startTime = -1;
        this.lastRunTime = this.arrivalTime - 1;
        this.runningTime = 0;
    }



    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getExpectedEndTime() {
        return arrivalTime + serviceTime;
    }

    @Override
    public String toString() {
        return "Process{" +
                "name=" + name +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                ", completionTime=" + lastRunTime +
                ", priority=" + priority +
                ", expectedEndTime=" + (arrivalTime+serviceTime) +
                '}';
    }

    @Override
    public int compareTo(Process p) {
        if (this.arrivalTime < p.arrivalTime) return -1;
        else if (this.arrivalTime > p.arrivalTime) return 1;
        else return 0;
    }
}
