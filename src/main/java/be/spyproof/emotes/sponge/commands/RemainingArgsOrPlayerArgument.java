 package be.spyproof.emotes.sponge.commands;

 import org.spongepowered.api.Sponge;
 import org.spongepowered.api.command.CommandSource;
 import org.spongepowered.api.command.args.ArgumentParseException;
 import org.spongepowered.api.command.args.CommandArgs;
 import org.spongepowered.api.command.args.CommandContext;
 import org.spongepowered.api.command.args.CommandElement;
 import org.spongepowered.api.text.Text;

 import javax.annotation.Nullable;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Optional;
 import java.util.stream.Collectors;
 
 public class RemainingArgsOrPlayerArgument
   extends CommandElement
 {
   public RemainingArgsOrPlayerArgument(@Nullable Text key) {
     super(key);
   }
 
 
   
   @Nullable
   protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
     StringBuilder ret = new StringBuilder(args.next());
     while (args.hasNext()) {
       ret.append(' ').append(args.next());
     }
     return ret.toString();
   }
 
 
   
   public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
     Optional<String> nextArg = args.nextIfPresent();
     if (nextArg.isPresent()) {
       return (List<String>)Sponge.getGame().getServer().getOnlinePlayers().stream()
         .map(input -> (input == null) ? null : input.getName())
         .filter(name -> (name != null && name.startsWith(nextArg.get())))
         .collect(Collectors.toList());
     }
     return new ArrayList<>();
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\RemainingArgsOrPlayerArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */