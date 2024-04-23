package com.simsolves.backend.main.data.impl.exposed

import com.simsolves.backend.main.ONE_DAY
import com.simsolves.backend.main.model.Penalty
import com.simsolves.backend.main.model.PuzzleCategory
import org.jetbrains.exposed.dao.id.LongIdTable

object Rooms : LongIdTable("Rooms") {
  val name = text("name").uniqueIndex()
  val relatedAdminId = long("related_admin_id").references(Admins.id)
  val minimumTimeRequiredToJoin = long("minimum_time_required_to_join")
  val puzzleCategory = enumeration<PuzzleCategory>("puzzle_category").default(PuzzleCategory.RubiksCube)
  val numUsers = integer("num_users").default(1)
  val duration = long("duration").default(ONE_DAY)
  val rawChatContent = largeText("chat_content").default("")

  // TODO: implement banned users list
}

// TODO: other custom user-related metadata
object Users : LongIdTable("Users") {
  val username = text("username").uniqueIndex()
  val email = text("email").uniqueIndex()
  val hashedPassword = text("hashed_password")
  val personalBestRelatedSolveId = long("personal_best_related_solve_id").references(Solves.id).nullable()

  // can be null; if null, then is not in any room
  val currentRoomId = long("current_room_id").references(Rooms.id).nullable()
}

object Admins : LongIdTable("Admins") {
  val relatedUserId = long("related_user_id").references(Users.id)
}

object Solves : LongIdTable("Solves") {
  val relatedUserId = long("related_user_id").references(Users.id)
  val time = long("time")
  val scramble = text("scramble")
  val penalty = enumeration<Penalty>("penalty").default(Penalty.Ok)
}