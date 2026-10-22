package net.minecraft.commands.arguments.blocks;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockPredicateArgument implements ArgumentType<BlockPredicateArgument$Result> {
   private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]", "#stone", "#stone[foo=bar]{baz=nbt}");
   private final HolderLookup<Block> blocks;

   public BlockPredicateArgument(final CommandBuildContext context) {
      this.blocks = context.lookupOrThrow(Registries.BLOCK);
   }

   public static BlockPredicateArgument blockPredicate(final CommandBuildContext context) {
      return new BlockPredicateArgument(context);
   }

   public BlockPredicateArgument$Result parse(final StringReader reader) throws CommandSyntaxException {
      return parse(this.blocks, reader);
   }

   public static BlockPredicateArgument$Result parse(final HolderLookup<Block> blocks, final StringReader reader) throws CommandSyntaxException {
      return (BlockPredicateArgument$Result)BlockStateParser.parseForTesting(blocks, reader, true)
         .map(
            block -> new BlockPredicateArgument$BlockPredicate(block.blockState(), block.properties().keySet(), block.nbt()),
            tag -> new BlockPredicateArgument$TagPredicate(tag.tag(), tag.vagueProperties(), tag.nbt())
         );
   }

   public static Predicate<BlockInWorld> getBlockPredicate(final CommandContext<CommandSourceStack> context, final String name) throws CommandSyntaxException {
      return (Predicate<BlockInWorld>)context.getArgument(name, BlockPredicateArgument$Result.class);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
      return BlockStateParser.fillSuggestions(this.blocks, builder, true, true);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
