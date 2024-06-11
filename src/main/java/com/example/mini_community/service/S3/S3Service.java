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
import java.util.UUID;

import static com.example.mini_community.common.exception.ErrorCode.FILE_UPLOAD_FAILED;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3Client amazonS3Client;

    public String uploadFile(Image image, MultipartFile file, UUID boardId) throws IOException {
        try {
            String folderDir = bucket + "/" + boardId;
            String savedName = image.getSaveName();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + boardId + "/" + savedName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(folderDir, savedName, file.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            throw new BusinessException(FILE_UPLOAD_FAILED);
        }
    }

    public void deleteFile(String boardId, String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket + "/" + boardId, fileName));
    }
}
