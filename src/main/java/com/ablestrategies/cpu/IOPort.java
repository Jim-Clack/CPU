package com.ablestrategies.cpu;

public class IOPort extends Octet {

    private final IInterruptable interruptableALU;
    private ICallableDevice callableDevice = null;
    private int interruptNumber = 0;

    public IOPort(IInterruptable interruptableALU) {
        this.interruptableALU = interruptableALU;
    }

    public void setDeviceCallback(ICallableDevice callableDevice) {
        this.callableDevice = callableDevice;
    }

    public void setInterruptNumber(int interruptNumber) {
        this.interruptNumber = interruptNumber;
    }

    // Input API
    public void inputToCpu(int value) {
        set(value);
        interruptableALU.sendInterrupt(interruptNumber);
    }

    // Output APIs
    @Override
    public void set(int intVal) {
        super.set(intVal);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getSignedValue()));
    }

}