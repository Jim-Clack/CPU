package com.ablestrategies.cpu;

import java.util.Scanner;

/**
 * Device Driver
 * 1. Install driver on CPU
 * 2. Implement ICallableDevice to receive output from CPU
 * 3. cpu.ioPorts[OutputPort].setDeviceCallback(this);
 * 4. cpu.ioPorts[OutputPort].setInterruptNumber(OutputIRQ);
 * 5. cpu.ioPorts[InputPort].setInterruptNumber(InputIRQ);
 * 6. To send input to CPU: cpu.ioPorts[InputPort].inputToCpu(value);
 */
public class SampleIoDevice implements ICallableDevice {

    public static int OutputIRQ = 4;
    public static int OutputPort = 10;
    public static int InputIRQ = 5;
    public static int InputPort = 11;
    private final CPU cpu;
    private final SampleIoDevice thisGuy = this;

    private final String driverCode =
            "0:0, 22, 75\n" +
            "5:0\n";

    public SampleIoDevice(CPU cpu) {
        this.cpu = cpu;
        installDriver();
        Thread simulator = simulate();
        simulator.setName("SampleIoDevice-Simulator-Thread");
        simulator.setDaemon(true);
        simulator.start();
    }

    private Thread simulate() {
        return new Thread() {
            public void run() {
                cpu.ioPorts[OutputPort].setDeviceCallback(thisGuy);
                cpu.ioPorts[OutputPort].setInterruptNumber(OutputIRQ);
                cpu.ioPorts[InputPort].setInterruptNumber(InputIRQ);
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

    private void installDriver() {
        new Assembler(cpu).assemble(driverCode);
    }

    @Override
    public int OutputFromCPU(int value) {
        System.out.println("\nOutput from CPU simulator to SampleIoDevice: " + value);
        return 0; // no interrupt needed
    }

}
