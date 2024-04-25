package com.simsolves.backend.main.data.persistence.exposed

import com.simsolves.backend.main.data.persistence.AppDB
import com.simsolves.backend.main.data.persistence.AppServiceAdapter
import com.simsolves.backend.main.data.persistence.exposed.validator.UserCreationValidator
import com.simsolves.backend.main.model.User
import com.simsolves.backend.main.model.error.*
import org.jetbrains.exposed.sql.insertAndGetId

object UsersHandler : AppServiceAdapter() {

  override suspend fun createUser(user: User): AppResult<Long, AppError> {
    when (val res = UserCreationValidator().validateUserCreation(user)) {
      is Success -> {
        val id = AppDB.query {
          Users.insertAndGetId {
            it[username] = user.username
            it[email] = user.email
            it[hashedPassword] = user.password
            it[personalBestRelatedSolveId] = user.personalBestSolveId
            it[currentRoomId] = user.currentRoomId
          }
        }.value

        return Success(id)
      }

      is Failure -> return Failure(res.error)
    }

    return Failure(ValidationError.CanNotToCreate)
  }
}