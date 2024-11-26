package com.blog.application.dao;

import com.blog.application.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentsDao extends JpaRepository<Comments,Integer> {
    @Query("SELECT s FROM Comments s JOIN FETCH s.post WHERE s.id = :data")
    Comments findByIDWithposts(@Param("data") int id);

}
