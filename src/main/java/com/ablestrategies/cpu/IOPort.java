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
    public Octet clone(Octet other) {
        super.clone(other);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
        return other;
    }

    @Override
    public void set(Octet from) {
        super.set(from);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
    }

    @Override
    public void setUnsignedValue(int intVal) {
        super.setUnsignedValue(intVal);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
    }

    @Override
    public void setBit(int bitNum, Bit bit) {
        super.setBit(bitNum, bit);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
    }

    @Override
    public void set(int from) {
        super.set(from);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
    }

    @Override
    public void set(String from) {
        super.set(from);
        interruptableALU.sendInterrupt(callableDevice.acceptOutputFromCPU(this.getIntValue()));
    }

}