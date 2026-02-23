package com.hkorea.skyisthelimit.common.infrastructure;

import com.hkorea.skyisthelimit.common.StorageService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService implements StorageService {

  private final MinioClient minioClient;

  @Value("${minio.bucket}")
  private String bucketName;

  @Value("${minio.endpoint}")
  private String minioEndpoint;

  @Override
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

  @Override
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

  @Override
  public String getBaseUrl() {
    return minioEndpoint + "/" + bucketName;
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
