package com.simsolves.backend.main.data.persistence

import com.simsolves.backend.main.model.User
import com.simsolves.backend.main.model.error.AppError
import com.simsolves.backend.main.model.error.AppResult
import com.simsolves.backend.main.model.error.Failure
import com.simsolves.backend.main.model.error.ValidationError

interface AppService {
  suspend fun createUser(user: User): AppResult<Long, AppError>
}

abstract class AppServiceAdapter : AppService {
  override suspend fun createUser(user: User): AppResult<Long, AppError> {
    return Failure(ValidationError.CanNotToCreate)
  }
}