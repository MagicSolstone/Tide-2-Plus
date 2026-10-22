package net.minecraft.world.entity.animal.cow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.ClientAsset$ResourceTexture;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.PriorityProvider$Selector;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public record CowVariant(ModelAndTexture<CowVariant$ModelType> modelAndTexture, ClientAsset$ResourceTexture babyTexture, SpawnPrioritySelectors spawnConditions)
   implements PriorityProvider<SpawnContext, SpawnCondition> {
   public static final Codec<CowVariant> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ModelAndTexture.codec(CowVariant$ModelType.CODEC, CowVariant$ModelType.NORMAL).forGetter(CowVariant::modelAndTexture),
            ClientAsset$ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(CowVariant::babyTexture),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(CowVariant::spawnConditions)
         )
         .apply(i, CowVariant::new)
   );
   public static final Codec<CowVariant> NETWORK_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ModelAndTexture.codec(CowVariant$ModelType.CODEC, CowVariant$ModelType.NORMAL).forGetter(CowVariant::modelAndTexture),
            ClientAsset$ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(CowVariant::babyTexture)
         )
         .apply(i, CowVariant::new)
   );
   public static final Codec<Holder<CowVariant>> CODEC = RegistryCodecs.holder(Registries.COW_VARIANT);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CowVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.COW_VARIANT);

   private CowVariant(final ModelAndTexture<CowVariant$ModelType> assetInfo, final ClientAsset$ResourceTexture babyTexture) {
      this(assetInfo, babyTexture, SpawnPrioritySelectors.EMPTY);
   }

   @Override
   public List<PriorityProvider$Selector<SpawnContext, SpawnCondition>> selectors() {
      return this.spawnConditions.selectors();
   }
}
