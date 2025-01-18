package com.project.whist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "username")
    private String username;

    @Column(columnDefinition = "password")
    private String password;

    @Column(columnDefinition = "email")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<UserLogin> logins = new ArrayList<>();

    private Integer wins;

    private Integer losses;

    private Integer draws;

    private Integer totalGames;

    @Column(nullable = false)
    private Instant createdAt;

}