package com.victorprado.financeappbackendjava.domain.entity;

import com.victorprado.financeappbackendjava.domain.enums.UserProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.CascadeType.REMOVE;

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

    @OneToMany(mappedBy = "user", cascade = REMOVE, fetch = FetchType.LAZY)
    @OrderBy("id")
    private List<CreditCard> creditCards;

    @OneToMany(mappedBy = "user", cascade = REMOVE, fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", cascade = REMOVE, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<RecurringExpense> recurringExpenses;

    @OneToMany(mappedBy = "user", cascade = REMOVE, fetch = FetchType.LAZY)
    @OrderBy("index DESC")
    private List<MonthClosure> monthClosures;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_property", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "property_name", length = 100)
    @Column(name = "property_value", length = 500)
    private Map<String, @NotBlank String> properties = new HashMap<>();

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

    public String getProperty(UserProperty userProperty) {
        if (this.properties.containsKey(userProperty.getName())) {
            return this.properties.get(userProperty.getName());
        }

        return null;
    }

}
