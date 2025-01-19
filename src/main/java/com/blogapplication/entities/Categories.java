package com.blogapplication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer categoriesId;

    @Column(name="title")
    private String categoriesTitle;

    @Column(name = "description")
    private String categoryDescription;

    @OneToMany
    private List<Post>posts=new ArrayList<>();
}
