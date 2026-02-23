package com.hkorea.skyisthelimit.common;

import com.hkorea.skyisthelimit.service.enums.ImageType;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface StorageService {

  void deleteOldImage(String oldUrl)
      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException,
      ServerException, XmlParserException;

  String uploadImage(ImageType type, String identifier, byte[] imageData,
      String originalFilename, String contentType)
      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException,
      NoSuchAlgorithmException, ServerException, XmlParserException;

  String getBaseUrl();
}
