package net.minecraft.util.parsing.packrat;

public interface Control {
   Control UNBOUND = new Control$1();

   void cut();

   boolean hasCut();
}
