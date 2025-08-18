package com.ablestrategies.cpu;

import java.util.Scanner;

/**
 * Device Driver - Echoes an integer as its ASCII character equivalent.
 */
public class SampleIoDevice implements ICallableDevice {

    public static int OutputPort = 10;
    public static int InputIRQ = 5;
    public static int InputPort = 11;

    private final CPU cpu;
    private final Thread simulator;

    private final String driverCode =
        """
            # SampleIoDevice Driver       Addr: HexCodes
                      JMPIMM Begin:
             Irq:     EQU 5
             OutPort: EQU 10
             InPort:  EQU 11
             Buf:     0                   # Inp -> Buf: -> Out
             Begin:   LOADIMM $IV, Isr:   # Enable interrupts
             Loop:    ZEROREG $1
                      ADDMEM $1, Buf:     # Add to set Flags
                      JZEIMM Loop:        # Wait for Buf != 0
                      INCREG $1
                      OUTREG $1, OutPort: # Echo it
                      ZEROREG $2
                      STORMEM $2, Buf:    # Clear the Buffer
                      JMPIMM Loop:        # Else keep looping
             # --------------------------------
             # Here's the ISR...
             Isr:     ENTER 0             # When input is rcv'd...
                      CMPIMM $IN, Irq:    # Make sure it's for us
                      JNZEIMM Leave:      # If not Our IRQ
                      INPREG $2, InPort:  # Read it
                      STORMEM $2, Buf:    # Put it into Buf
             Leave:   ILEAVE
             Exit:
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
        cpu.ioPorts[InputPort].setDeviceCallback(this);
        cpu.ioPorts[InputPort].setInterruptNumber(InputIRQ);
        cpu.setTraceCells(3, 3);
     //   cpu.setTracingDelayMs(200);
        return new Thread() {
            public void run() {
                System.out.print("\n#############\n" +
                        "Type a Number 0..99 to send to CPU Simulator, then hit Enter.\n" +
                        "The Driver will add 1 to it, then reply with that value...\n#############\n\n>>");
                Scanner keyboard = new Scanner(System.in);
                while(true) {
                    String command = keyboard.nextLine();
                    if (!command.isEmpty()) {
                        int value = Integer.parseInt(command);
                        System.out.println("\n#############\nSending: " + value + " to CPU Simulator input...\n##############");
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
        System.out.println("\n#############\nOutput from CPU simulator to SampleIoDevice: " + value + "\n#############");
        return 0; // no interrupt needed
    }

    private void installDriver() {
        new Assembler(cpu).assemble(driverCode);
    }

}
