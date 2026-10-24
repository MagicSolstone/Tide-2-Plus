package com.li64.tide.events;

import com.li64.tide.data.item.TideTooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TideClientEventHandler {
    /** Tide's tooltip lines wrap at about this many characters (a rig's bonus would otherwise run across the screen). */
    public static final int WRAP = 42;

    public static void onTooltipRender(ItemStack stack, List<Component> lines) {
        List<Component> ours = new ArrayList<>();
        TideTooltips.add(stack, ours, Minecraft.getInstance().level != null);
        for (Component c : ours) lines.addAll(wrap(c, WRAP));
    }

    /** Splits a long line at spaces, keeping each word's colour; the lines after the first are indented a little. */
    public static List<Component> wrap(Component line, int max) {
        if (line.getString().length() <= max) return List.of(line);
        record Word(String text, Style style) {}
        List<Word> words = new ArrayList<>();
        line.visit((style, text) -> {
            StringBuilder cur = new StringBuilder();
            for (char ch : text.toCharArray()) {
                cur.append(ch);
                if (ch == ' ') { words.add(new Word(cur.toString(), style)); cur.setLength(0); }
            }
            if (!cur.isEmpty()) words.add(new Word(cur.toString(), style));
            return java.util.Optional.empty();
        }, Style.EMPTY);
        List<Component> out = new ArrayList<>();
        MutableComponent cur = Component.empty();
        int len = 0;
        for (Word w : words) {
            if (len > 0 && len + w.text.stripTrailing().length() > max) {
                out.add(cur);
                cur = Component.literal("  ");
                len = 2;
            }
            cur.append(Component.literal(w.text).withStyle(w.style));
            len += w.text.length();
        }
        if (len > 0) out.add(cur);
        return out;
    }
}
