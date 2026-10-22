package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CopperChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.storage.loot.LootTable;

public record BlockTransformer(List<BlockTransformer$BlockTransformData> transforms) {
   public static final Codec<BlockTransformer> DIRECT_CODEC = BlockTransformer$BlockTransformData.CODEC
      .listOf(1, 200)
      .xmap(BlockTransformer::new, BlockTransformer::transforms);
   public static final Codec<Holder<BlockTransformer>> CODEC = RegistryCodecs.holder(Registries.BLOCK_TRANSFORMER);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BlockTransformer>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.BLOCK_TRANSFORMER);

   public InteractionResult transformBlock(final UseOnContext context) {
      if (playerHasBlockingItemUseIntent(context)) {
         return InteractionResult.PASS;
      } else {
         BlockPos pos = context.getClickedPos();
         Level level = context.getLevel();

         for (BlockTransformer$BlockTransformData transformData : this.transforms) {
            Direction clickedFace = context.getClickedFace();
            if (!transformData.disallowedFaces().contains(clickedFace)) {
               BlockState newBlockState = transformData.blockStateProvider.value().getOptionalState(level, level.getRandom(), pos);
               if (newBlockState != null) {
                  BlockState updatedShape = transformData.updateFromNeighbors ? Block.updateFromNeighbourShapes(newBlockState, level, pos) : newBlockState;
                  Player player = context.getPlayer();
                  ItemStack itemInHand = context.getItemInHand();
                  if (player instanceof ServerPlayer serverPlayer) {
                     CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemInHand);
                  }

                  BlockState oldBlockState = level.getBlockState(pos);
                  if (level instanceof ServerLevel serverLevel) {
                     transformData.loot
                        .ifPresent(
                           lt -> Block.dropFromBlockInteractLootTable(
                              serverLevel,
                              (ResourceKey<LootTable>)lt,
                              pos,
                              oldBlockState,
                              level.getBlockEntity(pos),
                              itemInHand,
                              player,
                              (sl, stack) -> transformData.dropStrategy.resourcePopper.pop(sl, pos, clickedFace, stack)
                           )
                        );
                  }

                  if (itemInHand.isStackable()) {
                     itemInHand.consume(transformData.consumeOnUse ? 1 : 0, player);
                  } else if (player != null) {
                     itemInHand.hurtAndBreak(transformData.itemDamagePerUse, player, context.getHand().asEquipmentSlot());
                  }

                  level.setBlock(pos, updatedShape, 11);
                  level.playSound(player, pos, transformData.sound.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                  transformData.particle.send(level, player, pos);
                  level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent$Context.of(player, updatedShape));
                  if (transformData.transformType == BlockTransformer$TransformType.COPPER_CHEST
                     && oldBlockState.getBlock() instanceof CopperChestBlock
                     && oldBlockState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
                     BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldBlockState);
                     level.gameEvent(GameEvent.BLOCK_CHANGE, neighborPos, GameEvent$Context.of(player, level.getBlockState(neighborPos)));
                     transformData.particle.send(level, player, neighborPos);
                  }

                  return InteractionResult.SUCCESS;
               }
            }
         }

         return InteractionResult.PASS;
      }
   }

   private static boolean playerHasBlockingItemUseIntent(final UseOnContext context) {
      Player player = context.getPlayer();
      return context.getHand().equals(InteractionHand.MAIN_HAND)
         && player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS)
         && !player.isSecondaryUseActive();
   }
}
