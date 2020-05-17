 package be.spyproof.emotes.sponge.channel;


import be.spyproof.emotes.sponge.event.MessageChannelChatEvent;
import com.google.common.base.Preconditions;
import com.magitechserver.magibridge.discord.DiscordMessageBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.ChatTypeMessageReceiver;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


 
 
 
 
 public class EmotesChannel
   implements MessageChannel
 {
   private List<EventListener<MessageChannelEvent.Chat>> listeners = new ArrayList<>();
 
   
   public void addListener(EventListener<MessageChannelEvent.Chat> listener) {
     this.listeners.add(listener);
   }
 
 
   
   public Collection<MessageReceiver> getMembers() {
     return Sponge.getServer()
       .getOnlinePlayers()
       .stream()
       .filter(player -> player.hasPermission("emotes.channel.listen"))
       .collect(Collectors.toList());
   }
 
 
   
   public void send(@Nullable Object sender, Text original, ChatType type) {
     Preconditions.checkNotNull(original, "original text");
     Preconditions.checkNotNull(type, "type");
     Cause.Builder builder = Cause.builder().append(this);
     if (sender != null) {
       builder.append(sender);
     }
     Cause cause = builder.build(EventContext.empty());
     
     MessageChannelChatEvent event = new MessageChannelChatEvent(original, this, cause);
 
 
 
 
     
     for (EventListener<MessageChannelEvent.Chat> listener : this.listeners) {
       
       try {
         listener.handle(event);
       } catch (Exception e) {
         
         e.printStackTrace();
       } 
     } 
     if (event.isCancelled() || event.isMessageCancelled()) {
       return;
     }

            StringBuilder message = new StringBuilder("✧" +
                    original.toPlain().replaceFirst("\\*", "")
                            .replaceFirst("\\*", "✧")
                            .replaceAll("`", "")); // This replace is to stop people from inserting code blocks

            try { // Send the message to the default discord channel from the magi discord bot
                DiscordMessageBuilder.forDefaultChannel()
                        .useWebhook(false)
                        .message(message.toString())
                        .send();
            } catch (Exception e) {
                e.printStackTrace();
            }


     for (MessageReceiver member : getMembers()) {
       
       if (member instanceof ChatTypeMessageReceiver) {

         transformMessage(sender, member, original, type).ifPresent(text -> ((ChatTypeMessageReceiver)member).sendMessage(type, text)); continue;
       } 
       transformMessage(sender, member, original, type).ifPresent(member::sendMessage);
     } 
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\channel\EmotesChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */