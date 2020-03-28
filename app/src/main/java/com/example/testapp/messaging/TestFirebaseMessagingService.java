package com.example.testapp.messaging;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class TestFirebaseMessagingService extends FirebaseMessagingService {

    @Override public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
