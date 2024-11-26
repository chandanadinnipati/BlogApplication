package com.blog.application.service;

import com.blog.application.dao.CommentsDao;
import com.blog.application.dao.UserDao;
import com.blog.application.dao.PostDao;
import com.blog.application.dao.TagDao;
import com.blog.application.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AppServiceImpl implements AppService{
    private PostDao postDao;
    private TagDao tagDao;
    private CommentsDao commentsDao;
    private UserDao userDao;

    @Autowired
    AppServiceImpl(PostDao post, TagDao tag, CommentsDao comment, UserDao login){
        this.postDao =post;
        this.tagDao =tag;
        this.commentsDao =comment;
        this.userDao =login;
    }

    public void savePosts(posts post){
        postDao.save(post);
    }

    public List<posts> findAllPosts(){
        return postDao.findAll();
    }

    public posts findPostsById(int id){
     return postDao.findByIDWithTags(id);

    }
    public void deletePostById(int id){
        postDao.deleteById(id);
    }

    public Page<posts> findPostByAuthor(String name, Pageable pageable){
        return postDao.findByAuthorWithTags(name,pageable);
    }

    @Transactional
    public void savePostWithTags(posts post,String tagName){
        String[] tagNames = tagName.split(",");
        for(String tag:tagNames) {
            tag=tag.trim();
            tags t= tagDao.findByName(tag);
            if(t==null){
                t=new tags();
                t.setName(tag);
            }
            post.addTags(t);

        }
        postDao.save(post);
    }


    public Page<posts> findAllPostsByPage(Pageable pageable){
        return postDao.findAll(pageable);
    }

    public Page<posts> searchPostsByCategory(String author,String tag,String date,Pageable pageable){
        return postDao.findByCategory(author,tag,date,pageable);
    }

    public Page<posts> searchPostsByTextCategory(String text, String category, Pageable pageable){
        Page<posts> p=null;
        if(category.isEmpty()){
            p= postDao.searchBlogPosts(text,pageable);
        }
        else {
            p= postDao.findByTextAndCategory(category,text,pageable);
        }
        return p;
    }


    public Page<posts> sortPostsByPublishedDateAscending(List<posts> post, Pageable pageable) {
        Collections.sort(post, Comparator.comparing(posts::getPublishedAt));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), post.size());
        List<posts> pageContent = post.subList(start, end);
        return new PageImpl<>(pageContent, pageable, post.size());

    }

    public Page<posts> sortPostsByPublishedDateDescending(List<posts> post, Pageable pageable) {
        Collections.sort(post, Comparator.comparing(posts::getPublishedAt).reversed());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), post.size());
        List<posts> pageContent = post.subList(start, end);
        return new PageImpl<>(pageContent, pageable, post.size());
    }

    public tags findTagsByName(String name){
        return tagDao.findByName(name);
    }

    public void saveTags(tags tag){
        tagDao.save(tag);
    }


    public Comments findCommentById(int id){
        return commentsDao.findByIDWithposts(id);
    }

    public void saveComment(Comments c){
        commentsDao.save(c);
    }

    public void deleteCommentById(int id){
        commentsDao.deleteById(id);
    }

    public Page<posts> searchPostsByText(String text,Pageable pageable){
        return postDao.searchBlogPosts(text,pageable);
    }

    public void saveLogin(User user){
        user.setPassword("{noop}"+user.getPassword());
        userDao.save(user);
    }


}
