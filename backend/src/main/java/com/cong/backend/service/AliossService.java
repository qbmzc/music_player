package com.cong.backend.service;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cong
 * @date 2024/12/10 14:35
 */
@Service
@Slf4j
public class AliossService {

    String endpoint = "https://oss-cn-shanghai.aliyuncs.com";

    @Value("${ali.oss.prefix}")
    private String bucketName;
    @Value("${ali.oss.prefix}")
    private String prefix;

    @Value("${file.host}")
    private String fileHost;


    public final OSS ossClient;

    @SneakyThrows
    public AliossService() {
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        this.ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .build();
    }


    public String uploadObject(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String key = prefix + File.separator + fileName;
        PutObjectRequest request = new PutObjectRequest(bucketName, key,
                file.getInputStream());

        ossClient.putObject(request);

        return fileHost + File.separator + key;
    }


    @SneakyThrows
    public String generatePreviewSignUrl(String filename, String contentType) {
        // 以华东1（杭州）的外网Endpoint为例，其它Region请按实际情况填写。

        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        // 填写Bucket名称，例如examplebucket。

        // 填写Object完整路径，例如exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = prefix + File.separator + filename;

        // 创建OSSClient实例
//        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        // 设置请求头。
        Map<String, String> headers = new HashMap<String, String>();
        /*// 指定Object的存储类型。
        headers.put(OSSHeaders.STORAGE_CLASS, StorageClass.Standard.toString());
        // 指定ContentType。
//        headers.put(OSSHeaders.CONTENT_TYPE, "text/txt");*/
        headers.put(OSSHeaders.CONTENT_TYPE, contentType);
        // 设置用户自定义元数据。
        Map<String, String> userMetadata = new HashMap<String, String>();
        /*userMetadata.put("key1","value1");
        userMetadata.put("key2","value2");*/

        URL signedUrl = null;
        try {
            // 指定生成的签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);

            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.PUT);
            // 设置过期时间。
            request.setExpiration(expiration);

            // 将请求头加入到request中。
            request.setHeaders(headers);
            // 添加用户自定义元数据。
            request.setUserMetadata(userMetadata);

            // 通过HTTP PUT请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);
            // 打印签名URL。
            System.out.println("signed url for putObject: " + signedUrl);
            return signedUrl.toString();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
        return null;
    }

    @SneakyThrows
    public static void putObjectWithHttp(String signedUrl, String pathName, Map<String, String> headers, Map<String, String> userMetadata) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPut put = new HttpPut(signedUrl);
            HttpEntity entity = new FileEntity(new File(pathName));
            put.setEntity(entity);
            // 如果生成签名URL时设置了header参数，例如用户元数据，存储类型等，则调用签名URL上传文件时，也需要将这些参数发送至服务端。如果签名和发送至服务端的不一致，会报签名错误。
            for (Map.Entry header : headers.entrySet()) {
                put.addHeader(header.getKey().toString(), header.getValue().toString());
            }
            for (Map.Entry meta : userMetadata.entrySet()) {
                // 如果使用userMeta，sdk内部会为userMeta拼接"x-oss-meta-"前缀。当您使用其他方式生成签名URL进行上传时，userMeta也需要拼接"x-oss-meta-"前缀。
                put.addHeader("x-oss-meta-" + meta.getKey().toString(), meta.getValue().toString());
            }

            httpClient = HttpClients.createDefault();

            response = httpClient.execute(put);

            System.out.println("返回上传状态码：" + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("使用网络库上传成功");
            }
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
            httpClient.close();
        }
    }

    public ObjectListing listMusicFiles() {

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setMaxKeys(20);
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        return objectListing;
    }
}
