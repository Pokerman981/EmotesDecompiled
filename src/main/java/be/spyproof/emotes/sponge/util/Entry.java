 package be.spyproof.emotes.sponge.util;
 
 import org.spongepowered.api.text.Text;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 class Entry
 {
   final String str;
   final Text placeholderValue;
   
   public Entry(String str) {
     this.str = str;
     this.placeholderValue = null;
   }
   
   public Entry(Text placeholderValue) {
     this.str = null;
     this.placeholderValue = placeholderValue;
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\spong\\util\Entry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */