/*    */ package be.spyproof.emotes.sponge.commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import org.spongepowered.api.Sponge;
/*    */ import org.spongepowered.api.command.CommandSource;
/*    */ import org.spongepowered.api.command.args.ArgumentParseException;
/*    */ import org.spongepowered.api.command.args.CommandArgs;
/*    */ import org.spongepowered.api.command.args.CommandContext;
/*    */ import org.spongepowered.api.command.args.CommandElement;
/*    */ import org.spongepowered.api.entity.living.player.Player;
/*    */ import org.spongepowered.api.text.Text;
/*    */ 
/*    */ public class RemainingArgsOrPlayerArgument
/*    */   extends CommandElement
/*    */ {
/*    */   public RemainingArgsOrPlayerArgument(@Nullable Text key) {
/* 21 */     super(key);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
/* 28 */     StringBuilder ret = new StringBuilder(args.next());
/* 29 */     while (args.hasNext()) {
/* 30 */       ret.append(' ').append(args.next());
/*    */     }
/* 32 */     return ret.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
/* 38 */     Optional<String> nextArg = args.nextIfPresent();
/* 39 */     if (nextArg.isPresent()) {
/* 40 */       return (List<String>)Sponge.getGame().getServer().getOnlinePlayers().stream()
/* 41 */         .map(input -> (input == null) ? null : input.getName())
/* 42 */         .filter(name -> (name != null && name.startsWith(nextArg.get())))
/* 43 */         .collect(Collectors.toList());
/*    */     }
/* 45 */     return new ArrayList<>();
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\RemainingArgsOrPlayerArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */