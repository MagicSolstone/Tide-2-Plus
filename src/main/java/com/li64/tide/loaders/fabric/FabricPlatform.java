package com.li64.tide.loaders.fabric;

import com.li64.tide.data.TidePlayer;
import com.li64.tide.loaders.LoaderPlatform;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricPlatform implements LoaderPlatform {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public CompoundTag getPlayerData(ServerPlayer player) {
        return ((TidePlayer) player).tide$getTidePlayerData();
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.create(registryKey)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister();
    }

    @Override
    public <T> T register(Registry<? super T> registry, Identifier id, T toRegister) {
        return Registry.register(registry, id, toRegister);
    }

    @Override
    public <T> T register(Registry<? super T> registry, ResourceKey<T> id, T toRegister) {
        return Registry.register(registry, id.identifier(), toRegister);
    }

    @Override
    public <T> Holder.Reference<T> registerForHolder(Registry<T> registry, Identifier id, T toRegister) {
        return Registry.registerForHolder(registry, id, toRegister);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuType.MenuSupplier<T> menuSupplier, FeatureFlagSet flags) {
        return new MenuType<>(menuSupplier, flags);
    }
}