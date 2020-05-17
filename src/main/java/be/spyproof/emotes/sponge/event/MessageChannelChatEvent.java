 package be.spyproof.emotes.sponge.event;

 import org.spongepowered.api.event.cause.Cause;
 import org.spongepowered.api.event.message.MessageChannelEvent;
 import org.spongepowered.api.event.message.MessageEvent;
 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.channel.MessageChannel;

 import javax.annotation.Nullable;
 import java.util.Optional;
 
 
 
 public class MessageChannelChatEvent
   implements MessageChannelEvent.Chat
 {
   private Text rawMessage;
   private MessageChannel originalChannel;
   private MessageChannel channel;
   private MessageEvent.MessageFormatter messageFormatter = new MessageEvent.MessageFormatter();
   
   private boolean isCancelled = false;
   private Cause cause;
   
   public MessageChannelChatEvent(Text rawMessage, MessageChannel originalChannel, Cause cause) {
     this.rawMessage = rawMessage;
     this.originalChannel = originalChannel;
     this.channel = originalChannel;
     this.cause = cause;
   }
 
 
   
   public Text getRawMessage() {
     return this.rawMessage;
   }
 
 
   
   public boolean isCancelled() {
     return this.isCancelled;
   }
 
 
   
   public void setCancelled(boolean cancel) {
     this.isCancelled = cancel;
   }
 
 
   
   public MessageChannel getOriginalChannel() {
     return this.originalChannel;
   }
 
 
   
   public Optional<MessageChannel> getChannel() {
     return Optional.ofNullable(this.channel);
   }
 
 
   
   public void setChannel(@Nullable MessageChannel channel) {
     this.channel = channel;
   }
 
 
   
   public Text getOriginalMessage() {
     return this.rawMessage;
   }
 
 
   
   public boolean isMessageCancelled() {
     return this.isCancelled;
   }
 
 
   
   public void setMessageCancelled(boolean cancelled) {
     this.isCancelled = cancelled;
   }
 
 
   
   public MessageEvent.MessageFormatter getFormatter() {
     return this.messageFormatter;
   }
 
 
   
   public Cause getCause() {
     return this.cause;
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\event\MessageChannelChatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */