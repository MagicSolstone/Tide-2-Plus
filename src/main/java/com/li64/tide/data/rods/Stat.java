package com.li64.tide.data.rods;

/**
 * The base stats, each with one short name and its own colour, used the same way everywhere: item and rod tooltips,
 * the rod panel, the Angling Table's stats panel and the journal. (What a piece does besides these is a {@link Skills} skill.)
 */
public enum Stat {
    CASTING("Casting", 0x7FC8FF, "How far your cast goes."),
    ATTRACTION("Attraction", 0xFFE066, "How soon a fish bites after the bobber lands."),
    PRECISION("Precision", 0x55FFFF, "How wide the hook zones are when a fish bites, so it's easier to hook it well."),
    FINESSE("Finesse", 0x8FE3C8, "Your chance of still landing a fish you hooked Good or Bad, instead of losing it."),
    STABILITY("Stability", 0xB9A7FF, "How calmly a hooked fish fights: it darts around the catch bar less."),
    CONTROL("Control", 0xD9D9D9, "A bigger catch area on the catch bar, so the fish is easier to keep in it."),
    FORTUNE("Fortune", 0x6BE07A, "Luck: more rare fish and treasure."),
    QUALITY("Quality", 0xFFB84D, "Shifts the odds toward better-quality fish."),
    EXPERIENCE("Experience", 0xC6F25A, "More fishing experience from each catch, so you level up faster."),
    DOUBLE_CATCH("Double Catch", 0xFF9ECF, "A chance to reel in a second fish of the same kind with the first.");

    public final String label;
    public final int color;
    /** What it does, in a sentence: the hover text in the Angling Table's stats panel. */
    public final String explain;

    Stat(String label, int color, String explain) {
        this.label = label;
        this.color = color;
        this.explain = explain;
    }

    /** Skills (what a piece does besides the base stats) are all this one colour. */
    public static final int SKILL = 0xFF7A3D;
}
