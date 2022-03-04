package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategory(Pageable pageable, int category);
    Page<Product> findByNameContainingIgnoreCase(Pageable pageable, String keyword);
    Page<Product> findByNameContainingIgnoreCaseAndCategory(Pageable pageable, String keyword, int category);
    @Query(value="select PID_SEQ.CURRVAL from DUAL", nativeQuery=true)
    Long findLastPid();
}
