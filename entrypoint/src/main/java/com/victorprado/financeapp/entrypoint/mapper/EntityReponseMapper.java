package com.victorprado.financeapp.entrypoint.mapper;


public interface EntityReponseMapper<E, R> {
  R toResponse(E e);
  E toEntity(R r);
}
