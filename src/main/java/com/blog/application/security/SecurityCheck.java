package com.blog.application.security;

import com.blog.application.entity.posts;
import com.blog.application.service.AppServiceImpl;
import org.springframework.stereotype.Component;

@Component("securityCheck")
public class SecurityCheck {

    AppServiceImpl postservice=null;
    public SecurityCheck(AppServiceImpl postservice){
        this.postservice=postservice;
    }

    public boolean isValidAuthor(String loggedInUsername, int postId) {
        posts post = postservice.findPostsById(postId);
        System.out.println(post.getAuthor()+" "+loggedInUsername);
        return post != null && post.getAuthor().equals(loggedInUsername);
    }
}
