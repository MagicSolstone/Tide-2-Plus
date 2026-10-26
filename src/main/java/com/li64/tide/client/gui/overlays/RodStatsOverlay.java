package com.li64.tide.client.gui.overlays;

import com.li64.tide.Tide;
import com.li64.tide.client.ClientAngler;
import com.li64.tide.client.Gui26;
import com.li64.tide.data.angler.Angler;
import com.li64.tide.data.rods.RodEffects;
import com.li64.tide.registries.items.TideFishingRodItem;
import com.li64.tide.util.BaitUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * While a rod is in hand: a small panel at the right edge with what the rod, everything on it, the outfit
 * and the fishing level add up to, against a plain rod. The basics always; extras only when there are any;
 * and which place-and-time perks are on right now. Off with "showRodStats": false in the client config.
 */
public final class RodStatsOverlay {
    private RodStatsOverlay() {}

    private static final int GOOD = 0xFF7FE07F, BAD = 0xFFFF8A7A, TEXT = 0xFFE8E8E8, DIM = 0xFFA8B4BC, HEAD = 0xFFFFD27F, OFF = 0xFFA87070;

    private record Row(String label, String value, int color) {}


    public static void render(GuiGraphicsExtractor g, float dt) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || !Tide.CLIENT_CONFIG.general.showRodStats) return;      // the HUD already hides with F1
        ItemStack rod = player.getMainHandItem().getItem() instanceof TideFishingRodItem ? player.getMainHandItem()
                : player.getOffhandItem().getItem() instanceof TideFishingRodItem ? player.getOffhandItem() : ItemStack.EMPTY;
        if (rod.isEmpty()) return;

        int own = Angler.levelForXp(ClientAngler.xp);
        int level = !ClientAngler.enabled || ClientAngler.fair ? ClientAngler.baseline : own;
        Angler.Stats st = Angler.Stats.of(level);
        RodEffects e = RodEffects.of(player, rod);
        var enchants = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int lure = EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(Enchantments.LURE), rod);
        int sea = EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(Enchantments.LUCK_OF_THE_SEA), rod);
        int speed = lure + Math.min(BaitUtils.getCombinedSpeed(rod), TideFishingRodItem.BAIT_SPEED_CAP) + e.speed();
        int luck = e.luck() + BaitUtils.getCombinedLuck(rod) + st.luck() + sea;

        // every stat, always, so two rods can be compared at a glance (a dash is "nothing from this")
        List<Row> rows = new ArrayList<>();
        rows.add(pct("Cast range", (1 + e.cast()) * st.castDistance() - 1, false));
        // one bite-speed number: the rod and tackle, lure/bait/rod speed points, and your fishing level, all together
        // (+% = faster, like every other row). Lure enchantments are in here; luck from Luck of the Sea is in Luck.
        rows.add(pct("Bite speed", 1 / (e.biteWaitFactor(speed) * st.biteWait()) - 1, false));
        rows.add(pct("Hook window", (1 + e.window()) * st.hookZones() - 1, false));
        rows.add(pct("Rare finds", RodEffects.rareFinds(luck), false));      // luck: rarer fish, a few more crates
        rows.add(extra("Catch quality", e.quality(), ""));
        rows.add(extra("Fishing XP", e.xp(), ""));
        rows.add(extra("Land Good/Bad", e.land(), ""));
        rows.add(extra("Fish fight", -e.fight(), " easier"));
        rows.add(extra("Catch difficulty", -e.ease(), ""));
        rows.add(extra("Second fish", Math.min(e.twin(), RodEffects.TWIN_CAP), e.twin() > RodEffects.TWIN_CAP ? " (max)" : ""));
        rows.add(extra("Comes up cooked", e.cook(), ""));

        Set<String> active = new LinkedHashSet<>(), idle = new LinkedHashSet<>();
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(RodEffects.parts(rod));
        for (ItemStack part : all)
            for (RodEffects.Perk p : RodEffects.situational(part))
                (p.when().test(player) ? active : idle).add(p.when().text.replaceFirst("^(in|on|at|by|when) (the |a )?", ""));
        idle.removeAll(active);
        // matched rigs: complete ones show as on or off like any perk; one piece short gets a hint
        for (com.li64.tide.data.rods.RigSets.RigSet set : com.li64.tide.data.rods.RigSets.SETS) {
            int n = com.li64.tide.data.rods.RigSets.pieces(set, rod);
            if (n >= com.li64.tide.data.rods.RigSets.HALF)
                (set.when().test(player) ? active : idle).add(set.name().toLowerCase() + " " + com.li64.tide.data.rods.RigSets.tier(n).toLowerCase());
        }

        Font font = mc.font;
        float s = 0.75f;
        int labelW = rows.stream().mapToInt(r -> font.width(r.label())).max().orElse(0);
        int valueW = rows.stream().mapToInt(r -> font.width(r.value())).max().orElse(0);
        String title = "Fishing Lv " + level + (ClientAngler.fair && ClientAngler.enabled ? " (fair play)" : "");
        String on = active.isEmpty() ? null : "Active: " + String.join(", ", active);
        String off = idle.isEmpty() ? null : "Not now: " + String.join(", ", idle);
        int w = Math.max(labelW + 10 + valueW, Math.max(font.width(title), Math.max(on == null ? 0 : Math.min(font.width(on), 170), off == null ? 0 : Math.min(font.width(off), 170))));
        List<net.minecraft.util.FormattedCharSequence> onLines = on == null ? List.of() : font.split(net.minecraft.network.chat.Component.literal(on), Math.max(w, 120));
        List<net.minecraft.util.FormattedCharSequence> offLines = off == null ? List.of() : font.split(net.minecraft.network.chat.Component.literal(off), Math.max(w, 120));
        // progress to the next level: a thin bar under the title that fills as you catch (and glides when XP lands)
        boolean maxed = own >= Angler.MAX_LEVEL;
        long lo = Angler.xpForLevel(own), hi = maxed ? lo : Angler.xpForLevel(own + 1);
        double target = maxed ? 1.0 : Math.max(0, Math.min(1, (ClientAngler.xp - lo) / (double) Math.max(1, hi - lo)));
        if (own != shownLevel) { shownLevel = own; shownFill = target; }
        shownFill += (target - shownFill) * 0.12;
        if (Math.abs(target - shownFill) < 0.002) shownFill = target;
        String pctText = maxed ? "max" : Math.round(target * 100) + "%";
        int h = 17 + rows.size() * 10 + (onLines.size() + offLines.size()) * 10 + (onLines.isEmpty() && offLines.isEmpty() ? 0 : 3);
        int sw = g.guiWidth(), sh = g.guiHeight();
        float px = sw - (w + 10) * s - 4, py = (sh - (h + 8) * s) / 2f;

        g.pose().pushMatrix();
        g.pose().translate(px, py);
        g.pose().scale(s, s);
        g.fill(0, 0, w + 10, h + 8, 0x90101820);
        g.fill(0, 0, 2, h + 8, 0xFF4FA3FF);
        int y = 5;
        Gui26.text(g, font, title, 6, y, HEAD, true);
        Gui26.text(g, font, pctText, 6 + w - font.width(pctText), y, DIM, true);
        y += 11;
        g.fill(6, y, 6 + w, y + 3, 0x60FFFFFF);
        g.fill(6, y, 6 + (int) Math.round(w * shownFill), y + 3, 0xFFFFC83A);
        y += 6;
        for (Row r : rows) {
            Gui26.text(g, font, r.label(), 6, y, TEXT, true);
            Gui26.text(g, font, r.value(), 6 + w - font.width(r.value()), y, r.color(), true);
            y += 10;
        }
        if (!onLines.isEmpty()) {
            y += 3;
            for (var line : onLines) { Gui26.text(g, font, line, 6, y, DIM, true); y += 10; }
        }
        if (!offLines.isEmpty()) {
            if (onLines.isEmpty()) y += 3;
            for (var line : offLines) { Gui26.text(g, font, line, 6, y, OFF, true); y += 10; }
        }
        g.pose().popMatrix();
    }

    /** A row for an extra: a plus percentage when the gear gives it, a dim dash when it doesn't. */
    private static Row extra(String label, double v, String suffix) {
        long n = Math.round(v * 100);
        if (n == 0) return new Row(label, "-", DIM);
        return new Row(label, String.format("%+d%%", n) + suffix, n > 0 ? GOOD : BAD);
    }

    private static int shownLevel = -1;
    private static double shownFill = 0;

    private static Row pct(String label, double v, boolean lowerIsBetter) {
        long n = Math.round(v * 100);
        if (n == 0) return new Row(label, "Normal", DIM);
        boolean good = lowerIsBetter ? n < 0 : n > 0;
        return new Row(label, String.format("%+d%%", n), good ? GOOD : BAD);
    }
}
