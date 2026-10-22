package net.minecraft.server.packs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class FilePackResources extends AbstractPackMetadataResources implements PackResources {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final FilePackResources$SharedZipFileAccess zipFileAccess;
   private final String prefix;

   private FilePackResources(final PackLocationInfo location, final FilePackResources$SharedZipFileAccess zipFileAccess, final String prefix) {
      super(location);
      this.zipFileAccess = zipFileAccess;
      this.prefix = prefix;
   }

   private static String getPathFromLocation(final PackType type, final Identifier location) {
      return String.format(Locale.ROOT, "%s/%s/%s", type.getDirectory(), location.getNamespace(), location.getPath());
   }

   @Nullable
   @Override
   public IoSupplier<InputStream> getRootResource(final String... path) {
      return this.getResource(String.join("/", path));
   }

   @Override
   public IoSupplier<InputStream> getResource(final PackType type, final Identifier location) {
      return this.getResource(getPathFromLocation(type, location));
   }

   private String addPrefix(final String path) {
      return this.prefix.isEmpty() ? path : this.prefix + "/" + path;
   }

   @Nullable
   private IoSupplier<InputStream> getResource(final String path) {
      ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
      if (zipFile == null) {
         return null;
      } else {
         ZipEntry entry = zipFile.getEntry(this.addPrefix(path));
         return entry == null ? null : IoSupplier.create(zipFile, entry);
      }
   }

   @Override
   public Set<String> getNamespaces(final PackType type) {
      ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
      if (zipFile == null) {
         return Set.of();
      } else {
         Enumeration<? extends ZipEntry> entries = zipFile.entries();
         Set<String> namespaces = Sets.newHashSet();
         String typePrefix = this.addPrefix(type.getDirectory() + "/");

         while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            String name = zipEntry.getName();
            String namespace = extractNamespace(typePrefix, name);
            if (!namespace.isEmpty()) {
               if (Identifier.isValidNamespace(namespace)) {
                  namespaces.add(namespace);
               } else {
                  LOGGER.warn("Non {} character in namespace {} in pack {}, ignoring", new Object[]{"[a-z0-9_.-]", namespace, this.zipFileAccess.file});
               }
            }
         }

         return namespaces;
      }
   }

   @VisibleForTesting
   public static String extractNamespace(final String prefix, final String name) {
      if (!name.startsWith(prefix)) {
         return "";
      } else {
         int prefixLength = prefix.length();
         int firstPart = name.indexOf(47, prefixLength);
         return firstPart == -1 ? name.substring(prefixLength) : name.substring(prefixLength, firstPart);
      }
   }

   @Override
   public void close() {
      this.zipFileAccess.close();
   }

   @Override
   public void listResources(final PackType type, final String namespace, final String directory, final PackResources$ResourceOutput output) {
      ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
      if (zipFile != null) {
         Enumeration<? extends ZipEntry> entries = zipFile.entries();
         String root = this.addPrefix(type.getDirectory() + "/" + namespace + "/");
         String prefix = root + directory + "/";

         while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (!zipEntry.isDirectory()) {
               String name = zipEntry.getName();
               if (name.startsWith(prefix)) {
                  String path = name.substring(root.length());
                  Identifier id = Identifier.tryBuild(namespace, path);
                  if (id != null) {
                     output.accept(id, IoSupplier.create(zipFile, zipEntry));
                  } else {
                     LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", namespace, path);
                  }
               }
            }
         }
      }
   }
}
