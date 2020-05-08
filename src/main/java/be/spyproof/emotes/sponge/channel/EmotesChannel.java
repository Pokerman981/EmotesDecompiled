/*    */ package be.spyproof.emotes.sponge.channel;
/*    */ 
/*    */ import be.spyproof.emotes.sponge.event.MessageChannelChatEvent;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import org.spongepowered.api.Sponge;
/*    */ import org.spongepowered.api.entity.living.player.Player;
/*    */ import org.spongepowered.api.event.Event;
/*    */ import org.spongepowered.api.event.EventListener;
/*    */ import org.spongepowered.api.event.cause.Cause;
/*    */ import org.spongepowered.api.event.cause.EventContext;
/*    */ import org.spongepowered.api.event.message.MessageChannelEvent;
/*    */ import org.spongepowered.api.text.Text;
/*    */ import org.spongepowered.api.text.channel.ChatTypeMessageReceiver;
/*    */ import org.spongepowered.api.text.channel.MessageChannel;
/*    */ import org.spongepowered.api.text.channel.MessageReceiver;
/*    */ import org.spongepowered.api.text.chat.ChatType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmotesChannel
/*    */   implements MessageChannel
/*    */ {
/* 29 */   private List<EventListener<MessageChannelEvent.Chat>> listeners = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void addListener(EventListener<MessageChannelEvent.Chat> listener) {
/* 33 */     this.listeners.add(listener);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<MessageReceiver> getMembers() {
/* 39 */     return Sponge.getServer()
/* 40 */       .getOnlinePlayers()
/* 41 */       .stream()
/* 42 */       .filter(player -> player.hasPermission("emotes.channel.listen"))
/* 43 */       .collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void send(@Nullable Object sender, Text original, ChatType type) {
/* 49 */     Preconditions.checkNotNull(original, "original text");
/* 50 */     Preconditions.checkNotNull(type, "type");
/* 51 */     Cause.Builder builder = Cause.builder().append(this);
/* 52 */     if (sender != null) {
/* 53 */       builder.append(sender);
/*    */     }
/* 55 */     Cause cause = builder.build(EventContext.empty());
/*    */     
/* 57 */     MessageChannelChatEvent event = new MessageChannelChatEvent(original, this, cause);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     for (EventListener<MessageChannelEvent.Chat> listener : this.listeners) {
/*    */       
/*    */       try {
/* 66 */         listener.handle(event);
/* 67 */       } catch (Exception e) {
/*    */         
/* 69 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/* 72 */     if (event.isCancelled() || event.isMessageCancelled()) {
/*    */       return;
/*    */     }
/*    */     
/* 76 */     for (MessageReceiver member : getMembers()) {
/*    */       
/* 78 */       if (member instanceof ChatTypeMessageReceiver) {
/* 79 */         transformMessage(sender, member, original, type).ifPresent(text -> ((ChatTypeMessageReceiver)member).sendMessage(type, text)); continue;
/*    */       } 
/* 81 */       transformMessage(sender, member, original, type).ifPresent(member::sendMessage);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\channel\EmotesChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */