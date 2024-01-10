package com.example.ringfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var editText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().reference
        editText = findViewById(R.id.editTextText)

        val button: Button = findViewById(R.id.button)

        button.setOnClickListener {
            submitTextToFirebase()
        }

        setupFirebaseListener()
    }

    private fun submitTextToFirebase(){
        val text = editText.text.toString().trim()
        database.child("message").removeValue()
        if(text.isNotEmpty()){
            database.child("message").push().setValue(text)
        }
    }

    private fun setupFirebaseListener() {
        database.child("message").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val type = object : GenericTypeIndicator<Map<String, Any>>() {}
                val messageMap = dataSnapshot.getValue(type)

                if(messageMap != null){
                    for(key in messageMap.keys){
                        val message = messageMap[key] as?String
                        if(message == "Ring"){
                            runOnUiThread {
                                Toast.makeText(applicationContext, "Ring!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}