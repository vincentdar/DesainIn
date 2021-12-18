package com.example.cobachatapp

class StaticHolder {
    companion object {
        var current_user : User = User("", "", "", "")
        fun set_current_user(user: User) {
            current_user = user
        }

        fun get_current_user(): User {
            return current_user
        }
    }
}