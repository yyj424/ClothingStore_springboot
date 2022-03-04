package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.entity.Image;
import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void saveFile(MultipartFile file, Long pid, int thumb) throws Exception {
        String fileName = file.getOriginalFilename();
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/images";
        Path path = Paths.get(fileName);
        if (!path.toFile().isFile()) {
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);
        }
        String filePath = "/images/" + fileName;
        Image image = new Image(fileName, filePath, pid, thumb);
        imageRepository.saveAndFlush(image);
    }

    public void saveMainThumbNail(MultipartFile thumb, Long pid) throws Exception {
        saveFile(thumb, pid, 2);
    }

    public void saveThumbNail(List<MultipartFile> thumbnails, Long pid) throws Exception {
        for (MultipartFile thumb : thumbnails) {
            saveFile(thumb, pid, 1);
        }
    }

    public void saveDetailImage(List<MultipartFile> detailImages, Long pid) throws Exception {
        for (MultipartFile detail : detailImages) {
            saveFile(detail, pid, 0);
        }
    }

    public List<String> findPathByPidAndThumb(Long pid, int thumb) {
        return imageRepository.findPathByPidAndThumb(pid, thumb);
    }

    public List<Image> findAllByPidAndThumb(Long pid, int thumb) {
        return imageRepository.findAllByPidAndThumb(pid, thumb);
    }

    public void deleteAllByPidAndThumb(Long pid, int thumb) {
        imageRepository.deleteAllByPidAndThumb(pid, thumb);

        //파일 자체 삭제하는 코드 추가
    }

//    public void saveThumbNail(List<MultipartFile> thumbnails) throws Exception {
//        for (MultipartFile thumb : thumbnails) {
//            String originName = thumb.getOriginalFilename();
//            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/images";
//            SimpleDateFormat dateFormat = new SimpleDateFormat("YYMMddHHmmss");
//            String date = dateFormat.format(System.currentTimeMillis());
//            String fileName = date + "_" + originName;
//            File saveFile = new File(projectPath, fileName);
//            thumb.transferTo(saveFile);
//        }
//    }
}
