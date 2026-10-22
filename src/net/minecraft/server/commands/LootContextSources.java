package net.minecraft.server.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class LootContextSources {
   private static ArgumentBuilder<CommandSourceStack, ?> decorate(
      final ArgumentBuilder<CommandSourceStack, ?> node, final LootContextSources$NodeVisitor nodeVisitor, final LootContextSources$ContextDecorator decorator
   ) {
      nodeVisitor.visit(decorator, node::then);
      return node;
   }

   public static <T extends ArgumentBuilder<CommandSourceStack, T>> T addContextSources(final T node, final LootContextSources$NodeVisitor factory) {
      return (T)node.then(decorate(Commands.literal("default"), factory, (var0, params) -> params.create(LootContextParamSets.COMMAND_COMPUTE_DEFAULT)))
         .then(
            Commands.literal("block")
               .then(
                  decorate(
                     Commands.argument("computePos", BlockPosArgument.blockPos()),
                     factory,
                     (context, params) -> {
                        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "computePos");
                        CommandSourceStack source = (CommandSourceStack)context.getSource();
                        ServerLevel level = source.getLevel();
                        BlockState blockState = level.getBlockState(pos);
                        BlockEntity blockEntity = level.getBlockEntity(pos);
                        return params.withParameter(LootContextParams.BLOCK_STATE, blockState)
                           .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                           .create(LootContextParamSets.COMMAND_COMPUTE_POSITION);
                     }
                  )
               )
         )
         .then(Commands.literal("entity").then(decorate(Commands.argument("computeTarget", EntityArgument.entity()), factory, (context, params) -> {
            Entity target = EntityArgument.getEntity(context, "computeTarget");
            return params.withParameter(LootContextParams.TARGET_ENTITY, target).create(LootContextParamSets.COMMAND_COMPUTE_ENTITY);
         })));
   }
}
