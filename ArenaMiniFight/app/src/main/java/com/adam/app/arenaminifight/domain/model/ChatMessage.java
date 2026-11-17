/*
 * Copyright (c) 2026 Adam Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adam.app.arenaminifight.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Chat message entity for lobby communication.
 */
public class ChatMessage implements Parcelable {

    private final String mSenderId;
    private final String mSenderName;
    private final String mMessage;
    private final long mTimestamp;

    /**
     * Constructs a ChatMessage instance.
     *
     * @senderId   sender identifier
     * @senderName sender display name
     * @message    message content text
     * @timestamp  message creation timestamp
     */
    public ChatMessage(String senderId, String senderName, String message, long timestamp) {
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

    /**
     * Returns the sender ID.
     *
     * @return sender ID
     */
    public String getSenderId() {
        return mSenderId;
    }

    /**
     * Returns the sender display name.
     *
     * @return sender name
     */
    public String getSenderName() {
        return mSenderName;
    }

    /**
     * Returns the message body.
     *
     * @return message text
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Returns the message timestamp.
     *
     * @return timestamp in milliseconds
     */
    public long getTimestamp() {
        return mTimestamp;
    }
}
