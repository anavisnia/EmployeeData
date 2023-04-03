package com.example.employeedata.entity;

import com.example.employeedata.enums.DevLanguage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "teamSize")
    private Integer teamSize;

    @Column(nullable = false, name = "customer")
    private String customer;

    @Column(nullable = false, name = "terminationDate")
    private ZonedDateTime terminationDate;

    @Column(name = "completionDate")
    private ZonedDateTime completionDate;

    @Column(nullable = false, name = "devLanguage")
    private DevLanguage devLanguage;

    @Column(nullable = false)
    private ZonedDateTime modificationDate;

    @ManyToMany(mappedBy = "projects")
    @JsonBackReference
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Employee> employees = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Project project = (Project) o;
        return Objects.equals(getTitle(), project.getTitle()) &&
                Objects.equals(getCustomer(), project.getCustomer()) &&
                Objects.equals(getDevLanguage(), project.getDevLanguage());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
