package com.jiangxue.waxberry.user.repository;

import com.jiangxue.waxberry.user.entity.User;
import com.jiangxue.waxberry.user.entity.UserProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProcessRepository extends JpaRepository<UserProcess, String>, JpaSpecificationExecutor<UserProcess> {

    @Query(" select a from UserProcess a where a.mobile = :mobile order by a.createdAt desc limit 1 ")
    Optional<UserProcess> findByMobile(String mobile);

    void deleteByMobile(String mobile);

    List<UserProcess> findByStatus(UserProcess.ProcessStatus status);
}
