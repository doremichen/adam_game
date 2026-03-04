/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the chat message model of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable {
    // TAG
    private static final String TAG = "ChatMessage";

    private final String mSenderId;
    private final String mSenderName;
    private final String mMessage;
    private final long mTimestamp;

    public ChatMessage(String senderId,
                       String senderName,
                       String message,
                       long timestamp) {
        mSenderId = senderId;
        mSenderName = senderName;
        mMessage = message;
        mTimestamp = timestamp;
    }

    protected ChatMessage(Parcel in) {
        mSenderId = in.readString();
        mSenderName = in.readString();
        mMessage = in.readString();
        mTimestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSenderId);
        dest.writeString(mSenderName);
        dest.writeString(mMessage);
        dest.writeLong(mTimestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    // -- getter --
    public String getSenderId() {
        return mSenderId;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public String getMessage() {
        return mMessage;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
