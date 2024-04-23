package com.simsolves.backend.main

import com.simsolves.backend.main.data.AppDB
import com.simsolves.backend.main.data.impl.exposed.*
import com.simsolves.backend.main.model.PuzzleCategory
import com.simsolves.backend.main.model.User
import com.simsolves.backend.main.model.error.Success
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.update

suspend fun main() {
  AppDB.initialize(
    jdbcUrl = SQLITE_URL,
    jdbcDriverClassName = SQLITE_DRIVER,
    username = "",
    password = ""
  ) {
    SchemaUtils.createMissingTablesAndColumns(Users, Rooms, Admins, Solves)
  }

  AppDB.query {
    when (val creationResult = UsersHandler.createUser(User(username = "lucas123", email = "abc@def.com", password = "123"))) {
      is Success -> {
        val adminId = Admins.insertAndGetId {
          it[relatedUserId] = creationResult.data
        }

        val roomId = Rooms.insertAndGetId {
          it[name] = "room123"
          it[relatedAdminId] = adminId.value
          it[puzzleCategory] = PuzzleCategory.PocketCube
          it[minimumTimeRequiredToJoin] = 60 * 1000L
        }

        Users.update({ Users.id eq creationResult.data }) {
          it[currentRoomId] = roomId.value
        }

        println("The created room ID: ${roomId.value}")
      }
    }
  }
}