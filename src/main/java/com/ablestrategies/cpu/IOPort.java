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
        setUnsignedValue(value);
        interruptableALU.sendInterrupt(interruptNumber);
    }

    // Output APIs
    @Override
    public void setUnsignedValue(int intVal) {
        super.setUnsignedValue(intVal);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
    }

    @Override
    public void set(int from) {
        super.set(from);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
    }

}