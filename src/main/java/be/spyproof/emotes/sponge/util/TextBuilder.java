/*    */ package be.spyproof.emotes.sponge.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.spongepowered.api.text.Text;
/*    */ import org.spongepowered.api.text.serializer.TextSerializers;
/*    */ 
/*    */ 
/*    */ public class TextBuilder
/*    */ {
/*    */   private final String input;
/* 12 */   private List<Entry> entries = new ArrayList<>();
/*    */   
/*    */   public TextBuilder(String input) {
/* 15 */     this.input = input;
/* 16 */     this.entries.add(new Entry(input));
/*    */   }
/*    */   
/*    */   public TextBuilder addPlaceholder(String placeholderKey, Text value) {
/* 20 */     List<Entry> newEntries = new ArrayList<>();
/*    */     
/* 22 */     for (Entry entry : this.entries) {
/* 23 */       if (entry.placeholderValue != null) {
/* 24 */         newEntries.add(entry); continue;
/*    */       } 
/* 26 */       String entryString = entry.str;
/*    */       Integer index;
/* 28 */       while ((index = Integer.valueOf(entryString.indexOf(placeholderKey))).intValue() > 0) {
/* 29 */         String prefix = entryString.substring(0, index.intValue());
/* 30 */         if (prefix.length() > 0) {
/* 31 */           newEntries.add(new Entry(prefix));
/*    */         }
/* 33 */         newEntries.add(new Entry((value == null) ? (Text)Text.of("null") : value));
/* 34 */         entryString = entryString.substring(index.intValue() + placeholderKey.length());
/*    */       } 
/* 36 */       if (entryString.length() > 0) {
/* 37 */         newEntries.add(new Entry(entryString));
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 42 */     this.entries = newEntries;
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public Text build() {
/* 47 */     Text lastText = null;
/*    */     
/* 49 */     for (int i = this.entries.size() - 1; i >= 0; i--) {
/* 50 */       Entry entry = this.entries.get(i);
/* 51 */       Text.Builder entryBuilder = (entry.placeholderValue == null) ? TextSerializers.FORMATTING_CODE.deserialize(entry.str).toBuilder() : entry.placeholderValue.toBuilder();
/*    */       
/* 53 */       if (lastText != null) {
/* 54 */         entryBuilder.append(new Text[] { lastText });
/*    */       }
/*    */       
/* 57 */       lastText = entryBuilder.build();
/*    */     } 
/*    */     
/* 60 */     return (lastText == null) ? Text.EMPTY : lastText;
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\spong\\util\TextBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */