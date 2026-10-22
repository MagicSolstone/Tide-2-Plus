package com.li64.tide.loaders;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface LoaderPlatform {
    String getPlatformName();

    default String getMCVersion() {
        return "1.21.1";
    }

    boolean isModLoaded(String modId);

    boolean isDevEnvironment();

    CompoundTag getPlayerData(ServerPlayer player);

    <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey);

    <T> T register(Registry<? super T> registry, Identifier id, T toRegister);
    <T> T register(Registry<? super T> registry, ResourceKey<T> id, T toRegister);
    <T> Holder<T> registerForHolder(Registry<T> registry, Identifier id, T toRegister);

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuType.MenuSupplier<T> menuSupplier, FeatureFlagSet flags);
}
