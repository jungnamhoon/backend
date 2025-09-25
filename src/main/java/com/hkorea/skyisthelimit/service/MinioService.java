package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.service.enums.ImageType;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioService {

  private final MinioClient minioClient;

  @Value("${minio.bucket}")
  private String bucketName;

  @Value("${minio.endpoint}")
  private String minioEndpoint;

  @Transactional
  public void deleteOldImage(String oldUrl)
      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException,
      ServerException, XmlParserException {

    String baseUrl = minioEndpoint + "/" + bucketName + "/";
    String oldObjectName = oldUrl.replace(baseUrl, "");

    if (isHaveImage(oldObjectName)) {
      minioClient.removeObject(
          RemoveObjectArgs.builder()
              .bucket(bucketName)
              .object(oldObjectName)
              .build()
      );
    }
  }

  public String uploadImage(ImageType type, String identifier, byte[] imageData,
      String originalFilename, String contentType)
      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException,
      NoSuchAlgorithmException, ServerException, XmlParserException {

    String objectName = type.getPath() + "/" + identifier + "_" + Instant.now().toEpochMilli() + "_"
        + originalFilename;

    try (ByteArrayInputStream bis = new ByteArrayInputStream(imageData)) {
      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(bucketName)
              .object(objectName)
              .stream(bis, imageData.length, -1)
              .contentType(contentType)
              .build()
      );
    }

    return minioEndpoint + "/" + bucketName + "/" + objectName;
  }

  private boolean isHaveImage(String fileName) {
    try {
      minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
      return true;
    } catch (Exception e) {
      return false;
    }
  }


}
