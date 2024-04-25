package com.simsolves.backend.main.data.persistence.exposed.validator

import com.simsolves.backend.main.data.persistence.AppDB
import com.simsolves.backend.main.data.persistence.AppValidatorAdapter
import com.simsolves.backend.main.data.persistence.exposed.Users
import com.simsolves.backend.main.model.User
import com.simsolves.backend.main.model.error.AppResult
import com.simsolves.backend.main.model.error.Failure
import com.simsolves.backend.main.model.error.Success
import com.simsolves.backend.main.model.error.ValidationError
import org.jetbrains.exposed.sql.selectAll

class UserCreationValidator : AppValidatorAdapter() {

  override suspend fun validateUserCreation(user: User): AppResult<Boolean, ValidationError> {
    if (emailExists(user.email)) {
      return Failure(ValidationError.CanNotToCreate)
    }

    if (usernameExists(user.username)) {
      return Failure(ValidationError.CanNotToCreate)
    }

    return Success(true)
  }

  private suspend fun emailExists(email: String) = !AppDB.query {
    Users.selectAll().where { Users.email eq email }.empty()
  }

  private suspend fun usernameExists(username: String) = !AppDB.query {
    Users.selectAll().where { Users.username eq username }.empty()
  }
}