package org.example.movie.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    protected Person() {}

    public Person(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Role> getRoles() { return roles; }

    public void addRole(Role role) {
        roles.add(role);
        role.setPerson(this);
    }
}

