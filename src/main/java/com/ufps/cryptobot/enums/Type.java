package com.ufps.cryptobot.enums;

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
