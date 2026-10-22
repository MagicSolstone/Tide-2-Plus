package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.CollectionPredicate;
import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.component.WritableBookContent;

public record WritableBookPredicate(Optional<CollectionPredicate<Filterable<String>, WritableBookPredicate$PagePredicate>> pages)
   implements SingleComponentItemPredicate<WritableBookContent> {
   public static final Codec<WritableBookPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(CollectionPredicate.codec(WritableBookPredicate$PagePredicate.CODEC).optionalFieldOf("pages").forGetter(WritableBookPredicate::pages))
         .apply(i, WritableBookPredicate::new)
   );

   @Override
   public DataComponentType<WritableBookContent> componentType() {
      return DataComponents.WRITABLE_BOOK_CONTENT;
   }

   public boolean matches(final WritableBookContent value) {
      return !this.pages.isPresent() || this.pages.get().test(value.pages());
   }
}
