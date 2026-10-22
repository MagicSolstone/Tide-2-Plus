package net.minecraft.world.scores;

import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class ScoreboardSaveData extends SavedData {
   public static final SavedDataType<ScoreboardSaveData> TYPE = new SavedDataType<>(
      Identifier.withDefaultNamespace("scoreboard"),
      ScoreboardSaveData::new,
      ScoreboardSaveData$Packed.CODEC.xmap(ScoreboardSaveData::new, ScoreboardSaveData::getData),
      DataFixTypes.SAVED_DATA_SCOREBOARD
   );
   private ScoreboardSaveData$Packed data;

   private ScoreboardSaveData() {
      this(ScoreboardSaveData$Packed.EMPTY);
   }

   public ScoreboardSaveData(final ScoreboardSaveData$Packed data) {
      this.data = data;
   }

   public ScoreboardSaveData$Packed getData() {
      return this.data;
   }

   public void setData(final ScoreboardSaveData$Packed data) {
      if (!data.equals(this.data)) {
         this.data = data;
         this.setDirty();
      }
   }
}
