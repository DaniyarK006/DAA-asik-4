package com.smartcity.common;

import java.util.HashMap;
import java.util.Map;

public class MetricsImpl implements Metrics {
    private final Map<String, Integer> counters;
    private long startTime;
    private long endTime;

    public MetricsImpl() {
        this.counters = new HashMap<>();
        this.startTime = 0;
        this.endTime = 0;
    }

    @Override
    public void incrementCounter(String name) {
        counters.put(name, counters.getOrDefault(name, 0) + 1);
    }

    @Override
    public void addCounter(String name, int value) {
        counters.put(name, counters.getOrDefault(name, 0) + value);
    }

    @Override
    public int getCounter(String name) {
        return counters.getOrDefault(name, 0);
    }

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        endTime = System.nanoTime();
    }

    @Override
    public long getElapsedNanos() {
        return endTime - startTime;
    }

    @Override
    public double getElapsedMillis() {
        return getElapsedNanos() / 1_000_000.0;
    }

    @Override
    public void reset() {
        counters.clear();
        startTime = 0;
        endTime = 0;
    }

    @Override
    public Map<String, Integer> getAllCounters() {
        return new HashMap<>(counters);
    }

    @Override
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Metrics Summary ===\n");
        sb.append(String.format("Time: %.3f ms\n", getElapsedMillis()));
        sb.append("Counters:\n");
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            sb.append(String.format("  %s: %d\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}