package com.li64.tide.loaders.fabric;

import com.li64.tide.Tide;
import com.li64.tide.client.FishDisplayRenderer;
import com.li64.tide.client.TideItemModelProperties;
import com.li64.tide.client.VoidParticleSpawner;
import com.li64.tide.client.gui.TideMenuTypes;
import com.li64.tide.client.gui.screens.AnglingTableScreen;
import com.li64.tide.data.DoubleJumper;
import com.li64.tide.data.rods.ClientFishingRodTooltip;
import com.li64.tide.data.rods.FishingRodTooltip;
import com.li64.tide.events.TideClientEventHandler;
import com.li64.tide.registries.*;
import com.li64.tide.registries.particles.MagicChain;
import com.li64.tide.registries.particles.VoidRipple;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.function.Supplier;

public class FabricClientEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Tide.NETWORK.registerHandlers();

        MenuScreens.register(TideMenuTypes.ANGLING_TABLE, AnglingTableScreen::new);
        MenuScreens.register(TideMenuTypes.TACKLE_BOX, com.li64.tide.client.gui.screens.TackleBoxScreen::new);
        MenuScreens.<com.li64.tide.client.gui.menus.AnglingTableMenu, com.li64.tide.client.gui.screens.TackleRigScreen>register(TideMenuTypes.TACKLE_RIG, (m, inv, t) -> new com.li64.tide.client.gui.screens.TackleRigScreen((com.li64.tide.registries.items.TackleBoxItem.RigMenu) m, inv, t));

        ParticleProviderRegistry.getInstance().register(TideParticleTypes.VOID_RIPPLE_LARGE, VoidRipple.LargeProvider::new);
        ParticleProviderRegistry.getInstance().register(TideParticleTypes.VOID_RIPPLE_SMALL, VoidRipple.SmallProvider::new);
        ParticleProviderRegistry.getInstance().register(TideParticleTypes.MAGIC_CHAIN, MagicChain.Provider::new);

        ClientTickEvents.END_CLIENT_TICK.register(tick -> {
            if (tick.player == null) return;
            if (!tick.isPaused() && tick.player.getRandom().nextFloat() > 0.5f) {
                VoidParticleSpawner.spawnParticles(tick.player, 1);
            }
            if (tick.player.getItemBySlot(EquipmentSlot.FEET).is(TideItems.DRAGONFIN_BOOTS)
                    && tick.player instanceof DoubleJumper jumper) {
                if (!jumper.tide$hasDoubleJump() && tick.player.onGround()) jumper.tide$restoreDoubleJump();
                if (tick.player.input.keyPresses.jump()) jumper.tide$doDoubleJump();
                jumper.tide$setWasJumpPressed(tick.player.input.keyPresses.jump());
            }
        });

        ClientTooltipComponentCallback.EVENT.register((component) -> {
            //noinspection DeconstructionCanBeUsed
            if (component instanceof FishingRodTooltip tooltip)
                return new ClientFishingRodTooltip(tooltip.slots(), tooltip.contents());
            return null;
        });

        ItemTooltipCallback.EVENT.register((stack, ctx, flag, lines) -> TideClientEventHandler.onTooltipRender(stack, lines));

        TideItemModelProperties.registerAll();

        // minigame bar, cast bar, sonar and fishing info, drawn over the HUD
        net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(Tide.resource("overlays"),
                (graphics, deltaTracker) -> com.li64.tide.client.gui.TideGuiOverlays.render(graphics));

        // (jelly torches: 26.x picks cutout rendering from the texture by itself)

        BlockEntityRenderers.register(TideBlockEntities.FISH_DISPLAY, FishDisplayRenderer::new);

        TideEntityModels.init();
        TideEntityModels.RENDERERS.forEach(FabricClientEntrypoint::registerRenderer);
        TideEntityModels.LAYER_DEFS.forEach(FabricClientEntrypoint::registerLayer);
    }

    public static <T extends Entity> void registerRenderer(TideEntityModels.RendererRegistration<T> reg) {
        EntityRendererRegistry.register(reg.entityType(), reg.renderer());
    }

    public static void registerLayer(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        ModelLayerRegistry.registerModelLayer(location, definition::get);
    }
}