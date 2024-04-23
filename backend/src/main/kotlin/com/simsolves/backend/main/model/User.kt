package com.simsolves.backend.main.model

data class User(
  val username: String,
  val email: String,
  val password: String,
  val personalBestSolveId: Long? = null,
  val currentRoomId: Long? = null
)