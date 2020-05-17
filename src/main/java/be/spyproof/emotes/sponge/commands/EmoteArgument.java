 package be.spyproof.emotes.sponge.commands;

 import be.spyproof.emotes.da.IStorage;
 import be.spyproof.emotes.model.Emote;
 import org.spongepowered.api.command.CommandSource;
 import org.spongepowered.api.command.args.ArgumentParseException;
 import org.spongepowered.api.command.args.CommandArgs;
 import org.spongepowered.api.command.args.CommandContext;
 import org.spongepowered.api.command.args.CommandElement;
 import org.spongepowered.api.text.Text;

 import javax.annotation.Nullable;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;
 import java.util.Optional;
 import java.util.stream.Collectors;
 
 
 
 
 
 
 public class EmoteArgument
   extends CommandElement
 {
   private IStorage<Emote, String> emoteStorage;
   
   public EmoteArgument(String key, IStorage<Emote, String> emoteStorage) {
     super((Text)Text.of(key));
     this.emoteStorage = emoteStorage;
   }
 
 
   
   @Nullable
   protected Emote parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
     if (!args.hasNext()) {
       return null;
     }
     String emote = args.peek();
     Optional<Emote> emoteOptional = this.emoteStorage.get(emote);
     if (emoteOptional.isPresent()) {
       
       args.next();
       return emoteOptional.get();
     } 
     
     return null;
   }
 
 
   
   public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
     Optional<String> arg = args.nextIfPresent();
     List<String> completion = new ArrayList<>();
     
     if (arg.isPresent()) {
       completion.addAll((Collection<? extends String>)this.emoteStorage.getAll()
           .stream()
           .filter(emote -> emote.getName().toLowerCase().startsWith(((String)arg.get()).toLowerCase()))
           .filter(emote -> src.hasPermission("emotes.command.${emote}".replace("${emote}", emote.getName())))
           .map(Emote::getName)
           .collect(Collectors.toList()));
     } else {
       completion.addAll((Collection<? extends String>)this.emoteStorage.getAll()
           .stream()
           .filter(emote -> src.hasPermission("emotes.command.${emote}".replace("${emote}", emote.getName())))
           .map(Emote::getName)
           .collect(Collectors.toList()));
     } 
     return completion;
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\EmoteArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */