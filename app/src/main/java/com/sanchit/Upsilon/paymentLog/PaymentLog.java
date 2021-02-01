package com.sanchit.Upsilon.paymentLog;

public class PaymentLog {
    private String transactionId;
    private String logMessage;
    private LogType type;

    public PaymentLog(String transactionId, String message, LogType type) {
        this.transactionId = transactionId;
        this.logMessage = message;
    }

    public PaymentLog(LogType type) {
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