package com.soulmates.valley.common.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile);
        return upload(uploadFile);
    }

    public List<String> upload(List<MultipartFile> multipartFile) {
        List<String> result = new LinkedList<>();
        for (MultipartFile file : multipartFile) {
            try {
                result.add(upload(file));
            } catch (IOException e) {
                throw new CustomException(ErrorEnum.UPLOAD_ERROR);
            }
        }
        return result;
    }

    private String upload(File uploadFile) {
        String uploadImageUrl = putS3(uploadFile);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile) {
        String newFileName = UUID.randomUUID().toString();
        amazonS3Client.putObject(new PutObjectRequest(bucket, newFileName, uploadFile));

        return amazonS3Client.getUrl(bucket, newFileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private File convert(MultipartFile file) throws IOException {
        String dirPath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        File convertFile = new File(dirPath.substring(0, dirPath.length() - 1) + file.getOriginalFilename());
        file.transferTo(convertFile);
        return convertFile;
    }
}
