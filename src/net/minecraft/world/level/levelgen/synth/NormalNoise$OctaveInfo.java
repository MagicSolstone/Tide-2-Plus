package net.minecraft.world.level.levelgen.synth;

record NormalNoise$OctaveInfo(int octaveIndex, double frequency, double amplitude) {
   public String seed() {
      return "octave_" + this.octaveIndex;
   }

   public double absAmplitude() {
      return Math.abs(this.amplitude);
   }
}
