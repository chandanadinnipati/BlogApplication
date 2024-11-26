package com.blog.application.dao;

import com.blog.application.entity.tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDao extends JpaRepository<tags,Integer> {

    @Query("SELECT s FROM tags s WHERE s.name = :data")
    tags findByName(@Param("data") String name);
}
