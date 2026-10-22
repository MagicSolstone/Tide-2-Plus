package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

public record SulfurCubeArchetype(
   HolderSet<Item> items,
   List<SulfurCubeArchetype$AttributeEntry> attributeModifiers,
   boolean buoyant,
   Optional<SulfurCubeArchetype$ExplosionData> explosion,
   Optional<SulfurCubeArchetype$ContactDamage> contactDamage,
   SulfurCubeArchetype$KnockbackModifiers knockbackModifiers,
   SulfurCubeArchetype$SoundSettings soundSettings
) {
   public static final Codec<SulfurCubeArchetype> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.ITEM).fieldOf("items").forGetter(SulfurCubeArchetype::items),
            SulfurCubeArchetype$AttributeEntry.CODEC.listOf().fieldOf("attribute_modifiers").forGetter(SulfurCubeArchetype::attributeModifiers),
            Codec.BOOL.optionalFieldOf("buoyant", false).forGetter(SulfurCubeArchetype::buoyant),
            SulfurCubeArchetype$ExplosionData.CODEC.optionalFieldOf("explosion").forGetter(SulfurCubeArchetype::explosion),
            SulfurCubeArchetype$ContactDamage.CODEC.optionalFieldOf("contact_damage").forGetter(SulfurCubeArchetype::contactDamage),
            SulfurCubeArchetype$KnockbackModifiers.CODEC.fieldOf("knockback_modifiers").forGetter(SulfurCubeArchetype::knockbackModifiers),
            SulfurCubeArchetype$SoundSettings.CODEC.fieldOf("sound_settings").forGetter(SulfurCubeArchetype::soundSettings)
         )
         .apply(i, SulfurCubeArchetype::new)
   );
   public static SulfurCubeArchetype$KnockbackModifiers DEFAULT_KNOCKBACK_MODIFIERS = new SulfurCubeArchetype$KnockbackModifiers(0.33F, 0.06F);
   public static SulfurCubeArchetype$SoundSettings DEFAULT_SOUND_SETTINGS = new SulfurCubeArchetype$SoundSettings(
      SoundEvents.SULFUR_CUBE_REGULAR_HIT, SoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.2F, 0.5F
   );
}
