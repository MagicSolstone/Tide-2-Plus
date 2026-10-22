package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.PlainSignBlock$Attachment;

@Environment(EnvType.CLIENT)
public class StandingSignRenderState extends SignRenderState {
   public PlainSignBlock$Attachment attachmentType = PlainSignBlock$Attachment.GROUND;
}
