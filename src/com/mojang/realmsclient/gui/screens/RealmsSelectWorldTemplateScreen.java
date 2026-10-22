package com.mojang.realmsclient.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer$WorldType;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.TextRenderingUtils;
import com.mojang.realmsclient.util.TextRenderingUtils$Line;
import com.mojang.realmsclient.util.TextRenderingUtils$LineSegment;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonLinks;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsSelectWorldTemplateScreen extends RealmsScreen {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
   private static final Component SELECT_BUTTON_NAME = Component.translatable("mco.template.button.select");
   private static final Component TRAILER_BUTTON_NAME = Component.translatable("mco.template.button.trailer");
   private static final Component PUBLISHER_BUTTON_NAME = Component.translatable("mco.template.button.publisher");
   private static final int BUTTON_WIDTH = 100;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private final Consumer<WorldTemplate> callback;
   private RealmsSelectWorldTemplateScreen$WorldTemplateList worldTemplateList;
   private final RealmsServer$WorldType worldType;
   private final List<Component> subtitle;
   private Button selectButton;
   private Button trailerButton;
   private Button publisherButton;
   @Nullable
   private WorldTemplate selectedTemplate = null;
   @Nullable
   private URI currentLink;
   @Nullable
   private List<TextRenderingUtils$Line> noTemplatesMessage;

   public RealmsSelectWorldTemplateScreen(
      final Component title,
      final Consumer<WorldTemplate> callback,
      final RealmsServer$WorldType worldType,
      @Nullable final WorldTemplatePaginatedList alreadyFetched
   ) {
      this(title, callback, worldType, alreadyFetched, List.of());
   }

   public RealmsSelectWorldTemplateScreen(
      final Component title,
      final Consumer<WorldTemplate> callback,
      final RealmsServer$WorldType worldType,
      @Nullable final WorldTemplatePaginatedList alreadyFetched,
      final List<Component> subtitle
   ) {
      super(title);
      this.callback = callback;
      this.worldType = worldType;
      if (alreadyFetched == null) {
         this.worldTemplateList = new RealmsSelectWorldTemplateScreen$WorldTemplateList(this);
         this.fetchTemplatesAsync(new WorldTemplatePaginatedList(10));
      } else {
         this.worldTemplateList = new RealmsSelectWorldTemplateScreen$WorldTemplateList(this, Lists.newArrayList(alreadyFetched.templates()));
         this.fetchTemplatesAsync(alreadyFetched);
      }

      this.subtitle = subtitle;
   }

   @Override
   public void init() {
      this.layout.setHeaderHeight(33 + this.subtitle.size() * (9 + 4));
      LinearLayout header = this.layout.addToHeader(LinearLayout.vertical().spacing(4));
      header.defaultCellSetting().alignHorizontallyCenter();
      header.addChild(new StringWidget(this.title, this.font));
      this.subtitle.forEach(warning -> header.addChild(new StringWidget(warning, this.font)));
      this.worldTemplateList = this.layout.addToContents(new RealmsSelectWorldTemplateScreen$WorldTemplateList(this, this.worldTemplateList.getTemplates()));
      LinearLayout bottomButtons = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
      bottomButtons.defaultCellSetting().alignHorizontallyCenter();
      this.trailerButton = bottomButtons.addChild(Button.builder(TRAILER_BUTTON_NAME, button -> this.onTrailer()).width(100).build());
      this.selectButton = bottomButtons.addChild(Button.builder(SELECT_BUTTON_NAME, button -> this.selectTemplate()).width(100).build());
      bottomButtons.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).width(100).build());
      this.publisherButton = bottomButtons.addChild(Button.builder(PUBLISHER_BUTTON_NAME, button -> this.onPublish()).width(100).build());
      this.updateButtonStates();
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
   }

   @Override
   protected void repositionElements() {
      this.worldTemplateList.updateSize(this.width, this.layout);
      this.layout.arrangeElements();
   }

   @Override
   public Component getNarrationMessage() {
      List<Component> parts = Lists.newArrayListWithCapacity(2);
      parts.add(this.title);
      parts.addAll(this.subtitle);
      return CommonComponents.joinLines(parts);
   }

   private void updateButtonStates() {
      this.publisherButton.visible = this.selectedTemplate != null && this.selectedTemplate.link() != null;
      this.trailerButton.visible = this.selectedTemplate != null && this.selectedTemplate.trailer() != null;
      this.selectButton.active = this.selectedTemplate != null;
   }

   @Override
   public void onClose() {
      this.callback.accept(null);
   }

   private void selectTemplate() {
      if (this.selectedTemplate != null) {
         this.callback.accept(this.selectedTemplate);
      }
   }

   private void onTrailer() {
      if (this.selectedTemplate != null && this.selectedTemplate.trailer() != null) {
         ConfirmLinkScreen.confirmLinkNow(this, this.selectedTemplate.trailer());
      }
   }

   private void onPublish() {
      if (this.selectedTemplate != null && this.selectedTemplate.link() != null) {
         ConfirmLinkScreen.confirmLinkNow(this, this.selectedTemplate.link());
      }
   }

   private void fetchTemplatesAsync(final WorldTemplatePaginatedList startPage) {
      Util.nonCriticalIoPool()
         .execute(
            () -> {
               WorldTemplatePaginatedList page = startPage;
               RealmsClient client = RealmsClient.getOrCreate();

               while (page != null) {
                  Either<WorldTemplatePaginatedList, Exception> result = this.fetchTemplates(page, client);
                  page = this.minecraft
                     .<WorldTemplatePaginatedList>submit(
                        () -> {
                           if (result.right().isPresent()) {
                              LOGGER.error("Couldn't fetch templates", (Throwable)result.right().get());
                              if (this.worldTemplateList.isEmpty()) {
                                 this.noTemplatesMessage = TextRenderingUtils.decompose(I18n.get("mco.template.select.failure"));
                              }

                              return null;
                           } else {
                              WorldTemplatePaginatedList currentPage = (WorldTemplatePaginatedList)result.left().get();

                              for (WorldTemplate template : currentPage.templates()) {
                                 this.worldTemplateList.addEntry(template);
                              }

                              if (currentPage.templates().isEmpty()) {
                                 if (this.worldTemplateList.isEmpty()) {
                                    String withoutLink = I18n.get("mco.template.select.none", "%link");
                                    TextRenderingUtils$LineSegment link = TextRenderingUtils$LineSegment.link(
                                       I18n.get("mco.template.select.none.linkTitle"), CommonLinks.REALMS_CONTENT_CREATION
                                    );
                                    this.noTemplatesMessage = TextRenderingUtils.decompose(withoutLink, link);
                                 }

                                 return null;
                              } else {
                                 return currentPage;
                              }
                           }
                        }
                     )
                     .join();
               }
            }
         );
   }

   private Either<WorldTemplatePaginatedList, Exception> fetchTemplates(final WorldTemplatePaginatedList paginatedList, final RealmsClient client) {
      try {
         return Either.left(client.fetchWorldTemplates(paginatedList.page() + 1, paginatedList.size(), this.worldType));
      } catch (RealmsServiceException var4) {
         return Either.right(var4);
      }
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int xm, final int ym, final float a) {
      super.extractRenderState(graphics, xm, ym, a);
      this.currentLink = null;
      if (this.noTemplatesMessage != null) {
         this.extractMultilineMessage(graphics, xm, ym, this.noTemplatesMessage);
      }
   }

   private void extractMultilineMessage(final GuiGraphicsExtractor graphics, final int xm, final int ym, final List<TextRenderingUtils$Line> noTemplatesMessage) {
      for (int i = 0; i < noTemplatesMessage.size(); i++) {
         TextRenderingUtils$Line line = noTemplatesMessage.get(i);
         int lineY = row(4 + i);
         int lineWidth = line.segments.stream().mapToInt(s -> this.font.width(s.renderedText())).sum();
         int startX = this.width / 2 - lineWidth / 2;

         for (TextRenderingUtils$LineSegment segment : line.segments) {
            int color = segment.isLink() ? -13408581 : -1;
            String text = segment.renderedText();
            graphics.text(this.font, text, startX, lineY, color);
            int endX = startX + this.font.width(text);
            if (segment.isLink() && xm > startX && xm < endX && ym > lineY - 3 && ym < lineY + 8) {
               URI linkUrl = segment.getLinkUrl();
               graphics.setTooltipForNextFrame(Component.literal(linkUrl.toString()), xm, ym);
               this.currentLink = linkUrl;
            }

            startX = endX;
         }
      }
   }
}
