package com.ablestrategies.cpu;

/**
 * Device Installation
 * 1. Install driver on CPU with an ISR for input and any means for output.
 * 2. Externally, implement ICallableDevice acceptOutputFromCPU()
 * 3. Externally, cpu.ioPorts[OutputPort].setDeviceCallback(this);
 * 4. Externally, cpu.ioPorts[OutputPort].setInterruptNumber(OutputIRQ);
 * 5. Externally, cpu.ioPorts[InputPort].setInterruptNumber(InputIRQ);
 * 6. Output from CPU will always run on the Main thread.
 * 7. You may have to launch a Thread to send input to the CPU.
 * 8. To send input to CPU, call: cpu.ioPorts[InputPort].inputToCpu(value);
 * 9. To receive output from CPU: acceptOutputFromCPU(value) will be called.
 * 10. You may use MemoryCell, but you need an IOPort for the ISR or Callback.
 */
public interface ICallableDevice {
    int acceptOutputFromCPU(int value); // returns interrupt number, 0 if none
}
