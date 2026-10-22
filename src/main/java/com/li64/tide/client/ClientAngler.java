package com.li64.tide.client;

/** The local player's fishing level, as last sent by the server. */
public final class ClientAngler {
    private ClientAngler() {}

    public static long xp = 0;
    public static boolean fair = false;
    public static boolean enabled = true;
    public static int baseline = 40;

    public static void update(long xp, boolean fair, boolean enabled, int baseline) {
        ClientAngler.xp = xp;
        ClientAngler.fair = fair;
        ClientAngler.enabled = enabled;
        ClientAngler.baseline = baseline;
    }
}
