package net.minecraft.client.gui.components.debug;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.TypedInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property$Value;

@Environment(EnvType.CLIENT)
public abstract class DebugEntryLookingAt$DebugEntryLookingAtState<OwnerType, StateType extends StateHolder<OwnerType, StateType> & TypedInstance<OwnerType>>
   extends DebugEntryLookingAt {
   private final String prefix;

   protected DebugEntryLookingAt$DebugEntryLookingAtState(final String prefix) {
      this.prefix = prefix;
   }

   protected abstract StateType getInstance(Level level, BlockPos pos);

   @Override
   public void extractInfo(final List<String> result, final Level level, final BlockPos pos) {
      StateType stateInstance = this.getInstance(level, pos);
      result.add(ChatFormatting.UNDERLINE + this.prefix + ": " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
      result.add(stateInstance.typeHolder().getRegisteredName());
      addStateProperties(result, stateInstance);
   }

   private static void addStateProperties(final List<String> result, final StateHolder<?, ?> stateHolder) {
      stateHolder.getValues().forEach(entry -> result.add(getPropertyValueString((Property$Value<?>)entry)));
   }

   private static String getPropertyValueString(final Property$Value<?> entry) {
      String valueString = entry.valueName();
      if (Boolean.TRUE.equals(entry.value())) {
         valueString = ChatFormatting.GREEN + valueString;
      } else if (Boolean.FALSE.equals(entry.value())) {
         valueString = ChatFormatting.RED + valueString;
      }

      return entry.property().getName() + ": " + valueString;
   }
}
