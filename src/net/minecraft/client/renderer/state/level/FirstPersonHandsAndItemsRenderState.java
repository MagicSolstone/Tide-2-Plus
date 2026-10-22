package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class FirstPersonHandsAndItemsRenderState {
   public InteractionHand attackHand;
   public float viewXRot;
   public float viewYRot;
   public float xBob;
   public float yBob;
   public boolean isScoping;
   public int useItemRemainingTicks;
   public int mainHandUseDuration;
   public int offHandUseDuration;
   public int mainHandChargeDuration;
   public int offHandChargeDuration;
   public float mainHandSwapScale;
   public float offHandSwapScale;
   public FirstPersonHandsAndItemsRenderState$HandRenderSelection handRenderSelection;
   public ItemStack mainHandItem = ItemStack.EMPTY;
   public ItemStack offHandItem = ItemStack.EMPTY;
   public float mainHandHeight;
   public float oldMainHandHeight;
   public float offHandHeight;
   public float oldOffHandHeight;
   public final ItemStackRenderState mainHandRenderState = new ItemStackRenderState();
   public final ItemStackRenderState offHandRenderState = new ItemStackRenderState();
   public final MapRenderState mainHandMapRenderState = new MapRenderState();
   public final MapRenderState offHandMapRenderState = new MapRenderState();
   public boolean hasMainHandMapData;
   public boolean hasOffHandMapData;
}
