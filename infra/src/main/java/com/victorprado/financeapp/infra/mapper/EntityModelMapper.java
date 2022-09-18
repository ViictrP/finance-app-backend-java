package com.victorprado.financeapp.infra.mapper;

public interface EntityModelMapper<E, M> {
  E toEntity(M m);
  M toModel(E e);
}
