package com.hkorea.skyisthelimit.common.utils;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class ImageUtils {

  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
  private static final int MAX_WIDTH = 8000;
  private static final int MAX_HEIGHT = 8000;
  private static final int THUMBNAIL_SIZE = 1920;


  public static void validateImage(MultipartFile image) throws IOException {

    log.info("image size : {}", image.getSize());
    log.info("image contentType : {}", image.getContentType());

    validateFileSize(image.getSize());
    validateMimeType(image.getContentType());
    BufferedImage originalImage = ImageIO.read(image.getInputStream());
    validateResolution(originalImage);
  }

  public static byte[] createThumbnail(MultipartFile image) throws IOException {

    BufferedImage originalImage = ImageIO.read(image.getInputStream());
    String formatName = "png"; // 기본값
    String originalFilename = image.getOriginalFilename();

    if (originalFilename != null && originalFilename.contains(".")) {
      formatName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
    return createThumbnail(originalImage, formatName);
  }

  public static byte[] createThumbnail(BufferedImage originalImage, String formatName)
      throws IOException {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      Thumbnails.of(originalImage)
          .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
          .crop(Positions.CENTER)        // 중앙 크롭
          .outputQuality(1.0)            // 최대 화질
          .outputFormat(formatName)
          .toOutputStream(os);
      return os.toByteArray();
    }
  }

  private static void validateFileSize(long fileSize) {

    log.info("file size : {} max file size : {}", fileSize, MAX_FILE_SIZE);

    if (fileSize > MAX_FILE_SIZE) {
      log.info("file size : {} max file size : {}", fileSize, MAX_FILE_SIZE);
      throw new BusinessException(ErrorCode.INVALID_IMAGE_SIZE);
    }
  }

  private static void validateMimeType(String contentType) {
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new BusinessException(ErrorCode.INVALID_IMAGE_MIME_TYPE);
    }
  }

  private static void validateResolution(BufferedImage image) {

    log.info("image resolution {} x {}", image.getWidth(), image.getHeight());

    if (image.getWidth() > MAX_WIDTH || image.getHeight() > MAX_HEIGHT) {
      throw new BusinessException(ErrorCode.INVALID_IMAGE_RESOLUTION);
    }
  }

}
