package com.victorprado.financeapp.infra.model;

import com.victorprado.financeapp.core.entities.User;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "finance_app_user", schema = "finance_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel extends Model {

  private String name;
  private String lastname;
  private String email;
  private String password;
  private boolean active;
  private BigDecimal salary;

  public UserModel(@NotNull User user) {
    this.name = user.getName();
    this.lastname = user.getLastname();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.salary = user.getSalary();
    this.active = user.isActive();
  }

  @OneToMany(mappedBy = "user")
  private List<CreditCardModel> creditCards;

  @OneToMany(mappedBy = "user")
  private List<TransactionModel> transactions;
}
