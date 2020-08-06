package com.example.firebasenakamaclient

import android.util.Log
import com.heroiclabs.nakama.*
import com.heroiclabs.nakama.api.ChannelMessage
import com.heroiclabs.nakama.api.NotificationList

class WebSocketListener: SocketListener {
    override fun onMatchmakerMatched(p0: MatchmakerMatched?) {
        Log.d(TAG, "")
    }

    override fun onStatusPresence(p0: StatusPresenceEvent?) {
        Log.d(TAG, "")
    }

    override fun onChannelPresence(p0: ChannelPresenceEvent?) {
        Log.d(TAG, "")
    }

    override fun onMatchData(p0: MatchData?) {
        Log.d(TAG, "")
    }

    override fun onStreamPresence(p0: StreamPresenceEvent?) {
        Log.d(TAG, "")
    }

    override fun onDisconnect(p0: Throwable?) {
        Log.d(TAG, "")
    }

    override fun onMatchPresence(p0: MatchPresenceEvent?) {
        Log.d(TAG, "")
    }

    override fun onError(p0: Error?) {
        Log.d(TAG, "")
    }

    override fun onStreamData(p0: StreamData?) {
        Log.d(TAG, "")
    }

    override fun onNotifications(p0: NotificationList?) {
        Log.d(TAG, "")
    }

    override fun onChannelMessage(p0: ChannelMessage?) {
        Log.d(TAG, "")
    }

    companion object{
        private val TAG by lazy { WebSocketListener::class.java.canonicalName }
    }
}