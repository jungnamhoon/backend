package com.hkorea.skyisthelimit.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {

  PERSONAL("profile"),
  STUDY("study");
  
  private final String path;
}
