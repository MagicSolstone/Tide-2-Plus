package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CopperGolemStatueBlock$Pose;
import net.minecraft.world.level.block.WeatheringCopper$WeatherState;

@Environment(EnvType.CLIENT)
public class CopperGolemStatueRenderState extends BlockEntityRenderState {
   public CopperGolemStatueBlock$Pose pose = CopperGolemStatueBlock$Pose.STANDING;
   public Direction direction = Direction.NORTH;
   public WeatheringCopper$WeatherState oxidationState = WeatheringCopper$WeatherState.UNAFFECTED;
}
