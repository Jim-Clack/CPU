package com.ablestrategies.cpu;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
    }

    public Main() { // Functor
        CPU cpu = new CPU();
        cpu.setTracing(true);
        cpu.setTraceCells(5, 8);
        SampleIoDevice device = new SampleIoDevice(cpu);
        cpu.run(false);
        try {
            device.getSimulatorThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}