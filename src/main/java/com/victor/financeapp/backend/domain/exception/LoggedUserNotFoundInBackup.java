package com.victor.financeapp.backend.domain.exception;

import org.springframework.http.HttpStatus;

public class LoggedUserNotFoundInBackup extends CoreException{

  public LoggedUserNotFoundInBackup() {
    super(HttpStatus.BAD_REQUEST, "The logged user is not in the backup! You can only import your own data.");
  }
}
