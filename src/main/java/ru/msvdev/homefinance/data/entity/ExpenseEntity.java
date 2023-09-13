package ru.msvdev.homefinance.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "expenses")
public class ExpenseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "date")
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    
    @Column(name = "cost")
    private BigDecimal cost;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 32)
    private State state;
    
    
    public enum State {
        APPROVED, NOT_APPROVED
    }
}