package net.minecraft.world.entity.animal.wolf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.PriorityProvider$Selector;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public record WolfVariant(WolfVariant$AssetInfo adultInfo, WolfVariant$AssetInfo babyInfo, SpawnPrioritySelectors spawnConditions)
   implements PriorityProvider<SpawnContext, SpawnCondition> {
   public static final Codec<WolfVariant> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            WolfVariant$AssetInfo.CODEC.fieldOf("assets").forGetter(WolfVariant::adultInfo),
            WolfVariant$AssetInfo.CODEC.fieldOf("baby_assets").forGetter(WolfVariant::babyInfo),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(WolfVariant::spawnConditions)
         )
         .apply(i, WolfVariant::new)
   );
   public static final Codec<WolfVariant> NETWORK_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            WolfVariant$AssetInfo.CODEC.fieldOf("assets").forGetter(WolfVariant::adultInfo),
            WolfVariant$AssetInfo.CODEC.fieldOf("baby_assets").forGetter(WolfVariant::babyInfo)
         )
         .apply(i, WolfVariant::new)
   );
   public static final Codec<Holder<WolfVariant>> CODEC = RegistryCodecs.holder(Registries.WOLF_VARIANT);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WolfVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.WOLF_VARIANT);

   private WolfVariant(final WolfVariant$AssetInfo adultInfo, final WolfVariant$AssetInfo babyInfo) {
      this(adultInfo, babyInfo, SpawnPrioritySelectors.EMPTY);
   }

   @Override
   public List<PriorityProvider$Selector<SpawnContext, SpawnCondition>> selectors() {
      return this.spawnConditions.selectors();
   }
}
