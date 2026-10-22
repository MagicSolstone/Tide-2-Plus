package net.minecraft.world.item.crafting.display;

import java.util.List;

public interface DisplayContentsFactory$ForRemainders<T> extends DisplayContentsFactory<T> {
   T addRemainder(T entry, List<T> remainders);
}
