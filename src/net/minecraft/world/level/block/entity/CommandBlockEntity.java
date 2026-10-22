package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap$Builder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CommandBlockEntity extends BlockEntity {
   private static final boolean DEFAULT_POWERED = false;
   private static final boolean DEFAULT_CONDITION_MET = false;
   private static final boolean DEFAULT_AUTOMATIC = false;
   private boolean powered = false;
   private boolean auto = false;
   private boolean conditionMet = false;
   private final BaseCommandBlock commandBlock = new CommandBlockEntity$1(this);

   public CommandBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
      super(BlockEntityTypes.COMMAND_BLOCK, worldPosition, blockState);
   }

   @Override
   protected void saveAdditional(final ValueOutput output) {
      super.saveAdditional(output);
      this.commandBlock.save(output);
      output.putBoolean("powered", this.isPowered());
      output.putBoolean("conditionMet", this.wasConditionMet());
      output.putBoolean("auto", this.isAutomatic());
   }

   @Override
   protected void loadAdditional(final ValueInput input) {
      super.loadAdditional(input);
      this.commandBlock.load(input);
      this.powered = input.getBooleanOr("powered", false);
      this.conditionMet = input.getBooleanOr("conditionMet", false);
      this.setAutomatic(input.getBooleanOr("auto", false));
   }

   public BaseCommandBlock getCommandBlock() {
      return this.commandBlock;
   }

   public void setPowered(final boolean powered) {
      this.powered = powered;
   }

   public boolean isPowered() {
      return this.powered;
   }

   public boolean isAutomatic() {
      return this.auto;
   }

   public void setAutomatic(final boolean auto) {
      boolean previousAuto = this.auto;
      this.auto = auto;
      if (!previousAuto && auto && !this.powered && this.level != null && this.getMode() != CommandBlockEntity$Mode.SEQUENCE) {
         this.scheduleTick();
      }
   }

   public void onModeSwitch() {
      CommandBlockEntity$Mode newMode = this.getMode();
      if (newMode == CommandBlockEntity$Mode.AUTO && (this.powered || this.auto) && this.level != null) {
         this.scheduleTick();
      }
   }

   private void scheduleTick() {
      Block commandBlock = this.getBlockState().getBlock();
      if (commandBlock instanceof CommandBlock) {
         this.markConditionMet();
         this.level.scheduleTick(this.worldPosition, commandBlock, 1);
      }
   }

   public boolean wasConditionMet() {
      return this.conditionMet;
   }

   public boolean markConditionMet() {
      this.conditionMet = true;
      if (this.isConditional()) {
         BlockPos relative = this.worldPosition.relative(this.level.getBlockState(this.worldPosition).getValue(CommandBlock.FACING).getOpposite());
         if (this.level.getBlockState(relative).getBlock() instanceof CommandBlock) {
            this.conditionMet = this.level.getBlockEntity(relative) instanceof CommandBlockEntity commandBlockEntity
               && commandBlockEntity.getCommandBlock().getSuccessCount() > 0;
         } else {
            this.conditionMet = false;
         }
      }

      return this.conditionMet;
   }

   public CommandBlockEntity$Mode getMode() {
      BlockState state = this.getBlockState();
      if (state.is(Blocks.COMMAND_BLOCK)) {
         return CommandBlockEntity$Mode.REDSTONE;
      } else if (state.is(Blocks.REPEATING_COMMAND_BLOCK)) {
         return CommandBlockEntity$Mode.AUTO;
      } else {
         return state.is(Blocks.CHAIN_COMMAND_BLOCK) ? CommandBlockEntity$Mode.SEQUENCE : CommandBlockEntity$Mode.REDSTONE;
      }
   }

   public boolean isConditional() {
      BlockState blockState = this.level.getBlockState(this.getBlockPos());
      return blockState.getBlock() instanceof CommandBlock ? blockState.getValue(CommandBlock.CONDITIONAL) : false;
   }

   @Override
   protected void applyImplicitComponents(final DataComponentGetter components) {
      super.applyImplicitComponents(components);
      this.commandBlock.setCustomName(components.get(DataComponents.CUSTOM_NAME));
   }

   @Override
   protected void collectImplicitComponents(final DataComponentMap$Builder components) {
      super.collectImplicitComponents(components);
      components.set(DataComponents.CUSTOM_NAME, this.commandBlock.getCustomName());
   }

   @Override
   public void removeComponentsFromTag(final ValueOutput output) {
      super.removeComponentsFromTag(output);
      output.discard("CustomName");
      output.discard("conditionMet");
      output.discard("powered");
   }
}
