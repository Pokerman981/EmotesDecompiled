 package be.spyproof.emotes.sponge.util;

 import be.spyproof.emotes.model.Emote;
 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.serializer.TextSerializers;
 
 
 
 
 
 public class ConversionUtils
 {
   public static Text convert(String s) {
     return TextSerializers.FORMATTING_CODE.deserialize(s);
   }
 
   
   public static Text[] convert(String[] s) {
     Text[] texts = new Text[s.length];
     
     for (int i = 0; i < texts.length; i++) {
       texts[i] = convert(s[i]);
     }
     return texts;
   }
 
   
   public static String convert(Text s) {
     return TextSerializers.FORMATTING_CODE.serialize(s);
   }
 
   
   public static String[] convert(Text[] texts) {
     String[] strings = new String[texts.length];
     
     for (int i = 0; i < strings.length; i++) {
       strings[i] = convert(texts[i]);
     }
     return strings;
   }
 
   
   public static Emote createEmote(String emoteName, Text selfLine, Text... otherLine) {
     return createEmote(emoteName, new Text[] { selfLine }, otherLine);
   }
 
   
   public static Emote createEmote(String emoteName, Text[] selfLine, Text... otherLine) {
     return new Emote(emoteName, 
         
         convert(selfLine), 
         convert(otherLine));
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\spong\\util\ConversionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */