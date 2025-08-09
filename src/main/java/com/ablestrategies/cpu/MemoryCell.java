package com.ablestrategies.cpu;

public class MemoryCell implements IDataCell {

    ////////////////////////////////// IData /////////////////////////////////

    public int getIntValue() {
        return 0; // content.intValue();
    }

    @Override
    public void setIntValue(int val) {

    }

    @Override
    public IDataCell get() {
        return null;
    }

    @Override
    public void set(IDataCell value) {
        // result.set(value);
    }

    @Override
    public void unsignedSetInt(int val) {

    }

    @Override
    public int unsignedGetInt() {
        return 0; // TODO
    }
}
