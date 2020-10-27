package com.recocozephyr.rpc.service.impl;

import com.recocozephyr.rpc.service.TellJokes;

import java.awt.peer.SystemTrayPeer;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 15:32
 * @DESCRIPTIONS:
 */
public class TellBadJokes implements TellJokes {
    public TellBadJokes() {
        System.out.println(" to tell bad jokes");
    }

    public String tell(int a) {
        System.out.println("in tell");
        return "Yes, I have reservations... but I'll eat here anyway!";
    }
}
