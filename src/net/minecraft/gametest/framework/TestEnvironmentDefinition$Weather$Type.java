package net.minecraft.gametest.framework;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;

public enum TestEnvironmentDefinition$Weather$Type implements StringRepresentable {
   CLEAR("clear", 100000, 0, false, false),
   RAIN("rain", 0, 100000, true, false),
   THUNDER("thunder", 0, 100000, true, true);

   public static final Codec<TestEnvironmentDefinition$Weather$Type> CODEC = StringRepresentable.fromEnum(TestEnvironmentDefinition$Weather$Type::values);
   private final String id;
   private final int clearTime;
   private final int rainTime;
   private final boolean raining;
   private final boolean thundering;

   private TestEnvironmentDefinition$Weather$Type(final String id, final int clearTime, final int rainTime, final boolean raining, final boolean thundering) {
      this.id = id;
      this.clearTime = clearTime;
      this.rainTime = rainTime;
      this.raining = raining;
      this.thundering = thundering;
   }

   public void apply(final ServerLevel level) {
      level.getServer().setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering);
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }
}
