package com.example.meetbookadmin

// Buat class data MainModel yang berisi List Ruangan dan Jumlah Ruangan
data class MainModel (var item : MutableList<Room> = mutableListOf(), var count : Int = 0) {
}