package com.simsolves.backend.main.model

data class User(
  val id: Long = -1,
  val username: String,
  val email: String,
  val password: String,
  val personalBestSolveId: Long? = null,
  val currentRoomId: Long? = null
)