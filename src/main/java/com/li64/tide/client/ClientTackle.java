package com.li64.tide.client;

import java.util.HashSet;
import java.util.Set;

/** The tackle the local player has found, as last sent by the server (item paths). */
public final class ClientTackle {
    private ClientTackle() {}

    public static Set<String> found = new HashSet<>();
}
