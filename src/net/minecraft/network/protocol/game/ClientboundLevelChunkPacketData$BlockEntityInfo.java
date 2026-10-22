package net.minecraft.network.protocol.game;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

record ClientboundLevelChunkPacketData$BlockEntityInfo(byte packedXZ, short y, BlockEntityType<?> type, Optional<CompoundTag> tag) {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelChunkPacketData$BlockEntityInfo> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BYTE,
      ClientboundLevelChunkPacketData$BlockEntityInfo::packedXZ,
      ByteBufCodecs.SHORT,
      ClientboundLevelChunkPacketData$BlockEntityInfo::y,
      ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE),
      ClientboundLevelChunkPacketData$BlockEntityInfo::type,
      ByteBufCodecs.OPTIONAL_COMPOUND_TAG,
      ClientboundLevelChunkPacketData$BlockEntityInfo::tag,
      ClientboundLevelChunkPacketData$BlockEntityInfo::new
   );

   private static ClientboundLevelChunkPacketData$BlockEntityInfo create(final BlockEntity blockEntity) {
      CompoundTag tag = blockEntity.getUpdateTag(blockEntity.getLevel().registryAccess());
      BlockPos pos = blockEntity.getBlockPos();
      int xz = SectionPos.sectionRelative(pos.getX()) << 4 | SectionPos.sectionRelative(pos.getZ());
      return new ClientboundLevelChunkPacketData$BlockEntityInfo(
         (byte)xz, (short)pos.getY(), blockEntity.getType(), tag.isEmpty() ? Optional.empty() : Optional.of(tag)
      );
   }
}
