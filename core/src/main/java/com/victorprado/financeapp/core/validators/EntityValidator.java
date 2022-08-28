package com.victorprado.financeapp.core.validators;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import com.victorprado.financeapp.core.entities.Entity;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class EntityValidator {

  public static final Validator validator;

  static {
    try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  /**
   * Validate an entity based on it's annotations
   *
   * @param entity entity which will be validated
   * @return true if valid, otherwise, false
   */
  public static boolean validate(Entity entity) {
    Set<ConstraintViolation<Entity>> violations = validator.validate(entity);
    return violations.isEmpty();
  }
}
