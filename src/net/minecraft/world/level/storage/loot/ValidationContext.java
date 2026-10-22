package net.minecraft.world.level.storage.loot;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.HolderGetter$Provider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.ProblemReporter$FieldPathElement;
import net.minecraft.util.ProblemReporter$IndexedFieldPathElement;
import net.minecraft.util.ProblemReporter$MapEntryPathElement;
import net.minecraft.util.ProblemReporter$PathElement;
import net.minecraft.util.ProblemReporter$Problem;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;

public class ValidationContext {
   private final ProblemReporter reporter;
   private final ContextKeySet contextKeySet;
   private final Optional<HolderGetter$Provider> resolver;
   private final Set<Object> visitedObjects;

   public ValidationContext(final ProblemReporter reporter, final ContextKeySet contextKeySet, final HolderGetter$Provider resolver) {
      this(reporter, contextKeySet, Optional.of(resolver), Set.of());
   }

   public ValidationContext(final ProblemReporter reporter, final ContextKeySet contextKeySet) {
      this(reporter, contextKeySet, Optional.empty(), Set.of());
   }

   private ValidationContext(
      final ProblemReporter reporter, final ContextKeySet contextKeySet, final Optional<HolderGetter$Provider> resolver, final Set<Object> visitedObjects
   ) {
      this.reporter = reporter;
      this.contextKeySet = contextKeySet;
      this.resolver = resolver;
      this.visitedObjects = visitedObjects;
   }

   public ValidationContext forChild(final ProblemReporter$PathElement subContext) {
      return new ValidationContext(this.reporter.forChild(subContext), this.contextKeySet, this.resolver, this.visitedObjects);
   }

   public ValidationContext forField(final String name) {
      return this.forChild(new ProblemReporter$FieldPathElement(name));
   }

   public ValidationContext forIndexedField(final String name, final int index) {
      return this.forChild(new ProblemReporter$IndexedFieldPathElement(name, index));
   }

   public ValidationContext forMapField(final String name, final String key) {
      return this.forChild(new ProblemReporter$MapEntryPathElement(name, key));
   }

   private ValidationContext enterObject(final ProblemReporter$PathElement subContext, final Object object) {
      Set<Object> newVisitedElements = ImmutableSet.builder().addAll(this.visitedObjects).add(object).build();
      return new ValidationContext(this.reporter.forChild(subContext), this.contextKeySet, this.resolver, newVisitedElements);
   }

   public ValidationContext enterElement(final ProblemReporter$PathElement subContext, final ResourceKey<?> element) {
      return this.enterObject(subContext, element);
   }

   public boolean hasVisitedElement(final ResourceKey<?> element) {
      return this.visitedObjects.contains(element);
   }

   public ValidationContext enterTag(final ProblemReporter$PathElement subContext, final TagKey<?> tag) {
      return this.enterObject(subContext, tag);
   }

   public boolean hasVisitedTag(final TagKey<?> tag) {
      return this.visitedObjects.contains(tag);
   }

   public void reportProblem(final ProblemReporter$Problem description) {
      this.reporter.report(description);
   }

   public void validateContextUsage(final LootContextUser lootContextUser) {
      Set<ContextKey<?>> allReferenced = lootContextUser.getReferencedContextParams();
      Set<ContextKey<?>> notProvided = Sets.difference(allReferenced, this.contextKeySet.allowed());
      if (!notProvided.isEmpty()) {
         this.reporter.report(new ValidationContext$ParametersNotProvidedProblem(notProvided));
      }
   }

   public HolderGetter$Provider resolver() {
      return this.resolver.orElseThrow(() -> new UnsupportedOperationException("References not allowed"));
   }

   public ProblemReporter reporter() {
      return this.reporter;
   }
}
