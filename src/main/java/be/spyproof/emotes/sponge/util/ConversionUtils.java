/*    */ package be.spyproof.emotes.sponge.util;
/*    */ 
/*    */ import be.spyproof.emotes.model.Emote;
/*    */ import org.spongepowered.api.text.Text;
/*    */ import org.spongepowered.api.text.serializer.TextSerializers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConversionUtils
/*    */ {
/*    */   public static Text convert(String s) {
/* 14 */     return TextSerializers.FORMATTING_CODE.deserialize(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Text[] convert(String[] s) {
/* 19 */     Text[] texts = new Text[s.length];
/*    */     
/* 21 */     for (int i = 0; i < texts.length; i++) {
/* 22 */       texts[i] = convert(s[i]);
/*    */     }
/* 24 */     return texts;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String convert(Text s) {
/* 29 */     return TextSerializers.FORMATTING_CODE.serialize(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String[] convert(Text[] texts) {
/* 34 */     String[] strings = new String[texts.length];
/*    */     
/* 36 */     for (int i = 0; i < strings.length; i++) {
/* 37 */       strings[i] = convert(texts[i]);
/*    */     }
/* 39 */     return strings;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Emote createEmote(String emoteName, Text selfLine, Text... otherLine) {
/* 44 */     return createEmote(emoteName, new Text[] { selfLine }, otherLine);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Emote createEmote(String emoteName, Text[] selfLine, Text... otherLine) {
/* 49 */     return new Emote(emoteName, 
/*    */         
/* 51 */         convert(selfLine), 
/* 52 */         convert(otherLine));
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\spong\\util\ConversionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */