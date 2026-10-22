package net.minecraft.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

public class ProblemReporter$Collector implements ProblemReporter {
   public static final ProblemReporter$PathElement EMPTY_ROOT = () -> "";
   @Nullable
   private final ProblemReporter$Collector parent;
   private final ProblemReporter$PathElement element;
   private final Set<ProblemReporter$Collector$Entry> problems;

   public ProblemReporter$Collector() {
      this(EMPTY_ROOT);
   }

   public ProblemReporter$Collector(final ProblemReporter$PathElement root) {
      this.parent = null;
      this.problems = new LinkedHashSet<>();
      this.element = root;
   }

   private ProblemReporter$Collector(final ProblemReporter$Collector parent, final ProblemReporter$PathElement path) {
      this.problems = parent.problems;
      this.parent = parent;
      this.element = path;
   }

   @Override
   public ProblemReporter forChild(final ProblemReporter$PathElement path) {
      return new ProblemReporter$Collector(this, path);
   }

   @Override
   public void report(final ProblemReporter$Problem problem) {
      this.problems.add(new ProblemReporter$Collector$Entry(this, problem));
   }

   public boolean isEmpty() {
      return this.problems.isEmpty();
   }

   public boolean hasFatalProblems() {
      for (ProblemReporter$Collector$Entry entry : this.problems) {
         if (entry.problem.isFatal()) {
            return true;
         }
      }

      return false;
   }

   public void forEach(final BiConsumer<String, ProblemReporter$Problem> output) {
      List<ProblemReporter$PathElement> pathElements = new ArrayList<>();
      StringBuilder pathString = new StringBuilder();

      for (ProblemReporter$Collector$Entry entry : this.problems) {
         for (ProblemReporter$Collector current = entry.source; current != null; current = current.parent) {
            pathElements.add(current.element);
         }

         for (int i = pathElements.size() - 1; i >= 0; i--) {
            pathString.append(pathElements.get(i).get());
         }

         output.accept(pathString.toString(), entry.problem());
         pathString.setLength(0);
         pathElements.clear();
      }
   }

   public String getReport() {
      Multimap<String, ProblemReporter$Problem> groupedProblems = HashMultimap.create();
      this.forEach(groupedProblems::put);
      return groupedProblems.asMap()
         .entrySet()
         .stream()
         .map(
            entry -> " at "
               + (String)entry.getKey()
               + ": "
               + ((Collection)entry.getValue()).stream().map(ProblemReporter$Problem::description).collect(Collectors.joining("; "))
         )
         .collect(Collectors.joining("\n"));
   }

   public String getTreeReport() {
      List<ProblemReporter$PathElement> pathElements = new ArrayList<>();
      ProblemReporter$Collector$ProblemTreeNode root = new ProblemReporter$Collector$ProblemTreeNode(this.element);

      for (ProblemReporter$Collector$Entry entry : this.problems) {
         for (ProblemReporter$Collector current = entry.source; current != this; current = current.parent) {
            pathElements.add(current.element);
         }

         ProblemReporter$Collector$ProblemTreeNode node = root;

         for (int i = pathElements.size() - 1; i >= 0; i--) {
            node = node.child(pathElements.get(i));
         }

         pathElements.clear();
         node.problems.add(entry.problem);
      }

      return String.join("\n", root.getLines());
   }
}
