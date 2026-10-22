package net.minecraft.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record AdultAndBabyModelPair<T extends Model<?>>(T adultModel, T babyModel) {
   public T getModel(final boolean isBaby) {
      return isBaby ? this.babyModel : this.adultModel;
   }
}
