package com.qchery.funda.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUserService extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
}