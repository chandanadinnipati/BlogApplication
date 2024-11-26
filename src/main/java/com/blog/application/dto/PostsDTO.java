package com.blog.application.dto;

import com.blog.application.entity.posts;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostsDTO {
    private List<posts> postsList;

    public List<posts> getPostsList() {
        return postsList;
    }

    public void setPostsList(List<posts> postsList) {
        this.postsList = postsList;
    }
}
