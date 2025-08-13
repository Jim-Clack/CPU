package com.ablestrategies.cpu;

import java.util.Scanner;

/**
 * Device Driver - Echoes an integer as its ASCII character equivalent.
 */
public class SampleIoDevice implements ICallableDevice {

    public static int OutputPort = 10;
    public static int InputIRQ = 5;
    public static int InputPort = 11;
    // public static int OutputIRQ = 4; // Not used for this device

    private final CPU cpu;
    private final Thread simulator;

    private final String driverCode =
        """
            0: LOADIMM, 2, Label2
            Label1: '1, 10, 100'
            Label2: CALL 1234
        """;

    public SampleIoDevice(CPU cpu) {
        this.cpu = cpu;
        installDriver();
        simulator = simulate();
        simulator.setName("SampleIoDevice-Simulator-Thread");
        simulator.setDaemon(true);
        simulator.start();
    }

    private Thread simulate() {
        cpu.ioPorts[OutputPort].setDeviceCallback(this);
        cpu.ioPorts[InputPort].setInterruptNumber(InputIRQ);
        // cpu.ioPorts[OutputPort].setInterruptNumber(OutputIRQ);
        return new Thread() {
            public void run() {
                Scanner keyboard = new Scanner(System.in);
                while(true) {
                    System.out.println("\nInteger to send to CPU Simulator, or type quit, then hit Enter");
                    String command = keyboard.nextLine();
                    if (command.equalsIgnoreCase("quit")) {
                        break;
                    }
                    if (!command.isEmpty()) {
                        int value = Integer.parseInt(command);
                        System.out.println("\nSending: " + value + " to CPU Simulator input...");
                        cpu.ioPorts[InputPort].inputToCpu(value);
                    }
                }
            }
        };
    }

    public Thread getSimulatorThread() {
        return simulator;
    }

    @Override
    public int acceptOutputFromCPU(int value) {
        System.out.println("\nOutput from CPU simulator to SampleIoDevice: " + value);
        return 0; // no interrupt needed
    }

    private void installDriver() {
        new Assembler(cpu).assemble(driverCode);
    }

}
