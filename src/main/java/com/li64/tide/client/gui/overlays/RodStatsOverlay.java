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

    /** The rows always on the panel; everything else shows while sneaking. */
    private static final Set<String> BASIC = Set.of("Cast range", "Bite speed", "Hook window", "Luck");

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

        List<Row> rows = new ArrayList<>();
        rows.add(pct("Cast range", (1 + e.cast()) * st.castDistance() - 1, false));
        // one bite-speed number: the rod and tackle, the fishing level, and lure/bait speed all together (+% = faster, like every other row)
        rows.add(pct("Bite speed", 1 / (e.biteWaitFactor() * st.biteWait() * RodEffects.lureWaitFactor(speed)) - 1, false));
        rows.add(pct("Hook window", (1 + e.window()) * st.hookZones() - 1, false));
        rows.add(new Row("Luck", luck == 0 ? "None" : String.format("%+d", luck), luck > 0 ? GOOD : luck < 0 ? BAD : DIM));
        if (e.quality() != 0) rows.add(pct("Catch quality", e.quality(), false));
        if (e.fight() != 0) rows.add(new Row("Fish fight", Math.round(Math.abs(e.fight()) * 100) + "% " + (e.fight() < 0 ? "easier" : "harder"), e.fight() < 0 ? GOOD : BAD));
        if (e.ease() != 0) rows.add(new Row("Catch difficulty", "-" + Math.round(e.ease() * 100) + "%", GOOD));
        if (e.land() != 0) rows.add(pct("Land Good/Bad", e.land(), false));
        if (e.xp() != 0) rows.add(pct("Fishing XP", e.xp(), false));
        if (e.twin() != 0) rows.add(new Row("Second fish", Math.round(Math.min(e.twin(), RodEffects.TWIN_CAP) * 100) + "%" + (e.twin() > RodEffects.TWIN_CAP ? " (max)" : ""), GOOD));
        if (e.cook() != 0) rows.add(new Row("Comes up cooked", Math.round(e.cook() * 100) + "%", GOOD));
        if (e.upgrade() != 0) rows.add(new Row("Quality bump", Math.round(e.upgrade() * 100) + "%", GOOD));

        // vanilla enchantments: what each one does here (their numbers are already in the rows above)
        if (lure > 0) rows.add(new Row("Lure " + roman(lure), String.format("%+d%%", Math.round((1 / RodEffects.lureWaitFactor(lure) - 1) * 100)), GOOD));
        if (sea > 0) rows.add(new Row("Luck of the Sea " + roman(sea), "+" + sea + " luck", GOOD));
        int unbreaking = EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(Enchantments.UNBREAKING), rod);
        if (unbreaking > 0) rows.add(new Row("Unbreaking " + roman(unbreaking), "wears slower", GOOD));
        if (EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(Enchantments.MENDING), rod) > 0) rows.add(new Row("Mending", "repairs from XP", GOOD));

        // normally just the four basics; sneaking opens the full panel (extras, enchantments, what is active here)
        boolean detail = player.isShiftKeyDown();
        int hidden = 0;
        if (!detail) {
            int before = rows.size();
            rows.removeIf(r -> !BASIC.contains(r.label()));
            hidden = before - rows.size();
        }

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
                (set.when().test(player) ? active : idle).add(set.name().toLowerCase() + (n >= com.li64.tide.data.rods.RigSets.PIECES ? "" : " (2 of 3)"));
        }

        Font font = mc.font;
        float s = 0.75f;
        int labelW = rows.stream().mapToInt(r -> font.width(r.label())).max().orElse(0);
        int valueW = rows.stream().mapToInt(r -> font.width(r.value())).max().orElse(0);
        String title = "Fishing Lv " + level + (ClientAngler.fair && ClientAngler.enabled ? " (fair play)" : "");
        if (!detail && (!active.isEmpty() || !idle.isEmpty())) hidden++;
        String on = !detail || active.isEmpty() ? null : "Active: " + String.join(", ", active);
        String off = !detail || idle.isEmpty() ? null : "Not now: " + String.join(", ", idle);
        String hint = hidden > 0 ? "Sneak for " + hidden + " more" : null;
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
        int h = 17 + rows.size() * 10 + (hint == null ? 0 : 10) + (onLines.size() + offLines.size()) * 10 + (onLines.isEmpty() && offLines.isEmpty() ? 0 : 3);
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
        if (hint != null) { Gui26.text(g, font, hint, 6, y, DIM, true); y += 10; }
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

    private static String roman(int n) {
        String[] r = {"", "I", "II", "III", "IV", "V"};
        return n >= 1 && n < r.length ? r[n] : String.valueOf(n);
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
