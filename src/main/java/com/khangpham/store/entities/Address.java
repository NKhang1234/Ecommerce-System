package com.khangpham.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder // Allows you to create objects fluently
@AllArgsConstructor // Makes the Builder happy (Object creation)
@NoArgsConstructor  // Makes Hibernate happy (Database storage)
@ToString
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zip")
    private String zip;

    @Column(name = "state")
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}