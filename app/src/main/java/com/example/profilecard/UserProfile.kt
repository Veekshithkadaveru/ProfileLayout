package com.example.profilecard

data class UserProfile(val name:String, val status:Boolean, val drawableId:Int)
val userProfileList= arrayListOf(
    UserProfile(name="Veekshith Kadaveru",status = false,R.drawable.profilepicture),
    UserProfile(name = "Anonymous User",status = true,R.drawable.unknownguy),
)
