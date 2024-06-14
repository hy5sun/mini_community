package com.example.mini_community.service.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.domain.board.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.mini_community.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3Client amazonS3Client;

    public List<Image> uploadFiles(List<MultipartFile> files) {

        validateFileSize(files);

        List<Image> images = files.stream().map(file -> {
            try {
                return uploadFile(file);
            } catch (IOException e) {
                throw new BusinessException(FILE_UPLOAD_FAILED);
            }
        }).collect(Collectors.toList());

        return images;
    }

    private Image builderImage(String originalName, String savedName, String url) {
        return Image.builder().originalName(originalName).savedName(savedName).imageUrl(url).build();
    }

    private void validateFileSize(List<MultipartFile> files) {
        if (files.size() > 10) {
            throw new BusinessException(TOO_MANY_FILES);
        }
    }

    public Image uploadFile(MultipartFile file) throws IOException {
        try {
            String folderDir = bucket;
            String originalName = file.getOriginalFilename();
            String savedName = createSaveName(originalName);
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + savedName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(folderDir, savedName, file.getInputStream(), metadata);

            return builderImage(originalName, savedName, fileUrl);
        } catch (IOException e) {
            throw new BusinessException(FILE_UPLOAD_FAILED);
        }
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createSaveName(String originalName) {
        String ext = extractExt(originalName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalName) {
        int pos = originalName.lastIndexOf(".");
        return originalName.substring(pos + 1);
    }
}
