package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select img.path from Image img where img.pid = ?1 and img.thumb =?2 order by img.imgId asc")
    List<String> findPathByPidAndThumb(Long pid, int thumb);
    void deleteAllByPidAndThumb(Long pid, int thumb);
    List<Image> findAllByPidAndThumb(Long pid, int thumb);
}
