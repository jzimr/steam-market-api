package com.multicus.steamApp.login;

import kong.unirest.JsonNode;

class RSAJson {
    private final boolean success;
    private final String publicKeyMod;
    private final String publicKeyExp;
    private final long timestamp;

    RSAJson(JsonNode node) {
        success = node.getObject().getBoolean("success");
        publicKeyMod = node.getObject().getString("publickey_mod");
        publicKeyExp = node.getObject().getString("publickey_exp");
        timestamp = node.getObject().getLong("timestamp");
    }

    String getPublicKeyExp() {
        return publicKeyExp;
    }

    String getPublicKeyMod() {
        return publicKeyMod;
    }

    long getTimestamp() {
        return timestamp;
    }

    boolean isSuccess() {
        return success;
    }
}
