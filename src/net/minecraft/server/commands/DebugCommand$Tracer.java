package net.minecraft.server.commands;

import java.io.PrintWriter;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.execution.TraceCallbacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.commons.io.IOUtils;

class DebugCommand$Tracer implements CommandSource, TraceCallbacks {
   public static final int INDENT_OFFSET = 1;
   private final PrintWriter output;
   private int lastIndent;
   private boolean waitingForResult;

   private DebugCommand$Tracer(final PrintWriter output) {
      this.output = output;
   }

   private void indentAndSave(final int value) {
      this.printIndent(value);
      this.lastIndent = value;
   }

   private void printIndent(final int value) {
      for (int i = 0; i < value + 1; i++) {
         this.output.write("    ");
      }
   }

   private void newLine() {
      if (this.waitingForResult) {
         this.output.println();
         this.waitingForResult = false;
      }
   }

   @Override
   public void onCommand(final int depth, final String command) {
      this.newLine();
      this.indentAndSave(depth);
      this.output.print("[C] ");
      this.output.print(command);
      this.waitingForResult = true;
   }

   @Override
   public void onReturn(final int depth, final String command, final int result) {
      if (this.waitingForResult) {
         this.output.print(" -> ");
         this.output.println(result);
         this.waitingForResult = false;
      } else {
         this.indentAndSave(depth);
         this.output.print("[R = ");
         this.output.print(result);
         this.output.print("] ");
         this.output.println(command);
      }
   }

   @Override
   public void onCall(final int depth, final Identifier function, final int size) {
      this.newLine();
      this.indentAndSave(depth);
      this.output.print("[F] ");
      this.output.print(function);
      this.output.print(" size=");
      this.output.println(size);
   }

   @Override
   public void onError(final String message) {
      this.newLine();
      this.indentAndSave(this.lastIndent + 1);
      this.output.print("[E] ");
      this.output.print(message);
   }

   @Override
   public void sendSystemMessage(final Component message) {
      this.newLine();
      this.printIndent(this.lastIndent + 1);
      this.output.print("[M] ");
      this.output.println(message.getString());
   }

   @Override
   public boolean acceptsSuccess() {
      return true;
   }

   @Override
   public boolean acceptsFailure() {
      return true;
   }

   @Override
   public boolean shouldInformAdmins() {
      return false;
   }

   @Override
   public boolean alwaysAccepts() {
      return true;
   }

   @Override
   public void close() {
      IOUtils.closeQuietly(this.output);
   }
}
