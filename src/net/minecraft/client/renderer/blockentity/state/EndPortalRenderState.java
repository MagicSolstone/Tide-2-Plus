package net.minecraft.client.renderer.blockentity.state;

import java.util.EnumSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class EndPortalRenderState extends BlockEntityRenderState {
   public final Set<Direction> facesToShow = EnumSet.noneOf(Direction.class);
}
