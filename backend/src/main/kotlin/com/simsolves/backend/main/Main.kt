package com.simsolves.backend.main

import com.simsolves.backend.main.data.AppDB
import com.simsolves.backend.main.data.impl.exposed.Admins
import com.simsolves.backend.main.data.impl.exposed.Rooms
import com.simsolves.backend.main.data.impl.exposed.Solves
import com.simsolves.backend.main.data.impl.exposed.Users
import com.simsolves.backend.main.model.PuzzleCategory
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

  /*
  Simulating:
  - creation of a user;
  - creation of a room:
    - for this is necessary to create an admin;
      - for this is necessary to have a userId in hand;
  - the update of user current room value
   */
  AppDB.query {
    val userId = Users.insertAndGetId {
      it[username] = "username123"
      it[email] = "user@abc.com"
      it[hashedPassword] = "123"
      it[personalBestRelatedSolveId] = null
      it[currentRoomId] = null
    }

    val adminId = Admins.insertAndGetId {
      it[relatedUserId] = userId.value
    }

    val roomId = Rooms.insertAndGetId {
      it[name] = "room123"
      it[relatedAdminId] = adminId.value
      it[puzzleCategory] = PuzzleCategory.PocketCube
      it[minimumTimeRequiredToJoin] = 60 * 1000L
    }

    Users.update({ Users.id eq userId.value }) {
      it[currentRoomId] = roomId.value
    }

    println("The created room ID: ${roomId.value}")
  }
}