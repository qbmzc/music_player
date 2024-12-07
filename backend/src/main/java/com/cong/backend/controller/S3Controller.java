package com.cong.backend.controller;

import com.cong.backend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
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
    public String getPresignedUrl(@RequestParam String fileName, @RequestParam String fileType) {
        return s3Service.generatePresignedUrl(fileName, fileType);
    }

    @GetMapping("/music/list")
    public List<MusicFile> listMusic() {
        List<S3Object> objects = s3Service.listMusicFiles().contents();
        return objects.stream().map(obj -> new MusicFile(
                obj.key(),
                s3Service.generateGetUrl(obj.key()),
                obj.size(),
                obj.lastModified()
        )).collect(Collectors.toList());
    }

    // DTO类
    public static class MusicFile {
        private String key;
        private String url;
        private long size;
        private String lastModified;

        public MusicFile(String key, String url, long size, java.time.Instant lastModified) {
            this.key = key;
            this.url = url;
            this.size = size;
            this.lastModified = lastModified.toString();
        }

        // Getters 和 Setters
        public String getKey() { return key; }
        public String getUrl() { return url; }
        public long getSize() { return size; }
        public String getLastModified() { return lastModified; }

        public void setKey(String key) { this.key = key; }
        public void setUrl(String url) { this.url = url; }
        public void setSize(long size) { this.size = size; }
        public void setLastModified(String lastModified) { this.lastModified = lastModified; }
    }
}
