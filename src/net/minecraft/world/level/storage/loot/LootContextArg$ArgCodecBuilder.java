package net.minecraft.world.level.storage.loot;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs$LateBoundIdMapper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class LootContextArg$ArgCodecBuilder<R> {
   private final ExtraCodecs$LateBoundIdMapper<String, LootContextArg<R>> sources = new ExtraCodecs$LateBoundIdMapper<>();

   private LootContextArg$ArgCodecBuilder() {
   }

   public <T> LootContextArg$ArgCodecBuilder<R> anyOf(
      final T[] targets, final Function<T, String> nameGetter, final Function<T, ? extends LootContextArg<R>> argFactory
   ) {
      for (T target : targets) {
         this.sources.put(nameGetter.apply(target), (LootContextArg<R>)argFactory.apply(target));
      }

      return this;
   }

   public <T extends StringRepresentable> LootContextArg$ArgCodecBuilder<R> anyOf(final T[] targets, final Function<T, ? extends LootContextArg<R>> argFactory) {
      return this.anyOf(targets, StringRepresentable::getSerializedName, argFactory);
   }

   public <T extends StringRepresentable & LootContextArg<? extends R>> LootContextArg$ArgCodecBuilder<R> anyOf(final T[] targets) {
      return this.anyOf(targets, x$0 -> LootContextArg.cast((LootContextArg<? extends R>)x$0));
   }

   public LootContextArg$ArgCodecBuilder<R> anyEntity(final Function<? super ContextKey<? extends Entity>, ? extends LootContextArg<R>> function) {
      return this.anyOf(LootContext$EntityTarget.values(), target -> function.apply(target.contextParam()));
   }

   public LootContextArg$ArgCodecBuilder<R> anyBlockEntity(final Function<? super ContextKey<? extends BlockEntity>, ? extends LootContextArg<R>> function) {
      return this.anyOf(LootContext$BlockEntityTarget.values(), target -> function.apply(target.contextParam()));
   }

   public LootContextArg$ArgCodecBuilder<R> anyItemStack(final Function<? super ContextKey<? extends ItemInstance>, ? extends LootContextArg<R>> function) {
      return this.anyOf(LootContext$ItemStackTarget.values(), target -> function.apply(target.contextParam()));
   }

   public LootContextArg$ArgCodecBuilder<R> or(final String name, final LootContextArg<R> arg) {
      this.sources.put(name, arg);
      return this;
   }

   private Codec<LootContextArg<R>> build() {
      return this.sources.codec(Codec.STRING);
   }
}
