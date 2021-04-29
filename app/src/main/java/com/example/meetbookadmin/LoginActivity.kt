package com.example.meetbookadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val user = "Admin"
    val pass = "123"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LoginButtonAdm.setOnClickListener {
            var intentToMain = Intent(this, MainActivity::class.java)

            if (AuthenticateUser(LUsernameAdm.text.toString()))
            {
                if (AuthenticatePass(LPasswordAdm.text.toString()))
                {
                    startActivity(intentToMain)
                }
                else{
                    makeToast("Password Tidak Cocok")
                }
            }
            else{
                makeToast("Username Tidak Cocok")
            }
        }
    }

    private fun makeToast(text : String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    private fun AuthenticatePass(password: String): Boolean {
        if (password.toLowerCase() == pass.toLowerCase()){
            return true
        }
        return false
    }

    private fun AuthenticateUser(username: String): Boolean {
        if (username.toLowerCase() == user.toLowerCase()){
            return true
        }
        return false
    }
}