package com.ablestrategies.cpu;

public interface ICallableDevice {
    int OutputFromCPU(int value); // returns interrupt number, 0 if none
}
