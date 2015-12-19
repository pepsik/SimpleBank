package com.aleksandr.berezovyi.model;

import java.time.LocalDateTime;

/**
 * Created by pepsik on 12/18/2015.
 */
public class Payment {
    private Long id;
    private Long senderAccountId;
    private Long recipientAccountId;
    private Integer amount;
    private LocalDateTime when;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
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
