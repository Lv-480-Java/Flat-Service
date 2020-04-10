package com.softserve.maklertaboo.entity;

import com.softserve.maklertaboo.entity.photo.PassportPhoto;
import com.softserve.maklertaboo.entity.user.User;
import lombok.Data;
import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table(name="passport")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String birthDate;
    private String birthPlace;
    private String passportType;
    private String nationality;
    private String authority;
    private String dateOfIssue;
    private String expirationDate;
    private String passportNumber;
    private Long identificationNumber;

    @OneToOne(cascade = CascadeType.DETACH, mappedBy = "passport")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PassportPhoto> passportPhotoList;

}
