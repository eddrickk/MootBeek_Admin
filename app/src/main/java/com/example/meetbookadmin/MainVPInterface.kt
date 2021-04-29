package com.example.meetbookadmin

// Buat class interface untuk berkomunikasi dengan View
interface MainVPInterface {
    // Fungsi addRoom untuk mengupdate ruangan
    fun addRoom(model : MainModel)
    // Fungsi untuk inisialisasi awal List Ruangan
    fun init(model : MainModel)
}