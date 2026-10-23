package com.li64.tide.data.minigame;

import com.li64.tide.data.angler.Angler;
import com.li64.tide.data.journal.FishRarity;
import com.li64.tide.Tide;
import com.li64.tide.compat.CompatHelper;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.mediums.FishingMedium;
import com.li64.tide.network.messages.MinigameClientMsg;
import com.li64.tide.registries.TideItems;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import com.li64.tide.util.TideUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class FishCatchMinigame {
    private static final int SERVER_DELAY_MILLIS = 200;
    private static final ArrayList<FishCatchMinigame> ACTIVE_MINIGAMES = new ArrayList<>();
    private static final HashMap<ServerPlayer, Long> ACTIVE_DELAYS = new HashMap<>();

    private final TideFishingHook hook;
    private com.li64.tide.data.rods.RodEffects gear = com.li64.tide.data.rods.RodEffects.NONE;
    private final ServerPlayer player;
    private Angler.Zones zones;

    public static FishCatchMinigame getInstance(Player player) {
        for (FishCatchMinigame minigame : ACTIVE_MINIGAMES) {
            if (minigame.getPlayer() == player) return minigame;
        }
        return null;
    }

    public static FishCatchMinigame create(Player player) {
        FishCatchMinigame existingInstance = getInstance(player);
        if (existingInstance != null) return existingInstance;

        FishCatchMinigame minigame = new FishCatchMinigame((ServerPlayer) player);
        ACTIVE_MINIGAMES.add(minigame);
        return minigame;
    }

    public static boolean minigameActive(Player player) {
        for (FishCatchMinigame minigame : ACTIVE_MINIGAMES) {
            if (minigame.getPlayer() == player) return true;
        }
        return false;
    }

    protected FishCatchMinigame(ServerPlayer player) {
        this.player = player;
        this.hook = Objects.requireNonNull(HookAccessor.getHook(player));

        hook.setMinigameActive(true);
        Optional<FishData> data = TideUtils.getStrongest(hook.getHookedItems());
        int behavior = data.map(d -> d.behavior().ordinal()).orElse(0);
        float fishStrength = data.map(FishData::strength).orElse(0.2f); // miss area percentage
        float fishSpeed = data.map(FishData::speed).orElse(0.5f); // movements per second

        // lines and the rest of the gear: copper/golden lines slow the fish (gear.fight below), iron/diamond ease it
        fishStrength *= (float) Math.max(0.1, 1.0 - com.li64.tide.data.rods.RodEffects.of(player, hook.getRod()).ease());

        float area = Mth.clamp(1 - fishStrength, 0.05f, 1.0f);
        float speed = Math.max(fishSpeed / 20f * Tide.SERVER_CONFIG.minigame.minigameDifficultyMultiplier, 0.05f);

        // fishing level: how wide the good zones are, and how hard this fish fights for this angler
        int level = Angler.effectiveLevel(player);
        FishRarity rarity = data.map(d -> d.profile().rarity()).orElse(FishRarity.COMMON);
        com.li64.tide.data.rods.RodEffects gear = com.li64.tide.data.rods.RodEffects.of(player, hook.getRod());
        this.gear = gear;
        double zoneScale = Angler.Stats.of(level).hookZones() * (1.0 + gear.window());
        speed *= (float) (Angler.fightSpeed(level, rarity) * (1.0 + gear.fight()));

        // catch quality is decided at the bite, so a bigger fish fights harder: faster, smaller zones
        hook.pendingQuality = null;
        if (data.isPresent() && com.li64.tide.data.angler.CatchQuality.enabled()) {
            com.li64.tide.data.angler.CatchQuality rolled = com.li64.tide.data.angler.CatchQuality.roll(player.getRandom(), level, gear.quality());
            if (gear.upgrade() > 0 && rolled != com.li64.tide.data.angler.CatchQuality.MYTHIC && player.getRandom().nextDouble() < gear.upgrade())
                rolled = com.li64.tide.data.angler.CatchQuality.values()[rolled.ordinal() + 1];   // amethyst rod, golden bait
            final com.li64.tide.data.angler.CatchQuality q = rolled;
            hook.pendingQuality = q;
            speed *= (float) q.fightSpeed();
            zoneScale *= q.zoneScale();
            if (q.ordinal() >= com.li64.tide.data.angler.CatchQuality.TROPHY.ordinal())
                player.sendOverlayMessage(net.minecraft.network.chat.Component.literal(
                        q == com.li64.tide.data.angler.CatchQuality.MYTHIC ? "This one feels enormous!" : "Something big is on the line!")
                        .withStyle(s -> s.withColor(q.color)));
        }
        this.zones = Angler.Zones.of(area, zoneScale);

        // Start client minigame gui
        int type = 0;
        if (hook.getCurrentMedium() == FishingMedium.LAVA) type = 1;
        if (hook.getCurrentMedium() == FishingMedium.VOID) type = 2;
        Tide.NETWORK.sendToPlayer(new MinigameClientMsg((byte) 0, (byte) type, (byte) behavior, zones.good(), speed,
                zones.perfect(), zones.great(), zones.bad()), player);
    }

    public static boolean delayActive(ServerPlayer player) {
        if (!ACTIVE_DELAYS.containsKey(player)) return false;
        if (System.currentTimeMillis() > ACTIVE_DELAYS.get(player)) {
            ACTIVE_DELAYS.remove(player);
            return false;
        } else return true;
    }

    private ServerPlayer getPlayer() {
        return player;
    }

    public void onFinish() {
        if (cancelIfNecessary()) return;
        hook.setMinigameActive(false);
        Tide.NETWORK.sendToPlayer(new MinigameClientMsg(2), player);
        ACTIVE_MINIGAMES.remove(this);
        ACTIVE_DELAYS.put(player, System.currentTimeMillis() + SERVER_DELAY_MILLIS);
    }

    public void onTimeout() {
        if (cancelIfNecessary()) return;
        onFinish();
    }

    public void onFail() {
        if (cancelIfNecessary()) return;
        if (Tide.SERVER_CONFIG.minigame.doFailSound) hook.level().playSound(
                null, hook.getPlayerOwner().blockPosition(),
                SoundEvents.SHEEP_SHEAR, SoundSource.AMBIENT,
                0.9f, 1.0f);
        hook.invalidateCatch();
        hook.retrieve();
        onFinish();
    }

    public void onWin(boolean perfectCatch) {
        if (cancelIfNecessary()) return;
        if (Tide.SERVER_CONFIG.minigame.doSuccessSound) hook.level().playSound(
                null, hook.getPlayerOwner().blockPosition(),
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT,
                0.15f, 1.0f);
        if (CompatHelper.useStarcatcherMinigame())
            CompatHelper.starcatcherCompleteCatch(player, hook, perfectCatch);
        hook.retrieve(perfectCatch);
        onFinish();
    }

    public boolean cancelIfNecessary() {
        if (hook == null || player == null) {
            Tide.NETWORK.sendToPlayer(new MinigameClientMsg(2), player);
            ACTIVE_MINIGAMES.remove(this);
            return true;
        } else return false;
    }

    /** The player clicked: {@code accuracy} is how far the marker was from the centre (0..1). */
    public void handleHook(float accuracy) {
        if (Tide.CI) Tide.LOG.info("TIDE-CI hook set, accuracy {}", accuracy);
        if (cancelIfNecessary()) return;
        Angler.Hook hook = Angler.Hook.of(Math.abs(accuracy), zones);
        double chance = hook.landChance >= 1.0 || hook.landChance <= 0.0 ? hook.landChance : Math.min(1.0, hook.landChance + gear.land());
        boolean landed = chance >= 1.0 || player.getRandom().nextDouble() < chance;
        if (!landed && hook != Angler.Hook.TERRIBLE)
            player.sendOverlayMessage(net.minecraft.network.chat.Component.literal("The fish shook the hook loose!")
                    .withStyle(net.minecraft.ChatFormatting.RED));
        this.hook.hookResult = hook;
        if (landed) onWin(hook == Angler.Hook.PERFECT);
        else onFail();
    }

    public void handleClientEvent(byte event) {
        cancelIfNecessary();
        switch (event) {
            case 0 -> onTimeout();
            case 1 -> onFail();
            case 2 -> onWin(false);
            case 3 -> {
                if (hook != null) hook.hookResult = Angler.Hook.PERFECT;
                onWin(true);
            }
        }
    }
}
