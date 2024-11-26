package com.blog.application.dao;

import com.blog.application.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDao extends JpaRepository<Login,Integer> {
}
