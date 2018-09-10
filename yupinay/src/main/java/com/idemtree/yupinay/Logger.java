package com.idemtree.yupinay;

public abstract class Logger {
    public abstract <L extends LogMessage> void log(L logMessage);
    public abstract void log(String logMessage);
}
