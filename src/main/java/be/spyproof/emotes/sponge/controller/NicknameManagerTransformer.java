 package be.spyproof.emotes.sponge.controller;

 import be.spyproof.nickmanager.controller.ISpongeNicknameController;
 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.format.TextColors;
 import org.spongepowered.api.text.serializer.TextSerializers;
 
 
 
 public class NicknameManagerTransformer
   implements INameTransformer
 {
   private ISpongeNicknameController nicknameController;
   
   public NicknameManagerTransformer(ISpongeNicknameController nicknameController) {
     this.nicknameController = nicknameController;
   }
 
 
   
   public Text transform(String param) {
     return this.nicknameController.getPlayer(param)
       .map(data -> (Text)data.getNickname().map(TextSerializers.FORMATTING_CODE::deserialize).orElseGet(null))
 
       
       .orElseGet(() -> Text.of(new Object[] { TextColors.WHITE, param }));
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\controller\NicknameManagerTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */