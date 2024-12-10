package com.cong.backend.controller;

import com.aliyun.oss.model.ObjectListing;
import com.cong.backend.service.AliossService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cong
 * @date 2024/12/10 14:34
 */
@RestController
@RequestMapping("api/alioss")
public class AliossController {


    @Value("${file.host}")
    private String fileHost;

    @Autowired
    private AliossService ossService;

    @PostMapping(value = "/upload/file")
    public Map<String, String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        String url = ossService.uploadObject(file);
        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);
        return data;
    }

    @GetMapping("/music/list")
    public List<S3Controller.MusicFile> listMusic() {
        ObjectListing objectListing = ossService.listMusicFiles();
        return objectListing.getObjectSummaries().stream().filter(o -> StringUtils.isNotBlank(FilenameUtils.getExtension(o.getKey())))
                .map(obj -> new S3Controller.MusicFile(
                        fileHost + File.separator + obj.getKey(),
                        "cong",
                        FilenameUtils.getName(obj.getKey()),
                        "http://test-fsservice.oss-cn-shanghai.aliyuncs.com/fs/test/2024/202412091544424.jpg"
                )).collect(Collectors.toList());
    }

}
