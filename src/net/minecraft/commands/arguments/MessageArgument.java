package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.commands.CommandSigningContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import org.jspecify.annotations.Nullable;

public class MessageArgument implements SignedArgument<MessageArgument$Message> {
   private static final Collection<String> EXAMPLES = Arrays.asList("Hello world!", "foo", "@e", "Hello @p :)");
   private static final Dynamic2CommandExceptionType TOO_LONG = new Dynamic2CommandExceptionType(
      (length, maxLength) -> Component.translatableEscape("argument.message.too_long", length, maxLength)
   );

   public static MessageArgument message() {
      return new MessageArgument();
   }

   public static Component getMessage(final CommandContext<CommandSourceStack> context, final String name) throws CommandSyntaxException {
      MessageArgument$Message message = (MessageArgument$Message)context.getArgument(name, MessageArgument$Message.class);
      return message.resolveComponent((CommandSourceStack)context.getSource());
   }

   public static void resolveChatMessage(final CommandContext<CommandSourceStack> context, final String name, final Consumer<PlayerChatMessage> task) throws CommandSyntaxException {
      MessageArgument$Message message = (MessageArgument$Message)context.getArgument(name, MessageArgument$Message.class);
      CommandSourceStack sender = (CommandSourceStack)context.getSource();
      Component formatted = message.resolveComponent(sender);
      CommandSigningContext signingContext = sender.getSigningContext();
      PlayerChatMessage signedArgument = signingContext.getArgument(name);
      if (signedArgument != null) {
         resolveSignedMessage(task, sender, signedArgument.withUnsignedContent(formatted));
      } else {
         resolveDisguisedMessage(task, sender, PlayerChatMessage.system(message.text).withUnsignedContent(formatted));
      }
   }

   private static void resolveSignedMessage(final Consumer<PlayerChatMessage> task, final CommandSourceStack sender, final PlayerChatMessage signedArgument) {
      MinecraftServer server = sender.getServer();
      CompletableFuture<FilteredText> filteredFuture = filterPlainText(sender, signedArgument);
      Component decorated = server.getChatDecorator().decorate(sender.getPlayer(), signedArgument.decoratedContent());
      sender.getChatMessageChainer().append(filteredFuture, filtered -> {
         PlayerChatMessage filteredMessage = signedArgument.withUnsignedContent(decorated).filter(filtered.mask());
         task.accept(filteredMessage);
      });
   }

   private static void resolveDisguisedMessage(final Consumer<PlayerChatMessage> task, final CommandSourceStack sender, final PlayerChatMessage argument) {
      ChatDecorator decorator = sender.getServer().getChatDecorator();
      Component decorated = decorator.decorate(sender.getPlayer(), argument.decoratedContent());
      task.accept(argument.withUnsignedContent(decorated));
   }

   private static CompletableFuture<FilteredText> filterPlainText(final CommandSourceStack sender, final PlayerChatMessage message) {
      ServerPlayer player = sender.getPlayer();
      return player != null && message.hasSignatureFrom(player.getUUID())
         ? player.getTextFilter().processStreamMessage(message.signedContent())
         : CompletableFuture.completedFuture(FilteredText.passThrough(message.signedContent()));
   }

   public MessageArgument$Message parse(final StringReader reader) throws CommandSyntaxException {
      return MessageArgument$Message.parseText(reader, true);
   }

   public <S> MessageArgument$Message parse(final StringReader reader, @Nullable final S source) throws CommandSyntaxException {
      return MessageArgument$Message.parseText(reader, EntitySelectorParser.allowSelectors(source));
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
