package com.li64.tide.data.angler;

import com.li64.tide.Tide;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.SizeData;
import com.li64.tide.data.item.TideItemData;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

/**
 * How good one caught fish is. Separate from rarity (how hard the species is to find):
 * a Mythic guppy and a Poor shark are both possible. Shown with ◆ marks, never stars.
 *
 * Base odds (level 40): Poor 30%, Average 48%, Fine 17%, Prime 1/25, Trophy ~1/111, Mythic 1/1000.
 * Higher levels push the odds up. Quality is decided when the fish bites, and better fish fight harder.
 */
public enum CatchQuality {
    POOR("Poor", 0x9A9A9A, 30.0),
    AVERAGE("Average", 0xE0E0E0, 48.0),
    FINE("Fine", 0x6BCB5A, 17.0),
    PRIME("Prime", 0x4FA3FF, 4.0),
    TROPHY("Trophy", 0xC77DFF, 0.9),
    MYTHIC("Mythic", 0xFFB020, 0.1);

    public static final Codec<CatchQuality> CODEC = Codec.INT.xmap(CatchQuality::byId, Enum::ordinal);

    public final String label;
    public final int color;
    public final double baseWeight;

    CatchQuality(String label, int color, double baseWeight) {
        this.label = label;
        this.color = color;
        this.baseWeight = baseWeight;
    }

    public static CatchQuality byId(int id) {
        return values()[Math.max(0, Math.min(values().length - 1, id))];
    }

    /** "◆◆◆◇◇" — one filled mark per step above Poor. */
    public String marks() {
        return "◆".repeat(ordinal()) + "◇".repeat(values().length - 1 - ordinal());
    }

    public MutableComponent describe() {
        return Component.literal(marks() + " " + label).withStyle(s -> s.withColor(TextColor.fromRgb(color)));
    }

    // ------------------------------------------------------------------ rolling

    /** Odds multiplier for the good tiers: 0.7 at level 1, 1.0 at the baseline, 1.5 at 99. */
    public static double luck(int level) {
        return Angler.curve(level, 0.7, 1.5);
    }

    /**
     * Chance of each quality (sums to 1) at a level. Mythic grows slowest, so it stays a story:
     * about 1 in 1,500 at level 1, 1 in 1,000 at the baseline, 1 in 700 at 99.
     */
    public static double[] odds(int level) {
        double m = luck(level);
        double[] w = new double[values().length];
        double total = 0;
        for (CatchQuality q : values()) {
            double scale = switch (q) {
                case POOR -> 1.0 / m;
                case AVERAGE -> 1.0;
                case FINE -> m;
                case PRIME -> Math.pow(m, 1.3);
                case TROPHY -> Math.pow(m, 1.5);
                case MYTHIC -> m;
            };
            w[q.ordinal()] = q.baseWeight * scale;
            total += w[q.ordinal()];
        }
        for (int i = 0; i < w.length; i++) w[i] /= total;
        return w;
    }

    /** How much faster this fish moves in the minigame than an average one of its kind. */
    public double fightSpeed() {
        return switch (this) {
            case POOR -> 0.9;
            case AVERAGE -> 1.0;
            case FINE -> 1.05;
            case PRIME -> 1.12;
            case TROPHY -> 1.2;
            case MYTHIC -> 1.3;
        };
    }

    /** How wide the good hook zones are against an average fish (big fish leave less room). */
    public double zoneScale() {
        return switch (this) {
            case POOR -> 1.1;
            case AVERAGE -> 1.0;
            case FINE -> 0.97;
            case PRIME -> 0.92;
            case TROPHY -> 0.85;
            case MYTHIC -> 0.78;
        };
    }

    public static CatchQuality roll(RandomSource rng, int level) {
        double[] p = odds(level);
        double r = rng.nextDouble();
        for (CatchQuality q : values()) {
            r -= p[q.ordinal()];
            if (r < 0) return q;
        }
        return AVERAGE;
    }

    // ------------------------------------------------------------------ size

    private static final double Z10 = -1.2815515655446004;

