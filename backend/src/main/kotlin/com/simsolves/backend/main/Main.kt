package com.simsolves.backend.main

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.simsolves.backend.main.data.firebase.FirebaseHandler
import com.simsolves.backend.main.data.persistence.AppDB
import com.simsolves.backend.main.data.persistence.exposed.Admins
import com.simsolves.backend.main.data.persistence.exposed.Rooms
import com.simsolves.backend.main.data.persistence.exposed.Solves
import com.simsolves.backend.main.data.persistence.exposed.Users
import com.simsolves.backend.main.model.User
import com.simsolves.backend.main.model.dto.RoomDTO
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SchemaUtils
import java.io.FileInputStream

suspend fun main() {
  runCatching { initDatabase() }
  runCatching { initFirebase() }
  runCatching { initWebserver() }
}

private fun initDatabase() {
  AppDB.initialize(
    jdbcUrl = SQLITE_URL,
    jdbcDriverClassName = SQLITE_DRIVER,
    username = "",
    password = ""
  ) {
    SchemaUtils.createMissingTablesAndColumns(Users, Rooms, Admins, Solves)
  }
}

private suspend fun initFirebase() {
  val serviceAccount = withContext(Dispatchers.IO) {
    FileInputStream("apenas-um-teste-af4bb-firebase-adminsdk-r6cn5-54b238ab47.json")
  }

  val options = FirebaseOptions.builder()
    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    .setDatabaseUrl("https://apenas-um-teste-af4bb-default-rtdb.firebaseio.com/")
    .build()

  FirebaseApp.initializeApp(options)

  FirebaseHandler.createRoom(RoomDTO(1, "criei bonito", 1L, 60_000L))
  FirebaseHandler.createRoom(RoomDTO(2, "essa eu criei depois hehe", 1L, 60_000L))

  var s = System.currentTimeMillis()
  var e = 0
  while (System.currentTimeMillis() - s < 3000) {
  }

  FirebaseHandler.insertUserIntoItsRoom(User(1, "", "", "", currentRoomId = 2))

  s = System.currentTimeMillis()
  e = 0
  while (System.currentTimeMillis() - s < 5000) {
  }

//
//  FirebaseHandler.removeUserById(1L)
}

private fun initWebserver() {
  embeddedServer(Netty, port = 9999) {
    routing {
      get("/") {
        call.respondText("hello")
      }
    }
  }.start(true)
}