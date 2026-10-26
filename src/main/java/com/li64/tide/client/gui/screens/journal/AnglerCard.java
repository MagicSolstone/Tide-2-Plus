package com.li64.tide.client.gui.screens.journal;

import com.li64.tide.client.ClientAngler;
import com.li64.tide.client.Gui26;
import com.li64.tide.data.angler.Angler;
import com.li64.tide.data.angler.CatchQuality;
import com.li64.tide.data.journal.FishRarity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;

/** The journal's angler page: who you are as an angler (left) and what your level does (right). */
final class AnglerCard {
    private AnglerCard() {}

    private static final int INK = 0xFF000000 | FishingJournal.TEXT_COLOR;
    private static final int DARK = 0xFF6B4A33;
    private static final int GOOD = 0xFF4E7A2A;
    private static final int OK = 0xFF8A6A52;
    private static final int BAD = 0xFFA8382C;
    private static final int COL = 150;

    static void draw(GuiGraphicsExtractor g, Font font, Player player, int tlX, int tlY) {
        long xp = ClientAngler.xp;
        int own = Angler.levelForXp(xp);
        int effective = !ClientAngler.enabled || ClientAngler.fair ? ClientAngler.baseline : own;
        Angler.Stats s = Angler.Stats.of(effective);

        // ---------------- left page: level, XP, catch quality odds
        int x = tlX + 40, y = tlY + 30;
        Gui26.text(g, font, player.getName().getString(), x, y, DARK, false);
        y += 14;
        Gui26.text(g, font, "Fishing level " + own, x, y, DARK, false);
        y += 11;
        Gui26.text(g, font, Angler.title(own), x, y, INK, false);
        y += 15;

        g.fill(x, y, x + COL, y + 5, ARGB.color(255, 0xD7B594));
        g.fill(x, y, x + Math.round(COL * Angler.progress(xp)), y + 5, ARGB.color(255, 0x5B8FB9));
        y += 9;
        String next = own >= Angler.MAX_LEVEL ? "Max level!"
                : String.format("%,d XP to level %d", Angler.xpForLevel(own + 1) - xp, own + 1);
        Gui26.text(g, font, next, x, y, INK, false);
        y += 10;
        int nextTitle = Math.min(Angler.MAX_LEVEL, (own / 10 + 1) * 10);
        if (own < Angler.MAX_LEVEL)
            Gui26.text(g, font, Angler.title(nextTitle) + " at Lv " + nextTitle, x, y, INK, false);
        y += 16;

        if (!ClientAngler.enabled) {
            Gui26.text(g, font, "Levels are off on this server;", x, y, BAD, false);
            y += 10;
            Gui26.text(g, font, "everyone fishes at Lv " + ClientAngler.baseline + ".", x, y, INK, false);
            y += 16;
        } else if (ClientAngler.fair) {
            Gui26.text(g, font, "Fair play: fishing at Lv " + ClientAngler.baseline, x, y, BAD, false);
            y += 10;
            Gui26.text(g, font, "until the contest ends.", x, y, INK, false);
            y += 16;
        }

        Gui26.text(g, font, "Catch quality odds", x, y, DARK, false);
        y += 11;
        double[] odds = CatchQuality.odds(effective);
        for (CatchQuality q : CatchQuality.values()) {
            double p = odds[q.ordinal()];
            String val = p >= 0.01 ? Math.round(p * 100) + "%" : "1 in " + String.format("%,d", Math.round(1 / p));
            Gui26.text(g, font, q.marks() + " " + q.label, x, y, 0xFF000000 | journalColor(q), false);
            Gui26.text(g, font, val, x + COL - font.width(val), y, INK, false);
            y += 10;
        }
        Gui26.text(g, font, "Bigger fish fight harder.", x, y + 2, OK, false);

        // ---------------- right page: what the level does
        x = tlX + 215;
        y = tlY + 30;
        Gui26.text(g, font, "Your skills at Lv " + effective, x, y, DARK, false);
        y += 10;
        Gui26.text(g, font, "compared to a normal rod", x, y, OK, false);
        y += 14;
        y = bonus(g, font, x, y, "Casting range", s.castDistance() - 1);
        y = bonus(g, font, x, y, "Bite speed", 1 / s.biteWait() - 1);
        y = bonus(g, font, x, y, "Hook window", s.hookZones() - 1);
        String luck = s.luck() == 0 ? "None" : String.format("%+d%%", Math.round(com.li64.tide.data.rods.RodEffects.rareFinds(s.luck()) * 100));
        y = row(g, font, x, y, "Rare finds", luck, s.luck() > 0 ? GOOD : s.luck() < 0 ? BAD : OK);
        y += 8;

        Gui26.text(g, font, "Fish fight difficulty", x, y, DARK, false);
        y += 12;
        for (FishRarity r : FishRarity.values()) {
            double f = Angler.fightSpeed(effective, r);
            String word = f < 0.92 ? "Easy" : f < 1.1 ? "Normal" : f < 1.35 ? "Tough" : f < 1.7 ? "Hard" : "Very hard";
            int color = f < 0.92 ? GOOD : f < 1.1 ? OK : BAD;
            y = row(g, font, x, y, " " + rarityName(r), word, color);
        }
        y += 8;

        Gui26.text(g, font, "Chance to land, by hook", x, y, DARK, false);
        y += 12;
        Gui26.text(g, font, "Perfect 100%  Great 100%", x, y, INK, false);
        y += 10;
        Gui26.text(g, font, "Good 90%  Bad 30%  Miss 0%", x, y, INK, false);
    }

    private static int bonus(GuiGraphicsExtractor g, Font font, int x, int y, String label, double delta) {
        long pct = Math.round(delta * 100);
        String val = pct == 0 ? "Normal" : String.format("%+d%%", pct);
        return row(g, font, x, y, label, val, pct > 0 ? GOOD : pct < 0 ? BAD : OK);
    }

    private static int row(GuiGraphicsExtractor g, Font font, int x, int y, String label, String value, int color) {
        Gui26.text(g, font, label, x, y, INK, false);
        Gui26.text(g, font, value, x + COL - font.width(value), y, color, false);
        return y + 11;
    }

    private static String rarityName(FishRarity r) {
        return switch (r) {
            case COMMON -> "Common";
            case UNCOMMON -> "Uncommon";
            case RARE -> "Rare";
            case VERY_RARE -> "Very rare";
            case LEGENDARY -> "Legendary";
        };
    }

    /** Quality colours are made for dark tooltips; deepen them for the paper page. */
    static int journalColor(CatchQuality q) {
        int rgb = q.color;
        int r = (rgb >> 16) & 0xFF, gr = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
        if (r > 200 && gr > 200 && b > 200) return 0x8A6A52;
        return ((int) (r * 0.7) << 16) | ((int) (gr * 0.7) << 8) | (int) (b * 0.7);
    }
}
