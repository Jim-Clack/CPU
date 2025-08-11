package com.ablestrategies.cpu;

public interface ICallableDevice {
    int OutputFromALU(int value); // returns interrupt number, 0 if none
}
