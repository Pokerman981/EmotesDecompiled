/*    */ package be.spyproof.emotes.sponge.listener;
/*    */ 
/*    */ import br.net.fabiozumbi12.UltimateChat.Sponge.API.uChatAPI;
/*    */ import br.net.fabiozumbi12.UltimateChat.Sponge.UCChannel;
/*    */ import br.net.fabiozumbi12.UltimateChat.Sponge.UChat;
/*    */ import java.util.Optional;
/*    */ import org.spongepowered.api.entity.living.player.Player;
/*    */ import org.spongepowered.api.event.Event;
/*    */ import org.spongepowered.api.event.EventListener;
/*    */ import org.spongepowered.api.event.message.MessageChannelEvent;
/*    */ import org.spongepowered.api.text.Text;
/*    */ import org.spongepowered.api.text.serializer.TextSerializers;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UChatListener
/*    */   implements EventListener<MessageChannelEvent.Chat>
/*    */ {
/* 19 */   private uChatAPI api = new uChatAPI();
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(MessageChannelEvent.Chat event) {
/* 24 */     Optional<Player> player = event.getCause().first(Player.class);
/* 25 */     if (!player.isPresent())
/*    */       return; 
/* 27 */     UCChannel channel = this.api.getPlayerChannel(player.get());
/* 28 */     if (channel != null && channel.isMuted(((Player)player.get()).getName())) {
/*    */       
/* 30 */       event.setMessageCancelled(true);
/* 31 */       ((Player)player.get()).sendMessage(getDeniedMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private Text getDeniedMessage() {
/* 37 */     return TextSerializers.FORMATTING_CODE.deserialize(UChat.get().getLang().get("channel.muted").replace("§", "&"));
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\listener\UChatListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */