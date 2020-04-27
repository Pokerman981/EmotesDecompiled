/*    */ package be.spyproof.emotes.sponge.commands;
/*    */ 
/*    */ import be.spyproof.emotes.da.IStorage;
/*    */ import be.spyproof.emotes.model.Emote;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Random;
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
/*    */ public class GiveRandomEmoteCommand
/*    */   implements CommandExecutor
/*    */ {
/* 23 */   public static final String[] keys = new String[] { "give-random-emote", "gre" };
/* 24 */   private final Random random = new Random(System.currentTimeMillis());
/*    */   
/*    */   private final IStorage<Emote, ?> storage;
/*    */   private final String givePermissionCommand;
/*    */   private final String backupCommand;
/*    */   private final String givePermissionMessage;
/*    */   private final String backupMessage;
/*    */   
/*    */   public GiveRandomEmoteCommand(IStorage<Emote, ?> storage, String givePermissionCommand, String backupCommand, String givePermissionMessage, String backupMessage) {
/* 33 */     this.storage = storage;
/* 34 */     this.givePermissionCommand = givePermissionCommand.replaceFirst("^/", "");
/* 35 */     this.backupCommand = backupCommand.replaceFirst("^/", "");
/* 36 */     this.givePermissionMessage = (givePermissionMessage == null) ? "" : givePermissionMessage;
/* 37 */     this.backupMessage = (backupMessage == null) ? "" : backupMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
/* 43 */     Optional<Player> optional = args.getOne("player");
/* 44 */     Player player = optional.get();
/* 45 */     List<Emote> emotes = new ArrayList<>(this.storage.getAll());
/*    */ 
/*    */     
/* 48 */     Iterator<Emote> iterator = emotes.iterator();
/* 49 */     while (iterator.hasNext()) {
/* 50 */       if (player.hasPermission("emotes.command.${emote}".replace("${emote}", ((Emote)iterator.next()).getName()))) {
/* 51 */         iterator.remove();
/*    */       }
/*    */     } 
/* 54 */     if (emotes.size() == 0) {
/*    */       
/* 56 */       Sponge.getCommandManager().process(
/* 57 */           (CommandSource)Sponge.getServer().getConsole(), this.backupCommand
/* 58 */           .replace("${user}", player.getName()).replaceFirst("^/", ""));
/*    */       
/* 60 */       if (!this.backupMessage.isEmpty()) {
/* 61 */         player.sendMessage(TextSerializers.FORMATTING_CODE
/* 62 */             .deserialize(this.backupMessage
/* 63 */               .replace("${user}", player.getName())));
/*    */       }
/*    */       
/* 66 */       return CommandResult.empty();
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 71 */     Emote emote = emotes.get(this.random.nextInt(emotes.size()));
/* 72 */     Sponge.getCommandManager().process(
/* 73 */         (CommandSource)Sponge.getServer().getConsole(), this.givePermissionCommand
/* 74 */         .replace("${user}", player.getName())
/* 75 */         .replace("${perm}", "emotes.command.${emote}".replace("${emote}", emote.getName()))
/* 76 */         .replaceFirst("^/", ""));
/*    */     
/* 78 */     if (!this.givePermissionMessage.isEmpty()) {
/* 79 */       player.sendMessage(TextSerializers.FORMATTING_CODE
/* 80 */           .deserialize(this.givePermissionMessage
/*    */             
/* 82 */             .replace("${user}", player.getName())
/* 83 */             .replace("${emote}", emote.getName())));
/*    */     }
/*    */     
/* 86 */     return CommandResult.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\GiveRandomEmoteCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */