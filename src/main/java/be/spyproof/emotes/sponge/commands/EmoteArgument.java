/*    */ package be.spyproof.emotes.sponge.commands;
/*    */ 
/*    */ import be.spyproof.emotes.da.IStorage;
/*    */ import be.spyproof.emotes.model.Emote;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import org.spongepowered.api.command.CommandSource;
/*    */ import org.spongepowered.api.command.args.ArgumentParseException;
/*    */ import org.spongepowered.api.command.args.CommandArgs;
/*    */ import org.spongepowered.api.command.args.CommandContext;
/*    */ import org.spongepowered.api.command.args.CommandElement;
/*    */ import org.spongepowered.api.text.Text;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmoteArgument
/*    */   extends CommandElement
/*    */ {
/*    */   private IStorage<Emote, String> emoteStorage;
/*    */   
/*    */   public EmoteArgument(String key, IStorage<Emote, String> emoteStorage) {
/* 29 */     super((Text)Text.of(key));
/* 30 */     this.emoteStorage = emoteStorage;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Emote parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
/* 37 */     if (!args.hasNext()) {
/* 38 */       return null;
/*    */     }
/* 40 */     String emote = args.peek();
/* 41 */     Optional<Emote> emoteOptional = this.emoteStorage.get(emote);
/* 42 */     if (emoteOptional.isPresent()) {
/*    */       
/* 44 */       args.next();
/* 45 */       return emoteOptional.get();
/*    */     } 
/*    */     
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
/* 54 */     Optional<String> arg = args.nextIfPresent();
/* 55 */     List<String> completion = new ArrayList<>();
/*    */     
/* 57 */     if (arg.isPresent()) {
/* 58 */       completion.addAll((Collection<? extends String>)this.emoteStorage.getAll()
/* 59 */           .stream()
/* 60 */           .filter(emote -> emote.getName().toLowerCase().startsWith(((String)arg.get()).toLowerCase()))
/* 61 */           .filter(emote -> src.hasPermission("emotes.command.${emote}".replace("${emote}", emote.getName())))
/* 62 */           .map(Emote::getName)
/* 63 */           .collect(Collectors.toList()));
/*    */     } else {
/* 65 */       completion.addAll((Collection<? extends String>)this.emoteStorage.getAll()
/* 66 */           .stream()
/* 67 */           .filter(emote -> src.hasPermission("emotes.command.${emote}".replace("${emote}", emote.getName())))
/* 68 */           .map(Emote::getName)
/* 69 */           .collect(Collectors.toList()));
/*    */     } 
/* 71 */     return completion;
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\EmoteArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */