package com.example.firebasenakamaclient

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Executors
import com.heroiclabs.nakama.*
import com.heroiclabs.nakama.api.ChannelMessage
import com.heroiclabs.nakama.api.NotificationList
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var socket: SocketClient
    private lateinit var session: Session
    private lateinit var client: DefaultClient
    private val ip = "192.168.43.242"
    private val lLogs = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        connect()
        btn_notify.setOnClickListener {
            socket.rpc("getNotification").addListener(Runnable {  }, Executors.DIRECT_EXECUTOR)
        }
        btn_disconnect.setOnClickListener {
            onDisconnectBtnClick()
        }
        fireStoreAction()
    }

    private fun connect() {

        client = DefaultClient(
            "defaultkey",
            ip,
            7349,
            false
        )

        client.authenticateCustom("nakama_client", true).let { future ->
            future.addListener(
                Runnable {
                    session = future.get()
                    onSessionCreated()
                }, Executors.DIRECT_EXECUTOR
            )
        }

        lLogs.observe(this, androidx.lifecycle.Observer {
            tv_logs.setText(lLogs.value)
        })
    }

    fun onSessionCreated() {
        socket = client.createSocket(
            ip,
            7350,
            false
        )


        socket.connect(session, object : SocketListener {
            override fun onMatchmakerMatched(p0: MatchmakerMatched?) {
                appendLogs("matched matchId: ${p0?.matchId}")
            }

            override fun onStatusPresence(p0: StatusPresenceEvent?) {}

            override fun onChannelPresence(p0: ChannelPresenceEvent?) {}

            override fun onMatchData(p0: MatchData?) {}

            override fun onStreamPresence(p0: StreamPresenceEvent?) {}

            override fun onDisconnect(p0: Throwable?) {
                appendLogs("onDisconnect")
            }

            override fun onMatchPresence(p0: MatchPresenceEvent?) {}

            override fun onError(p0: Error?) {
                appendLogs("onError ${p0?.message}")
            }

            override fun onStreamData(p0: StreamData?) {}

            override fun onNotifications(p0: NotificationList?) {
                appendLogs("onNotifications ${p0?.notificationsList?.size}")
            }

            override fun onChannelMessage(p0: ChannelMessage?) {
                appendLogs("onChannelMessage ${p0?.messageId}")
            }

        }).addListener(Runnable { appendLogs("socket connected") }, Executors.DIRECT_EXECUTOR)
    }

    fun onDisconnectBtnClick() {
        socket.disconnect()
        client.disconnect()
    }

    fun appendLogs(log: String) {
        lLogs.postValue(lLogs.value + "\n" + log)
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

    companion object {
        private val TAG by lazy { MainActivity::class.java.canonicalName }
    }
}