package main;

public class Process implements Comparable<Process> {
    private char name;
    private int arrivalTime;
    private int serviceTime;
    private int priority;

    public void setName(char name) {
        this.name = name;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Process(char name, int arrivalTime, int serviceTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }



    public char getName() {
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
                ", expectedEndTime=" + (arrivalTime+serviceTime) +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int compareTo(Process p) {
        if (this.arrivalTime < p.arrivalTime) return -1;
        else if (this.arrivalTime > p.arrivalTime) return 1;
        else return 0;
    }
}
