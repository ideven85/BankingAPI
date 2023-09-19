package bankingservice.service.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "statement")
@NamedQueries({
        @NamedQuery(name = "getStatements", query = "select s from StatementsEntity s where s.accountsEntity = :aid")})
public class StatementsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reference_number", length = 64, nullable = false)
    private String referenceNumber;

    @Column(name = "debit")
    private Integer debit;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "date")
    private Date date;

    @Column(name = "balance")
    private Integer balance;

    @ManyToOne
    @JoinColumn(name = "aid")
    private AccountsEntity accountsEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Integer getDebit() {
        return debit;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AccountsEntity getAccountsEntity() {
        return accountsEntity;
    }

    public void setAccountsEntity(AccountsEntity accountsEntity) {
        this.accountsEntity = accountsEntity;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
