package com.sanchit.Upsilon.paymentLog;

import com.sanchit.Upsilon.R;

public class PaymentLog {
    private String transactionId;
    private String logMessage;
    private LogType type;
    private Long timeStamp;
    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public PaymentLog(String transactionId, String logMessage, LogType type, Long timeStamp, Integer amount) {
        this.transactionId = transactionId;
        this.logMessage = logMessage;
        this.type = type;
        this.timeStamp = timeStamp;
        this.amount = amount;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public PaymentLog(String transactionId, String logMessage, LogType type, Long timeStamp) {
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