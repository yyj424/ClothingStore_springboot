package com.yyj.wylie_yyj_springboot.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Image {
    @Id
    @Column(name = "image_id")
    @SequenceGenerator(name = "imgid_seq_generator", sequenceName = "imgid_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imgid_seq_generator")
    private Long imgId;
    @Column(name = "imagename")
    private String name;
    @Column(name = "imagepath")
    private String path;
    @Column(name = "product_id")
    private Long pid;
    @Column(name = "thumbnail")
    private int thumb;

    public Image(String name, String path, Long pid, int thumb) {
        this.name = name;
        this.path = path;
        this.pid = pid;
        this.thumb = thumb;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }
}

