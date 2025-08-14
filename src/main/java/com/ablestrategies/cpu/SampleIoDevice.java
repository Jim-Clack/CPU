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
                    # Test                Addr: HexCodes
            0:      LOADIMM, 2, LabelA:   # 00: cc 02 05
                    JMP LabelB:           # 03: 0e 08
            LabelA: 1, 10, 100            # 05: 01 0a 64
            LabelB: CALL 24               # 08: 12 14 
                    "ABC"                 # 0a: 41 42 43 00
                    LOADMEM 1, LabelC:    # 0e: ce 01 12
                    RET                   # 11: 05
            LabelC: "D"                   # 12: 44 00
                                          # 14: -- -- -- --                   
            24:     ENTER 0               # 18: 0b 00
                    LEAVE                 # 1a: 08
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
