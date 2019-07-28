package main;

import java.util.ArrayList;

public class TimeChart {

    private String idleString = "-";
    private ArrayList<String> TimeChart = new ArrayList<>();

    public void addIdlePeriod()
    {
        TimeChart.add(idleString);
    }

    /**
     * Print a "time chart" of the results, e.g. ABCDABCD...
     */
    public void printTimeChart()
    {
        System.out.print("    ");
        TimeChart.forEach(x -> System.out.print(x));
        System.out.println();
    }

    public void add(String str)
    {
        TimeChart.add(str);
    }
}
