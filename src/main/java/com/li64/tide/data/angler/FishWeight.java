package com.li64.tide.data.angler;

import com.li64.tide.util.TideUtils;
import net.minecraft.util.RandomSource;

/**
 * Weight from length: W(g) = 0.01 · K · L(cm)³, where K is the species' build (1.0 is a trout-shaped fish;
 * eels are thin, sunfish and bass are deep-bodied). Quality adds a condition factor, plus a little noise.
 */
public final class FishWeight {
    private FishWeight() {}

    /** Body build by name; first match wins. */
    private static final String[][] BUILD = {
            {"sock", "0.05"}, {"jelly", "0.25"}, {"starfish", "0.3"}, {"eel", "0.2"}, {"larva", "0.4"},
            {"swordfish", "0.45"}, {"sailfish", "0.4"}, {"manta", "0.45"}, {"dragon", "0.3"}, {"mantyvern", "0.4"},
            {"shark", "0.75"}, {"pike", "0.6"}, {"sturgeon", "0.6"}, {"arapaima", "0.65"}, {"darter", "0.7"},
            {"squid", "0.6"}, {"cuttlefish", "0.7"}, {"pentapus", "0.6"}, {"shrimp", "0.5"}, {"snail", "0.9"},
            {"mackerel", "0.8"}, {"mahi", "0.8"}, {"anchovy", "0.7"}, {"walleye", "0.8"}, {"salmon", "0.9"},
            {"trout", "1.0"}, {"catfish", "0.9"}, {"cod", "0.9"}, {"snook", "0.9"},
            {"bass", "1.35"}, {"carp", "1.4"}, {"koi", "1.4"}, {"tuna", "1.5"}, {"grouper", "1.5"},
            {"snapper", "1.3"}, {"rockfish", "1.3"}, {"perch", "1.2"}, {"flounder", "1.1"},
            {"crappie", "1.5"}, {"bluegill", "1.7"}, {"angelfish", "1.8"}, {"puffer", "2.5"},
            {"angler", "1.6"}, {"coelacanth", "1.2"},
    };

    public static double build(String speciesPath) {
        for (String[] b : BUILD) if (speciesPath.contains(b[0])) return Double.parseDouble(b[1]);
        return 1.0;
    }

    public static double grams(String speciesPath, double lengthCm, CatchQuality quality, RandomSource rng) {
        double noise = 1.0 + (rng.nextDouble() - 0.5) * 0.08;
        return 0.01 * build(speciesPath) * Math.pow(lengthCm, 3) * quality.condition() * noise;
    }

    // ------------------------------------------------------------------ formatting

    /** Pounds with one decimal ("3.4 lb"); two for small fish ("0.38 lb"); whole pounds from 100 ("1,298 lb"). */
    public static String imperial(double grams) {
        double lb = grams / 453.59237;
        if (lb < 0.01) return "0.01 lb";
        if (lb < 1) return String.format("%.2f lb", lb);
        if (lb < 100) return String.format("%.1f lb", lb);
        return String.format("%,d lb", Math.round(lb));
    }

    /** Grams under a kilo ("380 g"), then kilos ("2.3 kg", "1,298 kg"). */
    public static String metric(double grams) {
        if (grams < 1000) return Math.max(1, Math.round(grams)) + " g";
        if (grams < 100_000) return String.format("%.1f kg", grams / 1000);
        return String.format("%,d kg", Math.round(grams / 1000));
    }

    /** Uses the client's unit setting when there is one. */
    public static String format(double grams) {
        return TideUtils.useImperial() ? imperial(grams) : metric(grams);
    }

    public static String imperialLength(double cm) {
        return TideUtils.imperialLength(cm);
    }
}
