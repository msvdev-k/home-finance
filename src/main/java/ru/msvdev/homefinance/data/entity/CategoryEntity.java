package ru.msvdev.homefinance.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;


@Data
@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @OneToMany(mappedBy = "category")
    private Collection<ExpenseEntity> expenses;
}