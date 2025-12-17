package org.example.movie.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    /**
     * Used for ACTOR roles (0 = main actor, 1 = second, 2 = third)
     * Can be null for DIRECTOR
     */
    private Integer creditOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Role() {}

    public Role(RoleType roleType, Movie movie, Person person) {
        this.roleType = roleType;
        this.movie = movie;
        this.person = person;
    }

    public Long getId() { return id; }

    public RoleType getRoleType() { return roleType; }
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Integer getCreditOrder() { return creditOrder; }
    public void setCreditOrder(Integer creditOrder) {
        this.creditOrder = creditOrder;
    }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Person getPerson() { return person; }
    public void setPerson(Person person) {
        this.person = person;
    }
}
