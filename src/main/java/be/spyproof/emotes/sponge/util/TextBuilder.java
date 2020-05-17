 package be.spyproof.emotes.sponge.util;

 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.serializer.TextSerializers;

 import java.util.ArrayList;
 import java.util.List;
 
 
 public class TextBuilder
 {
   private final String input;
   private List<Entry> entries = new ArrayList<>();
   
   public TextBuilder(String input) {
     this.input = input;
     this.entries.add(new Entry(input));
   }
   
   public TextBuilder addPlaceholder(String placeholderKey, Text value) {
     List<Entry> newEntries = new ArrayList<>();
     
     for (Entry entry : this.entries) {
       if (entry.placeholderValue != null) {
         newEntries.add(entry); continue;
       } 
       String entryString = entry.str;
       Integer index;
       while ((index = Integer.valueOf(entryString.indexOf(placeholderKey))).intValue() > 0) {
         String prefix = entryString.substring(0, index.intValue());
         if (prefix.length() > 0) {
           newEntries.add(new Entry(prefix));
         }
         newEntries.add(new Entry((value == null) ? (Text)Text.of("null") : value));
         entryString = entryString.substring(index.intValue() + placeholderKey.length());
       } 
       if (entryString.length() > 0) {
         newEntries.add(new Entry(entryString));
       }
     } 
 
     
     this.entries = newEntries;
     return this;
   }
   
   public Text build() {
     Text lastText = null;
     
     for (int i = this.entries.size() - 1; i >= 0; i--) {
       Entry entry = this.entries.get(i);
       Text.Builder entryBuilder = (entry.placeholderValue == null) ? TextSerializers.FORMATTING_CODE.deserialize(entry.str).toBuilder() : entry.placeholderValue.toBuilder();
       
       if (lastText != null) {
         entryBuilder.append(new Text[] { lastText });
       }
       
       lastText = entryBuilder.build();
     } 
     
     return (lastText == null) ? Text.EMPTY : lastText;
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\spong\\util\TextBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */