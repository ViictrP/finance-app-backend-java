package com.victorprado.financeapp.infra.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit_card", schema = "finance_app")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardModel extends Model {

  private String title;
  private String description;
  private String number;
  private Integer invoiceClosingDay;
  private String backgroundColor;

  @ManyToOne
  private UserModel user;

  @OneToMany(mappedBy = "creditCard")
  private List<InvoiceModel> invoices;
}
