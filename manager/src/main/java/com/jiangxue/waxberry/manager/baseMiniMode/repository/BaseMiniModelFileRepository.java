package com.jiangxue.waxberry.manager.baseMiniMode.repository;

import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseMiniModelFileRepository extends JpaRepository<BaseMiniModelFile,String>,JpaSpecificationExecutor {


    @Query(value = "SELECT bf.id, bf.waxberry_id, bf.file_id, bf.creator_id, bf.updater_id, bf.create_time, bf.update_time, ff.filename, ff.filesize " +
            "FROM mgr_baseminmodel_file bf " +
            "LEFT JOIN fs_file_desc ff ON bf.file_id = ff.pid WHERE bf.waxberry_id=:waxberryId", nativeQuery = true)
    List<Object[]> findByWaxberryId(@Param("waxberryId") String waxberryId);

}
