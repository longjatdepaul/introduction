package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Person implements Serializable {
    private final UUID id;
    @NotBlank
    private final String name;
    private final int age;
    private final boolean employed;
    private final Date hireDate;

    public Person(@JsonProperty("id") UUID id,
                  @JsonProperty("name") String name,
                  @JsonProperty("age") int age,
                  @JsonProperty("employed") boolean employed,
                  @JsonProperty("hireDate") Date hireDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.employed = employed;
        this.hireDate = hireDate;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isEmployed() {
        return employed;
    }

    public Date getHireDate() {
        return hireDate;
    }
}
