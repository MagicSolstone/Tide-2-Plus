package net.minecraft.world.entity.animal.wolf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.ClientAsset$ResourceTexture;

public record WolfVariant$AssetInfo(ClientAsset$ResourceTexture wild, ClientAsset$ResourceTexture tame, ClientAsset$ResourceTexture angry) {
   public static final Codec<WolfVariant$AssetInfo> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            ClientAsset$ResourceTexture.CODEC.fieldOf("wild").forGetter(WolfVariant$AssetInfo::wild),
            ClientAsset$ResourceTexture.CODEC.fieldOf("tame").forGetter(WolfVariant$AssetInfo::tame),
            ClientAsset$ResourceTexture.CODEC.fieldOf("angry").forGetter(WolfVariant$AssetInfo::angry)
         )
         .apply(instance, WolfVariant$AssetInfo::new)
   );
}
