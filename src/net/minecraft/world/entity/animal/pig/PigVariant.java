package net.minecraft.world.entity.animal.pig;

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

public record PigVariant(ModelAndTexture<PigVariant$ModelType> modelAndTexture, ClientAsset$ResourceTexture babyTexture, SpawnPrioritySelectors spawnConditions)
   implements PriorityProvider<SpawnContext, SpawnCondition> {
   public static final Codec<PigVariant> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ModelAndTexture.codec(PigVariant$ModelType.CODEC, PigVariant$ModelType.NORMAL).forGetter(PigVariant::modelAndTexture),
            ClientAsset$ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(PigVariant::babyTexture),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(PigVariant::spawnConditions)
         )
         .apply(i, PigVariant::new)
   );
   public static final Codec<PigVariant> NETWORK_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ModelAndTexture.codec(PigVariant$ModelType.CODEC, PigVariant$ModelType.NORMAL).forGetter(PigVariant::modelAndTexture),
            ClientAsset$ResourceTexture.CODEC.fieldOf("baby_asset_id").forGetter(PigVariant::babyTexture)
         )
         .apply(i, PigVariant::new)
   );
   public static final Codec<Holder<PigVariant>> CODEC = RegistryCodecs.holder(Registries.PIG_VARIANT);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PigVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.PIG_VARIANT);

   private PigVariant(final ModelAndTexture<PigVariant$ModelType> assetInfo, final ClientAsset$ResourceTexture babyTexture) {
      this(assetInfo, babyTexture, SpawnPrioritySelectors.EMPTY);
   }

   @Override
   public List<PriorityProvider$Selector<SpawnContext, SpawnCondition>> selectors() {
      return this.spawnConditions.selectors();
   }
}
