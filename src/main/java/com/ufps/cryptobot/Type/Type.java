package com.ufps.cryptobot.Type;

import com.google.gson.annotations.SerializedName;

public enum Type {
    @SerializedName("private")
    Private,
    group,
    supergroup,
    channel;

    private Type() {
    }
}
