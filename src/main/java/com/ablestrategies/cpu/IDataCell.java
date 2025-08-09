package com.ablestrategies.cpu;

public interface IDataCell {

    int getIntValue();

    void setIntValue(int val);

    IDataCell get();

    void set(IDataCell value);

    void unsignedSetInt(int val);

    int unsignedGetInt();

}
