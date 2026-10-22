package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.HangingSignBlock$Attachment;

@Environment(EnvType.CLIENT)
public class HangingSignRenderState extends SignRenderState {
   public HangingSignBlock$Attachment attachmentType = HangingSignBlock$Attachment.CEILING;
}
