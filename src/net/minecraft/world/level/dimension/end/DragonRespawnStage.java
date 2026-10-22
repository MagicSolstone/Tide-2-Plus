package net.minecraft.world.level.dimension.end;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public enum DragonRespawnStage implements StringRepresentable {
   START("start"),
   PREPARING_TO_SUMMON_PILLARS("preparing_to_summon_pillars"),
   SUMMONING_PILLARS("summoning_pillars"),
   SUMMONING_DRAGON("summoning_dragon"),
   END("end");

   public static final Codec<DragonRespawnStage> CODEC = StringRepresentable.fromEnum(DragonRespawnStage::values);
   private final String name;

   private DragonRespawnStage(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public abstract void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time);
}
