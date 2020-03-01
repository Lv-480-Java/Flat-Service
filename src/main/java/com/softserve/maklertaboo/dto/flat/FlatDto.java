package com.softserve.maklertaboo.dto.flat;

import com.softserve.maklertaboo.entity.Address;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FlatDto {

    private Long id;
    private Integer monthPrice;

    private String description;
    private String title;
    private String photoUrl;

    private String creationDate;
    private Address address;
    private List<String> tags;
}