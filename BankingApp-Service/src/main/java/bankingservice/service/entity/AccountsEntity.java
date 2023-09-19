package bankingservice.service.entity;

import javax.persistence.*;


@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(name = "getAmount", query = "select a from AccountsEntity a where a.accountNumber = :accountNumber"),
        @NamedQuery(name = "getAccounts", query = "select a from AccountsEntity a where a.personEntity = :entity")})

public class AccountsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private Integer balance;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pid")
    private PersonEntity personEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public PersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }
}
