package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.client.Gui26;
import com.google.common.collect.ImmutableList;
import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.data.player.CatchTimestamp;
import com.li64.tide.data.player.FishStats;
import com.li64.tide.data.angler.CatchQuality;
import com.li64.tide.data.angler.FishWeight;
import com.li64.tide.util.TideUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class StatsComponent extends ProfileComponent {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    private final List<Component> lines;

    public StatsComponent(FishStats stats, String species) {
        ImmutableList.Builder<Component> builder = ImmutableList.builder();
        // add total caught
        builder.add(Component.translatable("journal.info.stats.total", stats.getAmountCaught()));

        if (!stats.isEmpty()) {
            // add timestamp
            if (stats.getInitialCatchDate().isPresent()) {
                CatchTimestamp timestamp = stats.getInitialCatchDate().get();
                Component formatted;

                if (Tide.CLIENT_CONFIG.journal.useRealDate) {
                    Instant instant = timestamp.date();
                    ZonedDateTime localTime = instant.atZone(ZoneId.systemDefault());
                    formatted = Component.literal(localTime.format(DATE_FORMAT));
                }
                else formatted = Component.translatable("journal.info.stats.day", (int)(timestamp.ticks() / 24000L));

                builder.add(Component.translatable("journal.info.stats.first", formatted));
            }

            // best quality, heaviest, longest/shortest
            if (stats.getBestQuality() >= 0)
                builder.add(Component.translatable("journal.info.stats.best_quality",
                        CatchQuality.byId(stats.getBestQuality()).describe()));
            if (stats.getHeaviestCatch() > 0.0)
                builder.add(Component.translatable("journal.info.stats.heaviest",
                        FishWeight.format(stats.getHeaviestCatch())));
            if (stats.getLargestCatch() > 0.0) {
                builder.add(Component.translatable("journal.info.stats.largest",
                        TideUtils.getFormattedLength(stats.getLargestCatch())));
                builder.add(Component.translatable("journal.info.stats.smallest",
                        TideUtils.getFormattedLength(stats.getSmallestCatch())));
            }

            // how many of each quality, three to a line
            List<Component> counts = new java.util.ArrayList<>();
            for (CatchQuality q : CatchQuality.values()) {
                int n = stats.getQualityCount(q);
                if (n > 0) counts.add(Component.literal(n + " " + q.label)
                        .withStyle(st -> st.withColor(net.minecraft.network.chat.TextColor.fromRgb(darken(q.color)))));
            }
            for (int i = 0; i < counts.size(); i += 3) {
                net.minecraft.network.chat.MutableComponent line = Component.empty();
                for (int j = i; j < Math.min(i + 3, counts.size()); j++) {
                    if (j > i) line.append(Component.literal(" · "));
                    line.append(counts.get(j));
                }
                builder.add(line);
            }
        }
        // the server's record for this fish, whoever holds it
        var record = com.li64.tide.network.messages.RecordsSyncMsg.CLIENT.get(species);
        if (record != null && record.grams() > 0) {
            net.minecraft.network.chat.MutableComponent line = Component.literal("Server record: " + FishWeight.format(record.grams()));
            if (record.quality() >= 0 && record.quality() != CatchQuality.AVERAGE.ordinal()) {
                CatchQuality rq = CatchQuality.byId(record.quality());
                line.append(Component.literal(" " + rq.label).withStyle(st -> st.withColor(net.minecraft.network.chat.TextColor.fromRgb(darken(rq.color)))));
            }
            line.append(Component.literal(" by " + record.player()));
            builder.add(line);
        }
        this.lines = builder.build();
    }

    /** The quality colours are made for dark tooltips; deepen them for the paper page. */
    private static int darken(int rgb) {
        int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
        if (r > 200 && g > 200 && b > 200) return 0x8A6A52; // "Average" in journal ink
        return ((int) (r * 0.7) << 16) | ((int) (g * 0.7) << 8) | (int) (b * 0.7);
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + AREA_WIDTH / 2;
        int cursorY = 0;
        for (Component line : lines) {
            Gui26.text(graphics, font, line, center - font.width(line) / 2, y + cursorY, TEXT_COLOR, false);
            cursorY += 11;
        }
    }

    @Override
    public int getRequiredHeight() {
        return lines.size() * 11;
    }
}
