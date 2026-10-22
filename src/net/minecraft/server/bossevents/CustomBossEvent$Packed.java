package net.minecraft.server.bossevents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.BossEvent$BossBarColor;
import net.minecraft.world.BossEvent$BossBarOverlay;

public record CustomBossEvent$Packed(
   Component name,
   boolean visible,
   int value,
   int max,
   BossEvent$BossBarColor color,
   BossEvent$BossBarOverlay overlay,
   boolean darkenScreen,
   boolean playBossMusic,
   boolean createWorldFog,
   Set<UUID> players
) {
   public static final Codec<CustomBossEvent$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ComponentSerialization.CODEC.fieldOf("Name").forGetter(CustomBossEvent$Packed::name),
            Codec.BOOL.optionalFieldOf("Visible", false).forGetter(CustomBossEvent$Packed::visible),
            Codec.INT.optionalFieldOf("Value", 0).forGetter(CustomBossEvent$Packed::value),
            Codec.INT.optionalFieldOf("Max", 100).forGetter(CustomBossEvent$Packed::max),
            BossEvent$BossBarColor.CODEC.optionalFieldOf("Color", BossEvent$BossBarColor.WHITE).forGetter(CustomBossEvent$Packed::color),
            BossEvent$BossBarOverlay.CODEC.optionalFieldOf("Overlay", BossEvent$BossBarOverlay.PROGRESS).forGetter(CustomBossEvent$Packed::overlay),
            Codec.BOOL.optionalFieldOf("DarkenScreen", false).forGetter(CustomBossEvent$Packed::darkenScreen),
            Codec.BOOL.optionalFieldOf("PlayBossMusic", false).forGetter(CustomBossEvent$Packed::playBossMusic),
            Codec.BOOL.optionalFieldOf("CreateWorldFog", false).forGetter(CustomBossEvent$Packed::createWorldFog),
            UUIDUtil.CODEC_SET.optionalFieldOf("Players", Set.of()).forGetter(CustomBossEvent$Packed::players)
         )
         .apply(i, CustomBossEvent$Packed::new)
   );
}
