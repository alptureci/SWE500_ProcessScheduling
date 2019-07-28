package main;

import java.util.ArrayList;

public class Process implements Comparable<Process> {
    public String name;
    public int arrivalTime;
    public int serviceTime;
    public int priority;
    public int lastRunTime; // last running quanta
    public int runningTime; // how long the process has been running
    public int remainingTime;
    public int waitcount;
    public String color;
    public static final String resetColor = "\u001B[0m";

    public static ArrayList<String> processColors = new ArrayList<String>(){{
        add("\u001B[30m");
        add("\u001B[31m");
        add("\u001B[32m");
        add("\u001B[33m");
        add("\u001B[34m");
        add("\u001B[35m");
        add("\u001B[36m");
        add("\u001B[37m");
    }};

    public void setName(String name) {
        this.name = name;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Process(String name, int arrivalTime, int serviceTime, int priority, int color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.priority = priority;
        this.remainingTime = serviceTime;
        this.color = processColors.get(color);
        this.lastRunTime = 0;
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
