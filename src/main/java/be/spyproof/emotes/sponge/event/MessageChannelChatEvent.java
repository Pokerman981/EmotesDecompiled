/*    */ package be.spyproof.emotes.sponge.event;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import javax.annotation.Nullable;
/*    */ import org.spongepowered.api.event.cause.Cause;
/*    */ import org.spongepowered.api.event.message.MessageChannelEvent;
/*    */ import org.spongepowered.api.event.message.MessageEvent;
/*    */ import org.spongepowered.api.text.Text;
/*    */ import org.spongepowered.api.text.channel.MessageChannel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageChannelChatEvent
/*    */   implements MessageChannelEvent.Chat
/*    */ {
/*    */   private Text rawMessage;
/*    */   private MessageChannel originalChannel;
/*    */   private MessageChannel channel;
/* 19 */   private MessageEvent.MessageFormatter messageFormatter = new MessageEvent.MessageFormatter();
/*    */   
/*    */   private boolean isCancelled = false;
/*    */   private Cause cause;
/*    */   
/*    */   public MessageChannelChatEvent(Text rawMessage, MessageChannel originalChannel, Cause cause) {
/* 25 */     this.rawMessage = rawMessage;
/* 26 */     this.originalChannel = originalChannel;
/* 27 */     this.channel = originalChannel;
/* 28 */     this.cause = cause;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Text getRawMessage() {
/* 34 */     return this.rawMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 40 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 46 */     this.isCancelled = cancel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MessageChannel getOriginalChannel() {
/* 52 */     return this.originalChannel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<MessageChannel> getChannel() {
/* 58 */     return Optional.ofNullable(this.channel);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChannel(@Nullable MessageChannel channel) {
/* 64 */     this.channel = channel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Text getOriginalMessage() {
/* 70 */     return this.rawMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMessageCancelled() {
/* 76 */     return this.isCancelled;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMessageCancelled(boolean cancelled) {
/* 82 */     this.isCancelled = cancelled;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MessageEvent.MessageFormatter getFormatter() {
/* 88 */     return this.messageFormatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Cause getCause() {
/* 94 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\event\MessageChannelChatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */