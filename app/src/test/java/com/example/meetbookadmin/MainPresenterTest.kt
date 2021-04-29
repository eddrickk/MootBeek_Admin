package com.example.meetbookadmin

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    private var view : MainVPInterface = mock(MainVPInterface::class.java)
    private var presenter = MainPresenter(view)
    @Test
    fun addRoomSuccess() {
        var capacity = "12"
        var room : MutableList<Room> = mutableListOf()
        room.add(Room("Room",capacity.toInt(),""))
        var result = presenter.checkCapacity(capacity,room)
        assertEquals(1,result.size)
    }
    @Test
    fun addRoomFail() {
        var capacity = "0"
        var room : MutableList<Room> = mutableListOf()
        room.add(Room("Room",capacity.toInt(),""))
        var result = presenter.checkCapacity(capacity,room)
        assertEquals(0,result.size)
    }
}