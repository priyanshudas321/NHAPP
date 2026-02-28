package com.nhapp.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nhapp.R

class FcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Store this token in your Supabase `users` table
        // This token is required by your Supabase Edge Function to send FCM via Admin SDK
        /*
        lifecycleScope.launch {
            SupabaseClient.client.postgrest["users"].update(mapOf("fcm_token" to token)) {
                filter { eq("id", currentUserId) }
            }
        }
        */
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        // This is called when app is in the foreground
        // When in background, FCM automatically shows the notification if payload contains `notification`
        val title = message.notification?.title ?: "NHAPP"
        val body = message.notification?.body ?: "You have a new message"
        
        showNotification(title, body)
    }

    private fun showNotification(title: String, messageBody: String) {
        val channelId = "nhapp_chat_notifications"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Chat Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
