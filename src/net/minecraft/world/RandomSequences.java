package net.minecraft.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class RandomSequences extends SavedData {
   public static final Codec<RandomSequences> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.INT.fieldOf("salt").forGetter(RandomSequences::salt),
            Codec.BOOL.optionalFieldOf("include_world_seed", true).forGetter(RandomSequences::includeWorldSeed),
            Codec.BOOL.optionalFieldOf("include_sequence_id", true).forGetter(RandomSequences::includeSequenceId),
            Codec.unboundedMap(Identifier.CODEC, RandomSequence.CODEC).fieldOf("sequences").forGetter(rs -> rs.sequences)
         )
         .apply(i, RandomSequences::new)
   );
   public static final SavedDataType<RandomSequences> TYPE = new SavedDataType<>(
      Identifier.withDefaultNamespace("random_sequences"), RandomSequences::new, CODEC, DataFixTypes.SAVED_DATA_RANDOM_SEQUENCES
   );
   private int salt;
   private boolean includeWorldSeed = true;
   private boolean includeSequenceId = true;
   private final Map<Identifier, RandomSequence> sequences = new Object2ObjectOpenHashMap();

   public RandomSequences() {
   }

   private RandomSequences(final int salt, final boolean includeWorldSeed, final boolean includeSequenceId, final Map<Identifier, RandomSequence> sequences) {
      this.salt = salt;
      this.includeWorldSeed = includeWorldSeed;
      this.includeSequenceId = includeSequenceId;
      this.sequences.putAll(sequences);
   }

   public RandomSource get(final Identifier key, final long worldSeed) {
      RandomSource random = this.sequences.computeIfAbsent(key, rl -> this.createSequence(rl, worldSeed)).random();
      return new RandomSequences$DirtyMarkingRandomSource(this, random);
   }

   private RandomSequence createSequence(final Identifier key, final long worldSeed) {
      return this.createSequence(key, worldSeed, this.salt, this.includeWorldSeed, this.includeSequenceId);
   }

   private RandomSequence createSequence(
      final Identifier key, final long worldSeed, final int salt, final boolean includeWorldSeed, final boolean includeSequenceId
   ) {
      long seed = (includeWorldSeed ? worldSeed : 0L) ^ salt;
      return new RandomSequence(seed, includeSequenceId ? Optional.of(key) : Optional.empty());
   }

   public void forAllSequences(final BiConsumer<Identifier, RandomSequence> consumer) {
      this.sequences.forEach(consumer);
   }

   public void setSeedDefaults(final int salt, final boolean includeWorldSeed, final boolean includeSequenceId) {
      this.salt = salt;
      this.includeWorldSeed = includeWorldSeed;
      this.includeSequenceId = includeSequenceId;
   }

   public int clear() {
      int count = this.sequences.size();
      this.sequences.clear();
      return count;
   }

   public void reset(final Identifier id, final long worldSeed) {
      this.sequences.put(id, this.createSequence(id, worldSeed));
   }

   public void reset(final Identifier id, final long worldSeed, final int salt, final boolean includeWorldSeed, final boolean includeSequenceId) {
      this.sequences.put(id, this.createSequence(id, worldSeed, salt, includeWorldSeed, includeSequenceId));
   }

   private int salt() {
      return this.salt;
   }

   private boolean includeWorldSeed() {
      return this.includeWorldSeed;
   }

   private boolean includeSequenceId() {
      return this.includeSequenceId;
   }
}
