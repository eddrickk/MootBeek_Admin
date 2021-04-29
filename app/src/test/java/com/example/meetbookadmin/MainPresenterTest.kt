package com.example.meetbookadmin

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

// Pengujian dengan Mockito JUnit
@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {
    // Mock MainVPInterface dan inisialisasi Presenter
    private var view : MainVPInterface = mock(MainVPInterface::class.java)
    private var presenter = MainPresenter(view)
    // Testing untuk penambahan Ruangan
    // Penambahan ruangan dengan kapasitas > 0 harus bisa sukses
    @Test
    fun addRoomSuccess() {
        // Buat kapasitas 12, dan inisialisasi List Room
        var capacity = "12"
        var room : MutableList<Room> = mutableListOf()
        // Tambahkan Room ke List
        room.add(Room("Room",capacity.toInt(),""))
        // Jalankan fungsi checkCapacity (bila kapasitas > 0, seharusnya akan mengembalikan List yang terisi)
        var result = presenter.checkCapacity(capacity,room)
        // Cek bila List memiliki isi dengan .size
        assertEquals(1,result.size)
    }

    // Penambahan ruangan dengan kapasitas <= 0 tidak bisa sukses
    @Test
    fun addRoomFail() {
        // BUat kapasitas 0 dan inisialisasi List Room
        var capacity = "0"
        var room : MutableList<Room> = mutableListOf()
        // Tambahkan Room ke List
        room.add(Room("Room",capacity.toInt(),""))
        // Jalankan fungsi checkCapacity (bila kapasitas <= 0, seharusnya akan mengembalikan List yang kosong)
        var result = presenter.checkCapacity(capacity,room)
        // Cek bila List Tidak memiliki isi dengan .size
        assertEquals(0,result.size)
    }
}