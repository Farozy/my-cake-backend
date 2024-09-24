package org.farozy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigInteger price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "image_url")
    private String imageUrl;

    private BigInteger stock;

    private BigDecimal discount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date created_at = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updated_at = new Date();

    @Column(name = "is_active")
    private Boolean isActive;

}
