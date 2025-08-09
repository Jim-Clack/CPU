package com.ablestrategies.cpu;

public enum ArgType {
    // No arguments
    NO_ARGS(0),
    NONE(0),
    // One argument
    ONE_ARG(10),
    VALUE(10),
    REGISTER(11),
    MEMORY(12),
    // Two arguments
    TWO_ARGS(20),
    REG_AND_MEM(20),
    REG_AND_VALUE(21),
    REG_AND_REG(22),
    REG_AND_LOCAL(23);

    private final int typeNum;

    ArgType(int typeNum) {
        this.typeNum = typeNum;
    }

    int typeNum() {
        return typeNum;
    }
}
