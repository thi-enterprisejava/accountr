package de.thi.phm6101.accountr.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Created by philipp on 08/12/15.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findByName",
                query = "SELECT t FROM Account as t WHERE t.name LIKE :name")
})
public class Account extends AbstractEntity{


    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transaction> transactions;

    @NotNull
    private String name;

    private String description;

    private Currency currency;

    //
    // CONSTRUCTION
    //

    public Account() {
        transactions = new ArrayList<>();
        currency = Currency.getInstance(Locale.getDefault());
    }

    //
    // PROPERTY ACCESS
    //

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(this.transactions);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        if (transaction.getAccount() != this) {
            transaction.setAccount(this);
        }
    }

    public Optional<Transaction> getTransaction(long id) {
        return getTransactions().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public void removeTransaction(Transaction transaction) {
        if (transactions.contains(transaction)) {
            transactions.remove(transaction);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return transactions.stream().mapToDouble(Transaction::getAmount).summaryStatistics().getSum();
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    //
    // IDENTITY
    //


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return !(getName() != null ? !getName().equals(account.getName()) : account.getName() != null);

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}

