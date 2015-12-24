package com.aleksandr.berezovyi.model;


import com.aleksandr.berezovyi.model.converter.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by pepsik on 12/18/2015.
 */
@Entity
public class Payment {
    @Id @GeneratedValue
    private Long id;
    @Column(name = "SENDERACCID")
    private Long senderAccountId;
    @Column(name = "RECIPIENTACCID")
    private Long recipientAccountId;
    private Double amount;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
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

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", senderAccountId=" + senderAccountId +
                ", recipientAccountId=" + recipientAccountId +
                ", amount=" + amount +
                ", when=" + when +
                '}';
    }
}
