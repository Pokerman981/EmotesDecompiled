 package be.spyproof.emotes.sponge.commands;

 import be.spyproof.emotes.model.Emote;
 import org.spongepowered.api.Sponge;
 import org.spongepowered.api.command.CommandException;
 import org.spongepowered.api.command.CommandResult;
 import org.spongepowered.api.command.CommandSource;
 import org.spongepowered.api.command.args.CommandContext;
 import org.spongepowered.api.command.spec.CommandExecutor;
 import org.spongepowered.api.entity.living.player.Player;
 import org.spongepowered.api.text.serializer.TextSerializers;

 import java.util.Optional;
 
 
 
 public class GiveEmoteCommand
   implements CommandExecutor
 {
   public static final String[] keys = new String[] { "give-emote", "ge" };
   
   private final String givePermissionCommand;
   private final String givePermissionMessage;
   
   public GiveEmoteCommand(String givePermissionCommand, String givePermissionMessage) {
     this.givePermissionCommand = givePermissionCommand.replaceFirst("^/", "");
     this.givePermissionMessage = (givePermissionMessage == null) ? "" : givePermissionMessage;
   }
 
 
   
   public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
     Optional<Player> optionalPlayer = args.getOne("player");
     Optional<Emote> optionalEmote = args.getOne("emote");
     Player player = optionalPlayer.get();
     Emote emote = optionalEmote.get();
     
     Sponge.getCommandManager().process(
         (CommandSource)Sponge.getServer().getConsole(), this.givePermissionCommand
         .replace("${user}", player.getName())
         .replace("${perm}", "emotes.command.${emote}".replace("${emote}", emote.getName()))
         .replaceFirst("^/", ""));
     
     if (!this.givePermissionMessage.isEmpty()) {
       player.sendMessage(TextSerializers.FORMATTING_CODE
           .deserialize(this.givePermissionMessage
             
             .replace("${user}", player.getName())
             .replace("${emote}", emote.getName())));
     }
     
     return CommandResult.empty();
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\GiveEmoteCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */