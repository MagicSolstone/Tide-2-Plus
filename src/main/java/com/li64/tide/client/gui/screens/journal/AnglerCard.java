package com.li64.tide.client.gui.screens.journal;

import com.li64.tide.client.ClientAngler;
import com.li64.tide.client.Gui26;
import com.li64.tide.data.angler.Angler;
import com.li64.tide.data.journal.FishRarity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;

/** The journal's angler page: level, title, XP and what the level does (WoW-style character sheet). */
final class AnglerCard {
    private AnglerCard() {}

    private static final int INK = 0xFF000000 | FishingJournal.TEXT_COLOR;
    private static final int DARK = 0xFF6B4A33;
    private static final int GOOD = 0xFF4E7A2A;
    private static final int BAD = 0xFFA8382C;

    static void draw(GuiGraphicsExtractor g, Font font, Player player, int tlX, int tlY) {
        long xp = ClientAngler.xp;
        int own = Angler.levelForXp(xp);
        int effective = !ClientAngler.enabled || ClientAngler.fair ? ClientAngler.baseline : own;
        Angler.Stats s = Angler.Stats.of(effective);

        // ---------------- left page: who
        int x = tlX + 40, y = tlY + 30;
        Gui26.text(g, font, player.getName().getString(), x, y, DARK, false);
        y += 14;
        Gui26.text(g, font, "Fishing level " + own, x, y, INK, false);
        y += 11;
        Gui26.text(g, font, Angler.title(own), x, y, DARK, false);
        y += 18;

        int barW = 140;
        float prog = Angler.progress(xp);
        g.fill(x, y, x + barW, y + 6, ARGB.color(255, 0xD7B594));
        g.fill(x, y, x + Math.round(barW * prog), y + 6, ARGB.color(255, 0x5B8FB9));
        y += 10;
        String next = own >= Angler.MAX_LEVEL ? "Max level"
                : (Angler.xpForLevel(own + 1) - xp) + " XP to level " + (own + 1);
        Gui26.text(g, font, xp + " XP", x, y, INK, false);
        y += 10;
        Gui26.text(g, font, next, x, y, INK, false);
        y += 20;

        if (!ClientAngler.enabled) {
            Gui26.text(g, font, "Levels are off here.", x, y, BAD, false);
            y += 10;
            Gui26.text(g, font, "Everyone fishes at Lv " + ClientAngler.baseline + ".", x, y, INK, false);
        } else if (ClientAngler.fair) {
            Gui26.text(g, font, "Fair play is on:", x, y, BAD, false);
            y += 10;
            Gui26.text(g, font, "fishing at Lv " + ClientAngler.baseline + " for now.", x, y, INK, false);
        } else {
            Gui26.text(g, font, "Lv " + ClientAngler.baseline + " fishes like", x, y, INK, false);
            y += 10;
            Gui26.text(g, font, "a plain fishing rod.", x, y, INK, false);
        }

        // ---------------- right page: what it does
        x = tlX + 215;
        y = tlY + 30;
        Gui26.text(g, font, "Angler stats (Lv " + effective + ")", x, y, DARK, false);
        y += 16;
        y = row(g, font, x, y, "Cast distance", pct(s.castDistance()), s.castDistance() >= 1);
        y = row(g, font, x, y, "Bite wait", pct(s.biteWait()), s.biteWait() <= 1);
        y = row(g, font, x, y, "Hook zones", pct(s.hookZones()), s.hookZones() >= 1);
        y = row(g, font, x, y, "Angler's luck", String.format("%+d", s.luck()), s.luck() >= 0);
        y += 6;
        Gui26.text(g, font, "Fish fight speed", x, y, DARK, false);
        y += 11;
        y = row(g, font, x, y, " Common", pct(Angler.fightSpeed(effective, FishRarity.COMMON)), Angler.fightSpeed(effective, FishRarity.COMMON) <= 1);
        y = row(g, font, x, y, " Rare", pct(Angler.fightSpeed(effective, FishRarity.RARE)), Angler.fightSpeed(effective, FishRarity.RARE) <= 1);
        y = row(g, font, x, y, " Legendary", pct(Angler.fightSpeed(effective, FishRarity.LEGENDARY)), Angler.fightSpeed(effective, FishRarity.LEGENDARY) <= 1);
        y += 6;
        Gui26.text(g, font, "Hook landing odds", x, y, DARK, false);
        y += 11;
        Gui26.text(g, font, "Perfect 100%  Great 100%", x, y, INK, false);
        y += 10;
        Gui26.text(g, font, "Good 90%  Bad 30%  Miss 0%", x, y, INK, false);
    }

    private static int row(GuiGraphicsExtractor g, Font font, int x, int y, String label, String value, boolean good) {
        Gui26.text(g, font, label, x, y, INK, false);
        Gui26.text(g, font, value, x + 150 - font.width(value), y, good ? GOOD : BAD, false);
        return y + 11;
    }

    private static String pct(double v) {
        return Math.round(v * 100) + "%";
    }
}
