package com.example.firebasenakamaclient

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.firestore.FirebaseFirestore
import com.heroiclabs.nakama.DefaultClient
import com.heroiclabs.nakama.Session
import com.heroiclabs.nakama.SocketClient
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var socket: SocketClient
    private lateinit var session: Session
    private lateinit var client: DefaultClient
    private val ip = "192.168.43.242"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        btn_join.setOnClickListener {
            onJoinBtnClick()
        }
        fireStoreAction()
    }

    private fun onJoinBtnClick() {

        client = DefaultClient(
            "defaultkey",
            ip,
            7349,
            false
        )

        session = client.authenticateCustom("${Calendar.getInstance().timeInMillis}", true).get()
        socket = client.createSocket(
            ip,
            7350,
            false
        )
        socket.connect(session, WebSocketListener()).get()
        Log.d(TAG, "socket connected")
        socket.addMatchmaker(2, 2).get()
        Log.d(TAG, "match requested")
    }

    /*
    DB read, write operation to firebase DB for testing.
     */
    private fun fireStoreAction() {
        val db = FirebaseFirestore.getInstance()

        // Create a new user with a first and last name
        var user: MutableMap<String, Any> = HashMap()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


        // Create a new user with a first, middle, and last name
        user = HashMap()
        user["first"] = "Alan"
        user["middle"] = "Mathison"
        user["last"] = "Turing"
        user["born"] = 1912

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                        db.collection("users").document(document.id).delete()
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
        client.disconnect()
    }

    companion object {
        private val TAG by lazy { MainActivity::class.java.canonicalName }
    }
}