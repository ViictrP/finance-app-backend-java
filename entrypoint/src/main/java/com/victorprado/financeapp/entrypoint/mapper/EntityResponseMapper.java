package com.victorprado.financeapp.entrypoint.mapper;


public interface EntityResponseMapper<E, R, T> {
  R toResponse(E e);
  E toEntity(R r);

  E fromRequest(T t);
}
