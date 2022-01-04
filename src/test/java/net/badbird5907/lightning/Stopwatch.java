package net.badbird5907.lightning;

import java.util.ArrayList;
import java.util.List;

public class Stopwatch { //shitty class ignore this
    private List<Long> milliseconds = new ArrayList<>();
    private long start;
    public void start(){
        start = System.currentTimeMillis();
    }
    public void end(){
        milliseconds.add(System.currentTimeMillis() - start);
    }
    public void startEnd(){
        end();
        start();
    }
    public int getAverage(){
        int total = milliseconds.size();
        int i = 0;
        for (Long millisecond : milliseconds) {
            i += millisecond;
        }
        return i / total;
    }
    public int getTotal(){
        int i = 0;
        for (Long millisecond : milliseconds) {
            i += millisecond;
        }
        return i;
    }
}