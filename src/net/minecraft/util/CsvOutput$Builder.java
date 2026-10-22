package net.minecraft.util;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CsvOutput$Builder {
   private final List<String> headers = Lists.newArrayList();

   public CsvOutput$Builder addColumn(final String header) {
      this.headers.add(header);
      return this;
   }

   public CsvOutput build(final Writer writer) throws IOException {
      return new CsvOutput(writer, this.headers);
   }
}
