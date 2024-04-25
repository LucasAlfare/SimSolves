package com.simsolves.backend.main.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.simsolves.backend.main.model.User
import com.simsolves.backend.main.model.dto.RoomDTO

object FirebaseHandler {

  private val instance = FirebaseDatabase.getInstance()
  private val roomsRef = instance.getReference("rooms")

  fun createRoom(roomDTO: RoomDTO) {
    // assuming room specs are validated...
    // assuming we are pushing to Firebase after persisting to the main DB...
    val createdRoom = roomsRef.child(roomDTO.id.toString())

    val name = createdRoom.child("name")
    val currentRelatedAdminId = createdRoom.child("currentRelatedAdminId")
    val chatRawContent = createdRoom.child("chatRawContent")
    val usersIds = createdRoom.child("usersIds")

    name.setValue(roomDTO.name, null)
    currentRelatedAdminId.setValue(roomDTO.currentRelatedAdminId, null)
    chatRawContent.setValue(roomDTO.chatRawContent, null)

    createdRoom.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        // TODO: invoke our main API services to update based on which key was modified (when applicable)
        println(snapshot.value)
      }

      override fun onCancelled(error: DatabaseError?) {
        /* pass */
      }
    })
  }

  fun removeRoomById(roomId: Long) {
    val roomRef = roomsRef.child(roomId.toString())
    roomRef.removeValue(null)
  }

  fun removeUserById(userId: Long) {
    // e implemente essa aqui também
  }

  fun insertUserIntoItsRoom(user: User) {
    val roomRef = roomsRef.child(user.currentRoomId.toString())
    val usersIdsRef = roomRef.child("usersIds")
    val userRef = usersIdsRef.child(user.id.toString())
    userRef.child("solvingState").setValue(false, null)

    userRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        println("changes happened to the user [${user.id}]: [${snapshot}]")
      }

      override fun onCancelled(error: DatabaseError?) {

      }
    })
  }
}