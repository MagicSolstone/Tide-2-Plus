package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

public record ClientboundRecipeBookAddPacket$Entry(RecipeDisplayEntry contents, byte flags) {
   public static final byte FLAG_NOTIFICATION = 1;
   public static final byte FLAG_HIGHLIGHT = 2;
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRecipeBookAddPacket$Entry> STREAM_CODEC = StreamCodec.composite(
      RecipeDisplayEntry.STREAM_CODEC,
      ClientboundRecipeBookAddPacket$Entry::contents,
      ByteBufCodecs.BYTE,
      ClientboundRecipeBookAddPacket$Entry::flags,
      ClientboundRecipeBookAddPacket$Entry::new
   );

   public ClientboundRecipeBookAddPacket$Entry(final RecipeDisplayEntry contents, final boolean notification, final boolean highlight) {
      this(contents, (byte)((notification ? 1 : 0) | (highlight ? 2 : 0)));
   }

   public boolean notification() {
      return (this.flags & 1) != 0;
   }

   public boolean highlight() {
      return (this.flags & 2) != 0;
   }
}
