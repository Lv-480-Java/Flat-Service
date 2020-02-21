package com.softserve.maklertaboo.entity;

import com.softserve.maklertaboo.entity.photo.PhotoFlat;
import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Address address;

    @OneToMany
    private List<PhotoFlat> photoFlatList;

    @ManyToOne
    private User owner;

    @ManyToMany
    List<Tag> tagList;

    @OneToMany
    List<Order> order;

    @OneToMany
    List<CommentFlat> commentFlatList;

    @Column (nullable = false)
    private String description;
    @Column (nullable = false)
    private String title;
    private boolean isVisible;
    @Column (nullable = false)
    private double monthPrice;
}
