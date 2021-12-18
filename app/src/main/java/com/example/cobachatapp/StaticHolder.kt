package com.example.cobachatapp

class StaticHolder {
    companion object {
        var current_user : User = User("Guest", "", "", "0")
        fun set_current_user(user: User) {
            current_user = user
        }

        fun get_current_user(): User {
            return current_user
        }

        fun set_guest() {
            current_user = User("Guest", "", "", "0")
        }
    }
}