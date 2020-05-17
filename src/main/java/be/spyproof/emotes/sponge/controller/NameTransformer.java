 package be.spyproof.emotes.sponge.controller;

 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.format.TextColors;
 
 
 
 
 
 public class NameTransformer
   implements INameTransformer
 {
   public Text transform(String param) {
     return Text.of(new Object[] { TextColors.WHITE, param });
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\controller\NameTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */