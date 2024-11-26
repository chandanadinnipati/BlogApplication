package com.blog.application.controller;

import com.blog.application.entity.Comments;
import com.blog.application.entity.Login;
import com.blog.application.entity.posts;
import com.blog.application.entity.tags;
import com.blog.application.service.AppService;
import com.blog.application.dto.PostsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class blogController {
    PostsDTO postsDTO = new PostsDTO();
    AppService appService;

    @Autowired
    public blogController(AppService postservice) {
        this.appService = postservice;
    }

    @GetMapping("/")
    public String getPosts(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<posts> postPage = appService.findAllPostsByPage(pageable);
        postsDTO.setPostsList(postPage.getContent());
        model.addAttribute("postPage", postPage);
        model.addAttribute("postData", postsDTO);
        model.addAttribute("searchbar",false);
        return "home";
    }

    @RequestMapping("/sort")
    public String sort(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       @RequestParam("sort") String st,
                       @ModelAttribute("postData") PostsDTO p) {
        List<posts> postsList = p.getPostsList();
        Pageable pageable = PageRequest.of(page, size);
        Page<posts> postPage = null;
        if (st.equals("desc")) {
            Collections.sort(postsList, Comparator.comparing(posts::getPublishedAt).reversed());
            postPage = appService.sortPostsByPublishedDateDescending(postsList, pageable);
        } else {
            Collections.sort(postsList, Comparator.comparing(posts::getPublishedAt));
            postPage = appService.sortPostsByPublishedDateAscending(postsList, pageable);
        }
        postsDTO.setPostsList(postPage.getContent());
        model.addAttribute("postData", postsDTO);
        model.addAttribute("postPage", postPage);
        model.addAttribute("searchbar",false);
        return "home";
    }

    @GetMapping("/filter-search")
    public String dispalyFilterSearch(Model mymodel,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<posts> postPage = appService.findAllPostsByPage(pageable);
        postsDTO.setPostsList(postPage.getContent());
        mymodel.addAttribute("postPage", postPage);
        mymodel.addAttribute("postData", postsDTO);
        mymodel.addAttribute("searchbar",true);
        return "home";
    }

    @GetMapping("/filter")
    public String filterData(Model mymodel,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "author",defaultValue = "") String author,
                             @RequestParam(value = "tag",defaultValue = "") String tag,
                             @RequestParam(value = "publishedAt",defaultValue = "") String date)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<posts> postPage = appService.searchPostsByCategory(author,tag,date,pageable);
        postsDTO.setPostsList(postPage.getContent());
        mymodel.addAttribute("postPage", postPage);
        mymodel.addAttribute("postData", postsDTO);
        mymodel.addAttribute("searchbar",true);
        return "home";
    }
    @RequestMapping("/search")
    public String searchByText(Model mymodel,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam("search") String search)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<posts> postPage = appService.searchPostsByText(search,pageable);
        postsDTO.setPostsList(postPage.getContent());
        mymodel.addAttribute("postPage", postPage);
        mymodel.addAttribute("postData", postsDTO);
        mymodel.addAttribute("searchbar",false);
        return "home";
    }

    @GetMapping("/post/{id}")
    public String blogPost(@PathVariable("id") int id, Model myModel) {
        posts p = appService.findPostsById(id);
        myModel.addAttribute("postdata", p);
        myModel.addAttribute("commentsList", p.getComment());
        return "blog";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_AUTHOR')" +
            " and @securityCheck.isValidAuthor(authentication.name, #id))")
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable int id) {
        appService.deletePostById(id);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_AUTHOR')" +
            " and @securityCheck.isValidAuthor(authentication.name, #id))")
    @GetMapping("/update/{id}")
    public String updateById(@PathVariable int id, Model mymodel, Authentication authentication) {
        boolean isAuthor = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_AUTHOR")) {
                isAuthor = true;
                break;
            }
        }
        posts p = appService.findPostsById(id);
        String tags = "";
        boolean first = true;
        for (tags t : p.getTags()) {
            if (!first) {
                tags += ",";
            }
            tags += t.getName();
            first = false;
        }
        mymodel.addAttribute("isAuthor", isAuthor);
        mymodel.addAttribute("tagList", tags);
        mymodel.addAttribute("blog", p);
        return "createPost";
    }

    @RequestMapping("/createblog")
    public String createBlog(Model mymodel, Authentication authentication) {
        boolean isAuthor = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_AUTHOR")) {
                isAuthor = true;
                break;
            }
        }
        posts p = new posts();
        p.setTags(new ArrayList<>());
        mymodel.addAttribute("isAuthor", isAuthor);
        mymodel.addAttribute("blog", p);
        mymodel.addAttribute("tagList", "");
        return "createPost";
    }


    @PostMapping("/create")
    public String create(@ModelAttribute("blog") posts p,
                         @RequestParam("string") String tag,
                         @RequestParam("isAuthor") boolean isAuthor,
                         @RequestParam(value = "publish", defaultValue = "false") String publish,
                         Authentication authentication) {
        if (isAuthor) {
            p.setAuthor(authentication.getName());
        }
        if (publish.equals("true")) {
            p.setPublished(true);
            p.setPublishedAt(new Date(System.currentTimeMillis()));
        }
        if (p.getPublishedAt() == null) {
            p.setPublishedAt(new Date(System.currentTimeMillis()));
        }
        appService.savePostWithTags(p, tag);
        return "redirect:/";
    }
    
    @GetMapping("/comment/{id}")
    public String createComment(@PathVariable int id, Model mymodel) {
        Comments c = new Comments();
        mymodel.addAttribute("commentdata", c);
        mymodel.addAttribute("post_id", id);
        return "comment";
    }

    @PostMapping("/savecomment/{id}")
    public String saveComment(@ModelAttribute("commentdata") Comments c, @PathVariable("id") int post_id) {
        posts p = appService.findPostsById(post_id);
        c.setPost(p);
        appService.saveComment(c);
        return "redirect:/post/" + c.getPost().getId();
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR') and @securityCheck.isValidAuthor(authentication.name, #postId)")
    @GetMapping("/comment/update/{id}/{post_id}")
    public String updateComment(@PathVariable int id, Model myModel, @PathVariable("post_id") int postId) {
        Comments c = appService.findCommentById(id);
        myModel.addAttribute("commentdata", c);
        myModel.addAttribute("id", c.getPost().getId());
        return "comment";
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR') and @securityCheck.isValidAuthor(authentication.name, #postId)")
    @GetMapping("/comment/delete/{id}/{post_id}")
    public String deleteComment(@PathVariable("id") int id, @PathVariable("post_id") int postId) {
        int post_id = appService.findCommentById(id).getPost().getId();
        appService.deleteCommentById(id);
        return "redirect:/post/" + post_id;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        Login l = new Login();
        model.addAttribute("login", l);
        return "CreateUser";
    }

    @GetMapping("/save-login")
    public String saveLogin(@ModelAttribute("login") Login l) {
        l.setPassword("{noop}" + l.getPassword());
        System.out.println(l);
        appService.saveLogin(l);
        return "redirect:/login";
    }

}
