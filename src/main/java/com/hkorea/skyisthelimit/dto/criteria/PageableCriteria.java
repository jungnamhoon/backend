package com.hkorea.skyisthelimit.dto.criteria;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface PageableCriteria<T> extends Criteria<T> {

  int getPage();

  int getSize();

  String getSort();

  String getDirection();

  default Pageable toPageable() {
    return PageRequest.of(getPage(), getSize(),
        Sort.by(Sort.Direction.fromString(getDirection()), getSort()));
  }

}
