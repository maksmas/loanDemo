package lv.mmm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lv.mmm.utils.DateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "LOANS")
public class Loan implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "USERS.ID")
    private User user;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "TERM")
    private Integer term; //Day #
    @Column(name = "APPLICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = DateSerializer.class)
    private Date applicationDate;
    @Column(name = "APPLICATION_COUNTRY")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String applicationCountry;

    public Loan() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationCountry() {
        return applicationCountry;
    }

    public void setApplicationCountry(String applicationCountry) {
        this.applicationCountry = applicationCountry;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
