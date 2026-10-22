package net.minecraft.nbt;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public interface TextComponentTagVisitor$Styling {
   Style keyStyle();

   Style stringStyle();

   Style numberStyle();

   Component token(final TextComponentTagVisitor$Token token);
}
