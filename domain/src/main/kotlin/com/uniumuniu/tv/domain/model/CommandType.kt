package com.uniumuniu.tv.domain.model

enum class CommandType(name: String) {
    turnOn("TURN_ON"),
    turnOff("TURN_OFF"),
    volumeUp("VOLUME_UP"),
    volumeDown("VOLUME_DOWN"),
    nextChannel("NEXT_CHANNEL"),
    previousChannel("PREVIOUS_CHANNEL"),
    arrowUp("ARROW_UP"),
    arrowDown("ARROW_DOWN"),
    arrowLeft("ARROW_LEFT"),
    arrowRight("ARROW_RIGHT"),
    ok("OK"),
    back("BACK"),
}
