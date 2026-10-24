package com.li64.tide.data.angler;

import com.li64.tide.Tide;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.player.TidePlayerData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

/**
 * The server's heaviest fish of each kind: who caught it, its weight and quality, saved with the world.
 * Beating one is announced to everyone with a bell. A kind's first catch sets its record quietly (otherwise the first
 * days would be nothing but announcements), and players' own journals fill in records from before this existed
 * when they join.
 */
public final class ServerRecords extends SavedData {
    public record Entry(String player, String uuid, double grams, int quality) {
        static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("player").forGetter(Entry::player),
                Codec.STRING.fieldOf("uuid").forGetter(Entry::uuid),
                Codec.DOUBLE.fieldOf("grams").forGetter(Entry::grams),
                Codec.INT.optionalFieldOf("quality", -1).forGetter(Entry::quality)
        ).apply(i, Entry::new));
    }

    private static final Codec<ServerRecords> CODEC = Codec.unboundedMap(Codec.STRING, Entry.CODEC)
            .xmap(ServerRecords::new, r -> new HashMap<>(r.records));
    public static final SavedDataType<ServerRecords> TYPE = new SavedDataType<>(Tide.resource("fish_records"), ServerRecords::new, CODEC,
            DataFixTypes.SAVED_DATA_COMMAND_STORAGE);

    private final Map<String, Entry> records = new HashMap<>();

    public ServerRecords() {}

    private ServerRecords(Map<String, Entry> records) {
        this.records.putAll(records);
    }

    private static ServerRecords of(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public static Entry get(MinecraftServer server, String species) {
        return of(server).records.get(species);
    }

    /** A graded catch: true (and announced) when it beats the server's record for its kind. */
    public static boolean onCatch(ServerPlayer player, ItemStack stack) {
        MinecraftServer server = player.level().getServer();
        double grams = TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0);
        if (server == null || grams <= 0) return false;
        String species = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        int quality = TideItemData.FISH_QUALITY.getOrDefault(stack, -1);
        ServerRecords r = of(server);
        Entry old = r.records.get(species);
        if (old != null && grams <= old.grams()) return false;
        r.records.put(species, new Entry(player.getScoreboardName(), player.getStringUUID(), grams, quality));
        r.setDirty();
        sendToEveryone(server);
        if (old == null) return false;
        announce(server, player, stack, grams, quality, old);
        return true;
    }

    /** The records as the journal shows them. */
    private static com.li64.tide.network.messages.RecordsSyncMsg message(ServerRecords r) {
        java.util.Map<String, com.li64.tide.network.messages.RecordsSyncMsg.Entry> out = new HashMap<>();
        r.records.forEach((k, e) -> out.put(k, new com.li64.tide.network.messages.RecordsSyncMsg.Entry(e.player(), e.grams(), e.quality())));
        return new com.li64.tide.network.messages.RecordsSyncMsg(out);
    }

    public static void sendTo(ServerPlayer player) {
        MinecraftServer server = player.level().getServer();
        if (server != null) Tide.NETWORK.sendToPlayer(message(of(server)), player);
    }

    private static void sendToEveryone(MinecraftServer server) {
        var msg = message(of(server));
        for (ServerPlayer p : server.getPlayerList().getPlayers()) Tide.NETWORK.sendToPlayer(msg, p);
    }

    /** Records from before this existed, from a player's journal (quietly). */
    public static void fillFromJournal(ServerPlayer player) {
        MinecraftServer server = player.level().getServer();
        if (server == null) return;
        ServerRecords r = of(server);
        TidePlayerData journal = TidePlayerData.getOrCreate(player);
        if (journal.fishPlayerData == null) return;
        boolean[] changed = {false};
        journal.fishPlayerData.forEach((holder, data) -> data.stats.ifPresent(st -> {
            if (st.getHeaviestCatch() <= 0) return;
            String species = holder.unwrapKey().map(k -> k.identifier().toString()).orElse(null);
            if (species == null) return;
            Entry old = r.records.get(species);
            if (old != null && old.grams() >= st.getHeaviestCatch()) return;
            r.records.put(species, new Entry(player.getScoreboardName(), player.getStringUUID(), st.getHeaviestCatch(), st.getHeaviestQuality()));
            changed[0] = true;
        }));
        if (changed[0]) {
            r.setDirty();
            sendToEveryone(server);
        }
    }

    /** A sound every player hears once, wherever they are. */
    public static void soundForEveryone(MinecraftServer server, net.minecraft.sounds.SoundEvent sound, float volume, float pitch) {
        var holder = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound);
        for (ServerPlayer p : server.getPlayerList().getPlayers())
            p.connection.send(new net.minecraft.network.protocol.game.ClientboundSoundPacket(holder, SoundSource.PLAYERS,
                    p.getX(), p.getY(), p.getZ(), volume, pitch, p.getRandom().nextLong()));
    }

    private static void announce(MinecraftServer server, ServerPlayer player, ItemStack stack, double grams, int quality, Entry old) {
        CatchQuality q = quality >= 0 ? CatchQuality.byId(quality) : null;
        int colour = q == null ? 0xFFFFFF : q.color;
        MutableComponent fish = Component.empty();
        if (q != null && q != CatchQuality.AVERAGE) fish.append(Component.literal(q.marks() + " " + q.label + " "));
        fish.append(Component.translatable(stack.getItem().getDescriptionId())).withStyle(s -> s.withColor(TextColor.fromRgb(colour)));
        boolean own = old.uuid().equals(player.getStringUUID());
        MutableComponent msg = Component.literal("🏆 ").withStyle(ChatFormatting.GOLD)
                .append(player.getDisplayName().copy().withStyle(ChatFormatting.WHITE))
                .append(Component.literal(" set the server record: ").withStyle(ChatFormatting.GRAY))
                .append(fish)
                .append(Component.literal(" " + FishWeight.imperial(grams)).withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" (beat " + (own ? "their own " : old.player() + "'s ") + FishWeight.imperial(old.grams()) + ")")
                        .withStyle(ChatFormatting.DARK_GRAY));
        server.getPlayerList().broadcastSystemMessage(msg, false);
        if (q != CatchQuality.MYTHIC) soundForEveryone(server, SoundEvents.BELL_BLOCK, 0.6f, 1.2f);   // a Mythic's fanfare already plays for all
        Tide.LOG.info("[Tide record] {} {} {}g (was {} {}g)", player.getScoreboardName(), BuiltInRegistries.ITEM.getKey(stack.getItem()), Math.round(grams), old.player(), Math.round(old.grams()));
    }
}
