package com.cong.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
/**
 * @author cong
 * @date 2024/12/7 18:24
 */
@Service
@Slf4j
public class S3Service {


        private final S3Client s3Client;

        @Value("${aws.s3.bucketName}")
        private String bucketName;

    @Value("${aws.region}")
    private String region;

        public S3Service(
                @Value("${aws.region}")
                String region
        ) {
            this.s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .build();
        }

        public String generatePresignedUrl(String fileName, String contentType) {
            String key = "music/"+UUID.randomUUID().toString() + "_" + fileName;
            log.info(contentType);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(putObjectRequest)
                    .build();

            try (S3Presigner s3Presigner = S3Presigner.create()) {
                PresignedPutObjectRequest request = s3Presigner.presignPutObject(presignRequest);
                log.info(request.url().toString());
                return request.url().toString();
            }
        }

        public ListObjectsV2Response listMusicFiles() {
            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix("music/")
                    .maxKeys(10)
                    .build();

            return s3Client.listObjectsV2(listReq);
        }

        public String generateGetUrl(String key) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(1))
                    .getObjectRequest(getObjectRequest)
                    .build();

            try (S3Presigner s3Presigner = S3Presigner.builder().s3Client(s3Client).region(Region.of(region)).build()) {
                PresignedGetObjectRequest request = s3Presigner.presignGetObject(presignRequest);
                return request.url().toString();
            }
        }


    public String uploadFile(MultipartFile file) {
        String url = this.generatePresignedUrl(file.getOriginalFilename(), file.getContentType());
        useHttpClientToPutObject(url, file.getContentType());
        return url;
    }

    /**
     * Use the JDK HttpClient class (since v11) to upload a String,
     * but you can use any HTTP client.
     *
     * @param presignedUrl - The presigned URL.
     */
    public void useHttpClientToPutObject(String presignedUrl, String contentType) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            final HttpResponse<Void> response = httpClient.send(HttpRequest.newBuilder()
                            .uri(new URI(presignedUrl))
                            .header("Content-Type", contentType)
                            .PUT(HttpRequest.BodyPublishers
                                    .ofString("This text was uploaded as an object by using a presigned URL."))
                            .build(),
                    HttpResponse.BodyHandlers.discarding());
            log.info("HTTP response code is " + response.statusCode());
        } catch (S3Exception | IOException | URISyntaxException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
    // snippet-end:[presigned.java2.generatepresignedurl.main]
}
