package com.aleksandr.berezovyi.model;

import com.aleksandr.berezovyi.model.util.PaymentStatus;

import java.time.LocalDateTime;

/**
 * Created by pepsik on 12/18/2015.
 */


public class Payment {
    private Long id;
    private Long senderAccountId;
    private Long recipientAccountId;
    private volatile Double amount;
    private LocalDateTime when;
    private PaymentStatus status = PaymentStatus.NOT_COMPLETE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Long getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(Long recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "sender=" + senderAccountId +
                ", recipient=" + recipientAccountId +
                ", amount=" + amount +
                ", when=" + when +
                '}';
    }
}
