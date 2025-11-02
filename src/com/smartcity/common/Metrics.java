package com.smartcity.common;

import java.util.HashMap;
import java.util.Map;

/** Interface for tracking algorithm performance metrics  */
public interface Metrics {
    void incrementCounter(String name);
    void addCounter(String name, int value);
    int getCounter(String name);
    void startTimer();
    void stopTimer();
    long getElapsedNanos();
    double getElapsedMillis();
    void reset();
    Map<String, Integer> getAllCounters();
    String getSummary();
}