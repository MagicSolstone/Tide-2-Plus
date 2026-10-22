package net.minecraft.client.resources.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record EquipmentClientInfo$Layer(Identifier textureId, Optional<EquipmentClientInfo$Dyeable> dyeable, boolean usePlayerTexture) {
   public static final Codec<EquipmentClientInfo$Layer> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Identifier.CODEC.fieldOf("texture").forGetter(EquipmentClientInfo$Layer::textureId),
            EquipmentClientInfo$Dyeable.CODEC.optionalFieldOf("dyeable").forGetter(EquipmentClientInfo$Layer::dyeable),
            Codec.BOOL.optionalFieldOf("use_player_texture", false).forGetter(EquipmentClientInfo$Layer::usePlayerTexture)
         )
         .apply(i, EquipmentClientInfo$Layer::new)
   );

   public EquipmentClientInfo$Layer(final Identifier textureId) {
      this(textureId, Optional.empty(), false);
   }

   public static EquipmentClientInfo$Layer leatherDyeable(final Identifier textureId, final boolean dyeable) {
      return new EquipmentClientInfo$Layer(textureId, dyeable ? Optional.of(new EquipmentClientInfo$Dyeable(Optional.of(-6265536))) : Optional.empty(), false);
   }

   public static EquipmentClientInfo$Layer onlyIfDyed(final Identifier textureId, final boolean dyeable) {
      return new EquipmentClientInfo$Layer(textureId, dyeable ? Optional.of(new EquipmentClientInfo$Dyeable(Optional.empty())) : Optional.empty(), false);
   }

   public Identifier getTextureLocation(final EquipmentClientInfo$LayerType type) {
      return this.textureId.withPath(path -> "textures/entity/equipment/" + type.getSerializedName() + "/" + path + ".png");
   }
}