    /**
     * A length (cm) that fits the quality. Poor to Fine come from the species' normal size curve
     * (small, middling, upper end); Prime starts at the top of the typical range, Trophy runs to about
     * +25% over it and Mythic to about +45%.
     */
    public double sampleLength(SizeData size, RandomSource rng) {
        double lo = size.typicalLowCm(), hi = size.typicalHighCm();
        double sigma = (Math.log(hi) - Math.log(lo)) / (-2 * Z10);
        double mu = Math.log(lo) - Z10 * sigma;
        double u = rng.nextDouble();
        double length = switch (this) {
            case POOR -> Math.exp(mu + sigma * (-2.0 + 1.45 * u));
            case AVERAGE -> Math.exp(mu + sigma * (-0.55 + 1.2 * u));
            case FINE -> Math.exp(mu + sigma * (0.65 + 0.63 * u));
            case PRIME -> hi * (1.0 + 0.1 * u);
            case TROPHY -> hi * (1.1 + 0.15 * u);
            case MYTHIC -> hi * (1.3 + 0.15 * u);
        };
        double min = size.recordLowCm().orElse(0.6 * lo);
        return Math.max(length, min);
    }

    /** Well-fed fish are heavier for their length. */
    public double condition() {
        return switch (this) {
            case POOR -> 0.85;
            case AVERAGE -> 1.0;
            case FINE -> 1.04;
            case PRIME -> 1.08;
            case TROPHY -> 1.1;
            case MYTHIC -> 1.12;
        };
    }

    public int bonusXp() {
        return switch (this) {
            case POOR, AVERAGE -> 0;
            case FINE -> 5;
            case PRIME -> 20;
            case TROPHY -> 60;
            case MYTHIC -> 250;
        };
    }

    // ------------------------------------------------------------------ applying to a catch

    public static boolean enabled() {
        return Tide.SERVER_CONFIG == null || Tide.SERVER_CONFIG.leveling.catchQuality;
    }

    /** Roll quality for a freshly caught fish and set its quality, length, weight and name. */
    /** {@code rolled}: the quality decided at the bite (null: roll now, e.g. with the minigame off). */
    public static void apply(ServerPlayer player, ItemStack stack, CatchQuality rolled) {
        if (!enabled()) return;
        FishData data = FishData.get(stack).orElse(null);
        if (data == null) return;
        RandomSource rng = player.getRandom();
        CatchQuality q = rolled != null ? rolled : roll(rng, Angler.effectiveLevel(player));

        if (data.size().isPresent()) {
            double length = q.sampleLength(data.size().get(), rng);
            TideItemData.FISH_LENGTH.set(stack, length);
            double grams = FishWeight.grams(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath(), length, q, rng);
            TideItemData.FISH_WEIGHT.set(stack, grams);
        }
        mark(stack, q);
        if (q.ordinal() >= TROPHY.ordinal()) announce(player, stack, q);
    }

    /** Quality onto a fish item: the component, and the name ("Fine Bluegill", coloured; Average keeps the plain name). */
    public static void mark(ItemStack stack, CatchQuality q) {
        TideItemData.FISH_QUALITY.set(stack, q.ordinal());
        if (q != AVERAGE) {
            stack.set(DataComponents.ITEM_NAME, Component.translatable("item.tide.quality_name",
                    Component.literal(q.label), Component.translatable(stack.getItem().getDescriptionId()))
                    .withStyle(s -> s.withColor(TextColor.fromRgb(q.color)).withItalic(false)));
        }
    }

    private static void announce(ServerPlayer player, ItemStack stack, CatchQuality q) {
        player.level().playSound(null, player.blockPosition(),
                q == MYTHIC ? SoundEvents.UI_TOAST_CHALLENGE_COMPLETE : SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS, 0.8f, q == MYTHIC ? 1.0f : 1.6f);
        MinecraftServer server = player.level().getServer();
        if (server == null) return;
        MutableComponent msg = Component.literal("🎣 ").withStyle(ChatFormatting.AQUA)
                .append(player.getDisplayName().copy().withStyle(ChatFormatting.WHITE))
                .append(Component.literal(" landed a ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(q.marks() + " " + q.label + " ").withStyle(s -> s.withColor(TextColor.fromRgb(q.color))))
                .append(Component.translatable(stack.getItem().getDescriptionId()).withStyle(s -> s.withColor(TextColor.fromRgb(q.color))));
        double grams = TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0);
        double length = TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0);
        if (length > 0) msg.append(Component.literal(" (" + FishWeight.imperialLength(length)
                + (grams > 0 ? ", " + FishWeight.imperial(grams) : "") + ")").withStyle(ChatFormatting.GRAY));
        msg.append(Component.literal("!").withStyle(ChatFormatting.GRAY));
        server.getPlayerList().broadcastSystemMessage(msg, false);
    }
}
