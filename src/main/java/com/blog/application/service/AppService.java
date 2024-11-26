package com.blog.application.service;

import com.blog.application.entity.Comments;
import com.blog.application.entity.Login;
import com.blog.application.entity.posts;
import com.blog.application.entity.tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppService {

    public void savePosts(posts post);
    public Page<posts> findAllPostsByPage(Pageable pageable);
    public posts findPostsById(int id);
    public void deletePostById(int id);
    public void savePostWithTags(posts post,String tagName);

    public Page<posts> searchPostsByText(String text,Pageable pageable);
    public Page<posts> searchPostsByCategory(String author,String tag,String date,Pageable pageable);
    public Page<posts> searchPostsByTextCategory(String text, String category, Pageable pageable);

    public Page<posts> sortPostsByPublishedDateAscending(List<posts> post, Pageable pageable);
    public Page<posts> sortPostsByPublishedDateDescending(List<posts> post, Pageable pageable);

    public tags findTagsByName(String name);
    public void saveTags(tags tag);

    public void saveComment(Comments c);
    public Comments findCommentById(int id);
    public void deleteCommentById(int id);

    public void saveLogin(Login l);
}
