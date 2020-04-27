/*    */ package be.spyproof.emotes.sponge.util;
/*    */ 
/*    */ import org.spongepowered.api.text.Text;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Entry
/*    */ {
/*    */   final String str;
/*    */   final Text placeholderValue;
/*    */   
/*    */   public Entry(String str) {
/* 70 */     this.str = str;
/* 71 */     this.placeholderValue = null;
/*    */   }
/*    */   
/*    */   public Entry(Text placeholderValue) {
/* 75 */     this.str = null;
/* 76 */     this.placeholderValue = placeholderValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\spong\\util\Entry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */