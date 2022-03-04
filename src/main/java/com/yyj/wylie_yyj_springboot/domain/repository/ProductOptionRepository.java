package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    @Query("select op from ProductOption op where op.pid = ?1 order by op.oid asc")
    List<ProductOption> findAllByPid(Long pid);
    void deleteAllByPid(Long pid);
}
