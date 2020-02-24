package com.softserve.maklertaboo.entity.photo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softserve.maklertaboo.entity.comment.Comment;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CommentPhoto extends PhotoAbstract {

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Comment comment;
}