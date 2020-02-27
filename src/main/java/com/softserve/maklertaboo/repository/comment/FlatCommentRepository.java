package com.softserve.maklertaboo.repository.comment;

import com.softserve.maklertaboo.entity.comment.FlatComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatCommentRepository extends JpaRepository<FlatComment, Long> {
}