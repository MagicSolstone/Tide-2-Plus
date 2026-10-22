package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStackTemplate;

record ItemContainerContents$Slot(int index, ItemStackTemplate item) {
   public static final Codec<ItemContainerContents$Slot> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.intRange(0, 255).fieldOf("slot").forGetter(ItemContainerContents$Slot::index),
            ItemStackTemplate.CODEC.fieldOf("item").forGetter(ItemContainerContents$Slot::item)
         )
         .apply(i, ItemContainerContents$Slot::new)
   );
}
