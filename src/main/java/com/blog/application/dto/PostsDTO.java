package com.blog.application.dto;

import com.blog.application.entity.posts;

import java.util.List;

public class PostsDTO {
    private List<posts> postsList;

    public List<posts> getPostsList() {
        return postsList;
    }

    public void setPostsList(List<posts> postsList) {
        this.postsList = postsList;
    }
}
