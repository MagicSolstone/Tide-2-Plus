package net.minecraft.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

record ProblemReporter$Collector$ProblemTreeNode(
   ProblemReporter$PathElement element,
   List<ProblemReporter$Problem> problems,
   Map<ProblemReporter$PathElement, ProblemReporter$Collector$ProblemTreeNode> children
) {
   public ProblemReporter$Collector$ProblemTreeNode(final ProblemReporter$PathElement pathElement) {
      this(pathElement, new ArrayList<>(), new LinkedHashMap<>());
   }

   public ProblemReporter$Collector$ProblemTreeNode child(final ProblemReporter$PathElement id) {
      return this.children.computeIfAbsent(id, ProblemReporter$Collector$ProblemTreeNode::new);
   }

   public List<String> getLines() {
      int problemCount = this.problems.size();
      int childrenCount = this.children.size();
      if (problemCount == 0 && childrenCount == 0) {
         return List.of();
      } else if (problemCount == 0 && childrenCount == 1) {
         List<String> lines = new ArrayList<>();
         this.children.forEach((element, child) -> lines.addAll(child.getLines()));
         lines.set(0, this.element.get() + lines.get(0));
         return lines;
      } else if (problemCount == 1 && childrenCount == 0) {
         return List.of(this.element.get() + ": " + this.problems.getFirst().description());
      } else {
         List<String> lines = new ArrayList<>();
         this.children.forEach((element, child) -> lines.addAll(child.getLines()));
         lines.replaceAll(s -> "  " + s);

         for (ProblemReporter$Problem problem : this.problems) {
            lines.add("  " + problem.description());
         }

         lines.addFirst(this.element.get() + ":");
         return lines;
      }
   }
}
