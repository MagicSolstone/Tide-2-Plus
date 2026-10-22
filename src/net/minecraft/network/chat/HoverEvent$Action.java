package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;

public enum HoverEvent$Action implements StringRepresentable {
   SHOW_TEXT("show_text", true, HoverEvent$ShowText.CODEC),
   SHOW_ITEM("show_item", true, HoverEvent$ShowItem.CODEC),
   SHOW_ENTITY("show_entity", true, HoverEvent$ShowEntity.CODEC);

   public static final Codec<HoverEvent$Action> UNSAFE_CODEC = StringRepresentable.fromValues(HoverEvent$Action::values);
   public static final Codec<HoverEvent$Action> CODEC = UNSAFE_CODEC.validate(HoverEvent$Action::filterForSerialization);
   private final String name;
   private final boolean allowFromServer;
   private final MapCodec<? extends HoverEvent> codec;

   private HoverEvent$Action(final String name, final boolean allowFromServer, final MapCodec<? extends HoverEvent> codec) {
      this.name = name;
      this.allowFromServer = allowFromServer;
      this.codec = codec;
   }

   public boolean isAllowedFromServer() {
      return this.allowFromServer;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   @Override
   public String toString() {
      return "<action " + this.name + ">";
   }

   private static DataResult<HoverEvent$Action> filterForSerialization(final HoverEvent$Action action) {
      return !action.isAllowedFromServer() ? DataResult.error(() -> "Action not allowed: " + action) : DataResult.success(action, Lifecycle.stable());
   }
}
