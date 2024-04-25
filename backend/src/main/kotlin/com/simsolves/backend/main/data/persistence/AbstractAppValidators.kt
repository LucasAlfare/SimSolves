package com.simsolves.backend.main.data.persistence

import com.simsolves.backend.main.model.User
import com.simsolves.backend.main.model.error.AppResult
import com.simsolves.backend.main.model.error.Failure
import com.simsolves.backend.main.model.error.ValidationError

interface AppValidator {

  suspend fun validateUserCreation(user: User): AppResult<Boolean, ValidationError>
}

abstract class AppValidatorAdapter : AppValidator {

  override suspend fun validateUserCreation(user: User): AppResult<Boolean, ValidationError> {
    return Failure(ValidationError.CanNotToCreate)
  }
}