package com.jiangxue.waxberry.fileserver.repository;

import com.jiangxue.waxberry.fileserver.entity.FileDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileDesc, Long> {

    Optional<FileDesc> findByMd5(String md5);

}
