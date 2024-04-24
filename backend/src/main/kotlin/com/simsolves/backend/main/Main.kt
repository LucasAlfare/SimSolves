package com.simsolves.backend.main

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.simsolves.backend.main.data.AppDB
import com.simsolves.backend.main.data.impl.exposed.Admins
import com.simsolves.backend.main.data.impl.exposed.Rooms
import com.simsolves.backend.main.data.impl.exposed.Solves
import com.simsolves.backend.main.data.impl.exposed.Users
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
    FileInputStream("TODO.json")
  }

  val options = FirebaseOptions.builder()
    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    .setDatabaseUrl("https://TODO-ID-POJECT.firebaseio.com/")
    .build()

  FirebaseApp.initializeApp(options)

  // TODO: setup needed fields for real time flow here
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