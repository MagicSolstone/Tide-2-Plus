package com.li64.tide.registries.entities.misc.fishing;

import com.li64.tide.Tide;
import com.li64.tide.registries.TideEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A stand-in vanilla hook set as {@code player.fishing} so vanilla and other mods see the player as
 * fishing. It's never added to the world, so it never gets a real entity id; 26.x throws if anything
 * asks for one (other mods do, e.g. LambDynamicLights when it's removed), so it gets its own
 * negative placeholder id, which can't clash with a real entity's.
 */
public class HookAccessor extends FishingHook {
    private static final java.util.concurrent.atomic.AtomicInteger PLACEHOLDER_IDS = new java.util.concurrent.atomic.AtomicInteger(-1000);
    private TideFishingHook hook = null;

    public HookAccessor(TideFishingHook hook, Level level) {
        super(TideEntityTypes.HOOK_ACCESSOR, level);
        this.setId(PLACEHOLDER_IDS.getAndDecrement());
        this.hook = hook;
    }

    public HookAccessor(EntityType<? extends FishingHook> entityType, Level level) {
        super(entityType, level);
        this.setId(PLACEHOLDER_IDS.getAndDecrement());
    }

    private boolean checkIsUnlinked() {
        if (hook != null) return false;
        Tide.LOG.error("Hook accessor unexpectedly unlinked from Tide fishing hook!");
        return true;
    }

    public static boolean bobberRemoved(Player player) {
        return player == null || player.fishing == null || getHook(player) == null;
    }

    public void clearHook(Player player) {
        this.hook = null;
        player.fishing = null;
        super.remove(RemovalReason.DISCARDED);
    }

    public boolean isFishing() {
        return hook != null;
    }

    public static TideFishingHook getHook(Player player) {
        if (player.fishing instanceof HookAccessor accessor) return accessor.hook;
        return null;
    }

    @Override
    public boolean isOpenWaterFishing() {
        if (this.checkIsUnlinked()) return false;
        return hook.isOpenWaterFishing();
    }

    @Override
    public double getX() {
        if (this.checkIsUnlinked()) return 0.0;
        return hook.getX();
    }

    @Override
    public double getY() {
        if (this.checkIsUnlinked()) return 0.0;
        return hook.getY();
    }

    @Override
    public double getZ() {
        if (this.checkIsUnlinked()) return 0.0;
        return hook.getZ();
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        if (hook != null) hook.remove(reason);
        super.remove(reason);
    }

    @Override
    public @NotNull Vec3 position() {
        if (this.checkIsUnlinked()) return new Vec3(0, 0, 0);
        return hook.position();
    }

    @Override
    public @NotNull BlockPos blockPosition() {
        if (this.checkIsUnlinked()) return new BlockPos(0, 0, 0);
        return hook.blockPosition();
    }

    @Nullable
    @Override
    public Player getPlayerOwner() {
        if (hook == null) return null;
        return hook.getPlayerOwner();
    }
}
