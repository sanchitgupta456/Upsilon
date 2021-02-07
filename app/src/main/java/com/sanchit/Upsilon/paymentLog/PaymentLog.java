package com.sanchit.Upsilon.paymentLog;

import com.sanchit.Upsilon.R;

public class PaymentLog {
    private String transactionId;
    private String logMessage;
    private LogType type;
    private Integer timeStamp;

    public Integer getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    public PaymentLog(String transactionId, String logMessage, LogType type, Integer timeStamp) {
        this.transactionId = transactionId;
        this.logMessage = logMessage;
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public PaymentLog(String transactionId, String message, LogType type) {
        this.transactionId = transactionId;
        this.logMessage = message;
        this.type = type;
    }

    public PaymentLog(LogType type) {
        transactionId = "Please wait...";
        logMessage = "This is a test message of type " + type.toString();
        this.type = type;
    }

    public PaymentLog() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String message) {
        this.logMessage = message;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }
}