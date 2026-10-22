package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStackTemplate;

public record HoverEvent$ShowItem(ItemStackTemplate item) implements HoverEvent {
   public static final MapCodec<HoverEvent$ShowItem> CODEC = ItemStackTemplate.MAP_CODEC.xmap(HoverEvent$ShowItem::new, HoverEvent$ShowItem::item);

   @Override
   public HoverEvent$Action action() {
      return HoverEvent$Action.SHOW_ITEM;
   }
}
