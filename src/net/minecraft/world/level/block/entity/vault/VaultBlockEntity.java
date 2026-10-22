package net.minecraft.world.level.block.entity.vault;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class VaultBlockEntity extends BlockEntity {
   private final VaultServerData serverData = new VaultServerData();
   private final VaultSharedData sharedData = new VaultSharedData();
   private final VaultClientData clientData = new VaultClientData();
   private VaultConfig config = VaultConfig.DEFAULT;

   public VaultBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
      super(BlockEntityTypes.VAULT, worldPosition, blockState);
   }

   @Nullable
   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public CompoundTag getUpdateTag(final HolderLookup$Provider registries) {
      return Util.make(
         new CompoundTag(), tag -> tag.store("shared_data", VaultSharedData.CODEC, registries.createSerializationContext(NbtOps.INSTANCE), this.sharedData)
      );
   }

   @Override
   protected void saveAdditional(final ValueOutput output) {
      super.saveAdditional(output);
      output.store("config", VaultConfig.CODEC, this.config);
      output.store("shared_data", VaultSharedData.CODEC, this.sharedData);
      output.store("server_data", VaultServerData.CODEC, this.serverData);
   }

   @Override
   protected void loadAdditional(final ValueInput input) {
      super.loadAdditional(input);
      input.<VaultServerData>read("server_data", VaultServerData.CODEC).ifPresent(this.serverData::set);
      this.config = input.<VaultConfig>read("config", VaultConfig.CODEC).orElse(VaultConfig.DEFAULT);
      input.<VaultSharedData>read("shared_data", VaultSharedData.CODEC).ifPresent(this.sharedData::set);
   }

   @Nullable
   public VaultServerData getServerData() {
      return this.level != null && !this.level.isClientSide() ? this.serverData : null;
   }

   public VaultSharedData getSharedData() {
      return this.sharedData;
   }

   public VaultClientData getClientData() {
      return this.clientData;
   }

   public VaultConfig getConfig() {
      return this.config;
   }

   @VisibleForTesting
   public void setConfig(final VaultConfig config) {
      this.config = config;
   }
}
