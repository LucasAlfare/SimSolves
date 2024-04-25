package com.simsolves.backend.main.model.dto

//@Serializable
data class RoomDTO(
  val id: Long = 1,
  val name: String,
  val currentRelatedAdminId: Long,
  val minimumTimeRequiredToJoin: Long,
  var chatRawContent: String = ""
)