package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class MonthClosure extends BaseEntity<Long> {
    private String month;
    private Integer year;
    private BigDecimal total;
    private BigDecimal available;
    private BigDecimal expenses;
    private Integer index;

    @Column(name = "final_usd_to_brl")
    private BigDecimal finalUsdToBRL;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean validate() {
        return true;
    }
}
