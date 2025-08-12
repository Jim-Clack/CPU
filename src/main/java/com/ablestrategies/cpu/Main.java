package com.ablestrategies.cpu;

/**
 * TODO
 * ENTER: push(All+FP), adjust SP
 * LEAVE/ILEAVE: set SP<-FP, pop(FP+All), RET
 * Implement remaining Opcodes
 * Include doc PDF in repo
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();
    }

    public Main() { // Functor
        CPU cpu = new CPU();
        cpu.setStepping(true);
        SampleIoDevice device = new SampleIoDevice(cpu);
        try {
            device.getSimulatorThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}