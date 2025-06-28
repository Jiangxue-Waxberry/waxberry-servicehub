package com.jiangxue.waxberry.user.repository;

import com.jiangxue.waxberry.user.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByMobile(String mobile);
    Optional<User> findByEmail(String email);

    Optional<Object> findByLoginname(String loginname);

    @Query(value = " SELECT COUNT(1) FROM User a WHERE a.email = :email AND a.mobile != :mobile ")
    int findCheckUserEmail(@Param("mobile")String mobile, @Param("email")String email);

    @Query(value = " SELECT COUNT(1) FROM User a WHERE a.mobile != :mobile AND a.loginname = :loginName ")
    int findCheckUserLoginName(@Param("mobile")String mobile, @Param("loginName")String loginName);
}
