package com.e_learning.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_learning.entities.ForgetPassword;

public interface ForgetPasswordRepo extends JpaRepository<ForgetPassword, Integer> {
    Optional<ForgetPassword> findByPhnum(String phnum);
}

