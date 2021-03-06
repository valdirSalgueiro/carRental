package br.com.arizonacrossmedia.carrental.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Rental extends AbstractEntity
{
    @NotEmpty
    private String customer;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date start;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date end;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Rental(String customer, Date start, Date end) {
        this.customer = customer;
        this.start = start;
        this.end = end;
    }

    public Rental() {
    }
}
