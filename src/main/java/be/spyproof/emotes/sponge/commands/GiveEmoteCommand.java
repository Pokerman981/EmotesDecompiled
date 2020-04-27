/*    */ package be.spyproof.emotes.sponge.commands;
/*    */ 
/*    */ import be.spyproof.emotes.model.Emote;
/*    */ import java.util.Optional;
/*    */ import org.spongepowered.api.Sponge;
/*    */ import org.spongepowered.api.command.CommandException;
/*    */ import org.spongepowered.api.command.CommandResult;
/*    */ import org.spongepowered.api.command.CommandSource;
/*    */ import org.spongepowered.api.command.args.CommandContext;
/*    */ import org.spongepowered.api.command.spec.CommandExecutor;
/*    */ import org.spongepowered.api.entity.living.player.Player;
/*    */ import org.spongepowered.api.text.serializer.TextSerializers;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GiveEmoteCommand
/*    */   implements CommandExecutor
/*    */ {
/* 19 */   public static final String[] keys = new String[] { "give-emote", "ge" };
/*    */   
/*    */   private final String givePermissionCommand;
/*    */   private final String givePermissionMessage;
/*    */   
/*    */   public GiveEmoteCommand(String givePermissionCommand, String givePermissionMessage) {
/* 25 */     this.givePermissionCommand = givePermissionCommand.replaceFirst("^/", "");
/* 26 */     this.givePermissionMessage = (givePermissionMessage == null) ? "" : givePermissionMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
/* 32 */     Optional<Player> optionalPlayer = args.getOne("player");
/* 33 */     Optional<Emote> optionalEmote = args.getOne("emote");
/* 34 */     Player player = optionalPlayer.get();
/* 35 */     Emote emote = optionalEmote.get();
/*    */     
/* 37 */     Sponge.getCommandManager().process(
/* 38 */         (CommandSource)Sponge.getServer().getConsole(), this.givePermissionCommand
/* 39 */         .replace("${user}", player.getName())
/* 40 */         .replace("${perm}", "emotes.command.${emote}".replace("${emote}", emote.getName()))
/* 41 */         .replaceFirst("^/", ""));
/*    */     
/* 43 */     if (!this.givePermissionMessage.isEmpty()) {
/* 44 */       player.sendMessage(TextSerializers.FORMATTING_CODE
/* 45 */           .deserialize(this.givePermissionMessage
/*    */             
/* 47 */             .replace("${user}", player.getName())
/* 48 */             .replace("${emote}", emote.getName())));
/*    */     }
/*    */     
/* 51 */     return CommandResult.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\GiveEmoteCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */