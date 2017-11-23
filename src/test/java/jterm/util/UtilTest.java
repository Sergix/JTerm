package jterm.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void getRunTime() {
        assertEquals(Util.getRunTime(172799999), "1 days, 23 hours, 59 minutes, 59 seconds, 999 millis");
    }

    @Test
    void clearLine() {
    }

    @Test
    void getAsArray() {
        assertEquals(Util.getAsArray("This function is just splitting on spaces"),
                Arrays.asList("This", "function", "is", "just", "splitting", "on", "spaces"));
    }

    @Test
    void getAsString() {
        assertEquals("This function is just concatenating an array",
                Util.getAsString(Arrays.asList("This", "function", "is", "just", "concatenating", "an", "array")));
    }

    @Test
    void getRest() {
        assertEquals("is just concatenating an array",
                Util.getRest(Arrays.asList("This", "function", "is", "just", "concatenating", "an", "array"), 2));
    }

    @Test
    void getFullPath() {
    }

    private void compareTimes(Runnable r1, Runnable r2) {
        averageTimeOfExecution(r1);
        long r1time = averageTimeOfExecution(r1);
        randomBranches();
        averageTimeOfExecution(r2);
        long r2time = averageTimeOfExecution(r2);
        System.out.println(String.format("R1: %d, R2: %d", r1time, r2time));
    }

    private void randomBranches(){
        int rand = 0;
        for(int i = 0; i < 10000; i++){
            if(i+(Math.random() * 100) < i+(Math.random() * 100)){
                rand++;
            }else{
                rand--;
            }
        }
    }

    private long averageTimeOfExecution(Runnable r) {
        long before, after;
        long total = 0;
        for (int i = 0; i < 1000; i++) {
            before = System.nanoTime();
            r.run();
            after = System.nanoTime();
            total += after - before;
        }
        return total / 1000;
    }
}