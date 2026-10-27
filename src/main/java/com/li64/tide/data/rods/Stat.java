package com.li64.tide.data.rods;

/**
 * The base stats, each with one short name and its own colour, used the same way everywhere: item and rod tooltips,
 * the rod panel, the Angling Table's stats panel and the journal. (What a piece does besides these is a {@link Skills} skill.)
 */
public enum Stat {
    CASTING("Casting", 0x7FC8FF),          // how far the cast goes
    ATTRACTION("Attraction", 0xFFE066),    // how soon a fish bites
    PRECISION("Precision", 0x55FFFF),      // how wide the hook zones are
    FINESSE("Finesse", 0x8FE3C8),          // landing the fish from a Good or Bad hook
    STABILITY("Stability", 0xB9A7FF),      // how calmly the fish fights
    CONTROL("Control", 0xD9D9D9),          // how easy the catch bar is
    FORTUNE("Fortune", 0x6BE07A),          // rare fish (luck)
    QUALITY("Quality", 0xFFB84D),          // catch quality
    EXPERIENCE("Experience", 0xC6F25A),    // fishing XP
    DOUBLE_CATCH("Double Catch", 0xFF9ECF); // a second fish

    public final String label;
    public final int color;

    Stat(String label, int color) {
        this.label = label;
        this.color = color;
    }

    /** Skills (what a piece does besides the base stats) are all this one colour. */
    public static final int SKILL = 0xFF7A3D;
}
