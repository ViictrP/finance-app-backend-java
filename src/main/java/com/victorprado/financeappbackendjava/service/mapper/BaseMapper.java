package com.victorprado.financeappbackendjava.service.mapper;

import java.util.ArrayList;
import java.util.List;

public interface BaseMapper <D, E> {

  E toEntity(D d);
  D toDTO(E e);

  default List<D> toDTO(List<E> eList) {
    List<D> list = null;
    if (eList != null) {
      list = new ArrayList<>(eList.stream()
        .map(this::toDTO)
        .toList());
    }
    return list;
  }

  default List<E> toEntity(List<D> dList) {
    List<E> list = null;
    if (dList != null) {
      list = new ArrayList<>(dList.stream()
        .map(this::toEntity)
        .toList());
    }
    return list;
  }
}
