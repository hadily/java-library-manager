package com.example.miniprojetjava.model;

import java.sql.Date;
import java.time.LocalDate;

public class Membership {
    private int membershipId;
    private Reader reader;
    private Date creationDate;
    private Date endDate;
    private String status;

    public Membership(Reader reader, Date creationDate, Date endDate, String status) {
        this.reader = reader;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Membership(int membershipId, Reader reader, Date creationDate, Date endDate, String status) {
        this.membershipId = membershipId;
        this.reader = reader;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMembershipId() { return this.membershipId; }

    public void setMembershipId(int membershipId) { this.membershipId = membershipId; }

    public void updateStatus() {
        if (endDate.after(Date.valueOf(LocalDate.now()))) {
            this.status = "Expired";
        } else {

        }
    }
}
