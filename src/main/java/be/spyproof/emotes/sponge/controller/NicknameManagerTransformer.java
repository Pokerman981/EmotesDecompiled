 package be.spyproof.emotes.sponge.controller;

 import be.spyproof.nickmanager.controller.ISpongeNicknameController;
 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.format.TextColors;
 import org.spongepowered.api.text.format.TextStyles;
 import org.spongepowered.api.text.serializer.TextSerializers;
 
 
 
 public class NicknameManagerTransformer
   implements INameTransformer
 {
   private final ISpongeNicknameController nicknameController;
   
   public NicknameManagerTransformer(ISpongeNicknameController nicknameController) {
     this.nicknameController = nicknameController;
   }
 
 
   
   public Text transform(String param) {

     return Text.of(this.nicknameController.getPlayer(param)
             .map(data -> (data.getNickname().map(TextSerializers.FORMATTING_CODE::deserialize).orElse(null)))
             .orElse(Text.of(TextColors.WHITE, param))
             , TextColors.RESET, TextStyles.RESET);
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\controller\NicknameManagerTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */