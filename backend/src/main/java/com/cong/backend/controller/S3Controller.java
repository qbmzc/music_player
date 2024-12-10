package com.cong.backend.controller;

import com.cong.backend.service.S3Service;
import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cong
 * @date 2024/12/7 17:43
 */
@RestController
@RequestMapping("api")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @GetMapping("/upload/presigned-url")
    public Map<String, String> getPresignedUrl(@RequestParam String fileName, @RequestParam String fileType) {
        String url = s3Service.generatePresignedUrl(fileName, fileType);
        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);
        return data;
    }

    @PostMapping(value = "/upload/file")
    public Map<String, String> uploadFile(@RequestPart MultipartFile file) {
        String url = s3Service.uploadFile(file);
        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);
        return data;
    }

    @GetMapping("/music/list")
    public List<MusicFile> listMusic() {
        List<S3Object> objects = s3Service.listMusicFiles().contents();
        return objects.stream().filter(o -> StringUtils.isNotBlank(FilenameUtils.getExtension(o.key())))
                .map(obj -> new MusicFile(
                s3Service.generateGetUrl(obj.key()),
                        "cong",
                        FilenameUtils.getName(obj.key()),
                        "http://test-fsservice.oss-cn-shanghai.aliyuncs.com/fs/test/2024/202412091544424.jpg"
                )).collect(Collectors.toList());
    }

    /**
     * title: item.title,
     * artist: item.artist,
     * src: item.src,
     * pic: item.pic
     */
    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MusicFile {
        // Getters 和 Setters
        private String url;
        private String author;
        private String title;
        private String pic;
    }
}
