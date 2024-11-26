package com.blog.application.dao;

import com.blog.application.entity.posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDao extends JpaRepository<posts,Integer> {

    @Query("SELECT s FROM posts s JOIN FETCH s.tags WHERE s.id = :data")
    posts findByIDWithTags(@Param("data") int id);

    @Query("SELECT s FROM posts s JOIN FETCH s.tags WHERE s.author = :name")
    Page<posts> findByAuthorWithTags(@Param("name") String author,Pageable pageable);

    Page<posts> findAll(Pageable pageable);

    @Query("SELECT b FROM posts b JOIN FETCH b.tags WHERE " +
                "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                "LOWER(b.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                "LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                "EXISTS (SELECT t FROM b.tags t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<posts> searchBlogPosts(@Param("query") String query,Pageable pageable);

    @Query("SELECT p FROM posts p JOIN FETCH p.tags WHERE p.author = :data")
    Page<posts> findByAuthor(@Param("data") String fieldValue,Pageable pageable);

    @Query("SELECT p FROM posts p JOIN FETCH p.tags WHERE LOWER(CAST(p.publishedAt AS string)) LIKE LOWER(CONCAT('%', :data, '%'))")
    Page<posts> findByPublishedDate(@Param("data") String fieldValue, Pageable pageable);

    @Query("SELECT p FROM posts p JOIN FETCH p.tags t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :data, '%'))")
    Page<posts> findByTag(@Param("data") String fieldValue, Pageable pageable);

    @Query("SELECT p FROM posts p JOIN FETCH p.tags t WHERE " +
            "('author' = :category AND LOWER(p.author) LIKE LOWER(CONCAT('%', :data, '%'))) OR " +
            "('published' = :category AND LOWER(CAST(p.publishedAt AS string)) LIKE LOWER(CONCAT('%', :data, '%'))) OR " +
            "('tags' = :category AND LOWER(t.name) LIKE LOWER(CONCAT('%', :data, '%')))")
    Page<posts> findByTextAndCategory(@Param("category") String category,
                                      @Param("data") String text,
                                      Pageable pageable);

    @Query("SELECT p FROM posts p JOIN FETCH p.tags t WHERE " +
            "(:author IS NULL OR LOWER(p.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:date IS NULL OR LOWER(CAST(p.publishedAt AS string)) LIKE LOWER(CONCAT('%', :date, '%'))) AND " +
            "(:tag IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :tag, '%')))")
    Page<posts> findByCategory(@Param("author") String author, @Param("tag") String tag, @Param("date") String date,Pageable pageable);


}
