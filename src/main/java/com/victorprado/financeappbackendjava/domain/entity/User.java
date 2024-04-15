package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity(name = "finance_app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity<Long> {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The lastname is required")
    private String lastname;

    @NotBlank(message = "The email is required")
    @Email(message = "The email should be in the correct format")
    private String email;

    @NotNull(message = "The salary is required")
    private BigDecimal salary;

    private Boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecurringExpense> recurringExpenses;

    @Override
    public boolean validate() {
        return false;
    }

    public void addCreditCard(CreditCard creditCard) {
        if (this.creditCards == null) {
            this.creditCards = new ArrayList<>();
        }

        creditCard.setUser(this);
        this.creditCards.add(creditCard);
    }

    public void addTransaction(Transaction transaction) {
        if (this.transactions == null) {
            this.transactions = new ArrayList<>();
        }

        transaction.setUser(this);
        this.transactions.add(transaction);
    }
}
