package com.ablestrategies.cpu;

/**
 * Device Installation
 * 1. Install driver on CPU
 * 2. Implement ICallableDevice (acceptOutputFromCPU)
 * 3. cpu.ioPorts[OutputPort].setDeviceCallback(this);
 * 4. cpu.ioPorts[OutputPort].setInterruptNumber(OutputIRQ);
 * 5. cpu.ioPorts[InputPort].setInterruptNumber(InputIRQ);
 * 6. To send input to CPU, call: cpu.ioPorts[InputPort].inputToCpu(value);
 * 7. To receive output from CPU: acceptOutputFromCPU(value) will be called.
 */
public interface ICallableDevice {
    int acceptOutputFromCPU(int value); // returns interrupt number, 0 if none
}
