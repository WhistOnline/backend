package com.project.whist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
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
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(columnDefinition = "user_name")
    private String userName;

    @Column(columnDefinition = "password")
    private String password;

    @Column(columnDefinition = "email")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<UserLogin> logins = new ArrayList<>();

}