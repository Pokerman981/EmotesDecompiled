/*     */ package be.spyproof.emotes.sponge.commands;
/*     */ import be.spyproof.emotes.da.IStorage;
/*     */ import be.spyproof.emotes.model.Emote;
/*     */ import be.spyproof.emotes.sponge.controller.INameTransformer;
/*     */ import be.spyproof.emotes.sponge.util.TextBuilder;
/*     */ import be.spyproof.emotes.utils.MapUtils;
/*     */ import java.util.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import org.spongepowered.api.Sponge;
/*     */ import org.spongepowered.api.command.CommandException;
/*     */ import org.spongepowered.api.command.CommandResult;
/*     */ import org.spongepowered.api.command.CommandSource;
/*     */ import org.spongepowered.api.command.args.CommandContext;
/*     */ import org.spongepowered.api.command.spec.CommandExecutor;
/*     */ import org.spongepowered.api.entity.living.player.Player;
/*     */ import org.spongepowered.api.service.pagination.PaginationList;
/*     */ import org.spongepowered.api.text.LiteralText;
/*     */ import org.spongepowered.api.text.Text;
/*     */ import org.spongepowered.api.text.action.ClickAction;
/*     */ import org.spongepowered.api.text.action.HoverAction;
/*     */ import org.spongepowered.api.text.action.TextActions;
/*     */ import org.spongepowered.api.text.channel.MessageChannel;
/*     */ import org.spongepowered.api.text.channel.MessageReceiver;
/*     */ import org.spongepowered.api.text.format.TextColors;
/*     */ import org.spongepowered.api.text.serializer.TextSerializers;
/*     */ 
/*     */ public class ListEmotesCommand implements CommandExecutor {
/*  32 */   private static final Random rand = new Random(System.currentTimeMillis());
/*  33 */   public static final String[] keys = new String[] { "emotes", "emote" };
/*     */   
/*     */   private IStorage<Emote, ?> emotes;
/*     */   private INameTransformer nameTransformer;
/*     */   private MessageChannel messageChannel;
/*     */   private final String noPermissionsMessage;
/*     */   
/*     */   public ListEmotesCommand(IStorage<Emote, ?> emotes, INameTransformer nameTransformer, MessageChannel messageChannel, String noPermissionsMessage) {
/*  41 */     this.emotes = emotes;
/*  42 */     this.nameTransformer = nameTransformer;
/*  43 */     this.messageChannel = messageChannel;
/*  44 */     this.noPermissionsMessage = noPermissionsMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
/*  50 */     Optional<Emote> emote = args.getOne("emote");
/*  51 */     return emote
/*  52 */       .<CommandResult>map(formattedEmote -> pushEmote(src, args, formattedEmote))
/*  53 */       .orElseGet(() -> listEmotes(src));
/*     */   }
/*     */ 
/*     */   
/*     */   private CommandResult pushEmote(CommandSource src, CommandContext args, Emote emote) {
/*  58 */     if (!src.hasPermission("emotes.command.${emote}".replace("${emote}", emote.getName()))) {
/*     */       
/*  60 */       String noPermissionsMessage = this.noPermissionsMessage.replace("${user}", src.getName());
/*  61 */       noPermissionsMessage = noPermissionsMessage.replace("${perm}", "emotes.command.${emote}".replace("${emote}", emote.getName()));
/*  62 */       src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(noPermissionsMessage));
/*  63 */       return CommandResult.empty();
/*     */     } 
/*     */     
/*  66 */     Optional<String> receiver = args.getOne("receiver");
/*  67 */     Optional<Text> receiverName = receiver.map(this.nameTransformer::transform);
/*  68 */     Text senderName = this.nameTransformer.transform(src.getName());
/*     */ 
/*     */ 
/*     */     
/*  72 */     String[] lines = receiverName.<String[]>map(name -> emote.getOtherLines()).orElseGet(emote::getSelfLines);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     Text[] messages = receiverName.<Text[]>map(name -> { Text[] texts = new Text[lines.length]; for (int i = 0; i < lines.length; i++) { TextBuilder builder = new TextBuilder(lines[i]); builder.addPlaceholder("${sender}", senderName); builder.addPlaceholder("${receiver}", name); texts[i] = builder.build(); }  return texts; }).orElseGet(() -> {
/*     */           Text[] texts = new Text[lines.length];
/*     */           
/*     */           for (int i = 0; i < lines.length; i++) {
/*     */             TextBuilder builder = new TextBuilder(lines[i]);
/*     */             builder.addPlaceholder("${sender}", senderName);
/*     */             texts[i] = builder.build();
/*     */           } 
/*     */           return texts;
/*     */         });
/*  95 */     for (Text text : messages) {
/*  96 */       this.messageChannel.send(src, text);
/*     */     }
/*  98 */     return CommandResult.success();
/*     */   }
/*     */ 
/*     */   
/*     */   private CommandResult listEmotes(CommandSource src) {
/* 103 */     Text senderName = this.nameTransformer.transform(src.getName());
/*     */     
/* 105 */     Map<String, String> params = MapUtils.toMap("${sender}", TextSerializers.FORMATTING_CODE.serialize(senderName));
/* 106 */     Map<Emote, Text> contentMap = new LinkedHashMap<>();
/*     */     
/* 108 */     List<Emote> emotes = new ArrayList<>(this.emotes.getAll());
/* 109 */     emotes.sort(Comparator.comparing(Emote::getName));
/* 110 */     for (Emote emote : emotes) {
/*     */       
/* 112 */       Text receiverName = this.nameTransformer.transform(getRandomPlayerName(new String[] { src.getName() }));
/* 113 */       params.put("${receiver}", TextSerializers.FORMATTING_CODE.serialize(receiverName));
/*     */ 
/*     */ 
/*     */       
/* 117 */       Text hover = Text.join(new Text[] { Text.of(new Object[] { TextColors.YELLOW, ">> /", keys[0], Character.valueOf(' '), emote.getName(), " <<" }), (Text)Text.newLine()});
/* 118 */       String[] lines = emote.getSelfLines();
/* 119 */       Text[] texts = new Text[lines.length]; int i;
/* 120 */       for (i = 0; i < lines.length; i++) {
/* 121 */         TextBuilder textBuilder = new TextBuilder(lines[i]);
/* 122 */         textBuilder.addPlaceholder("${sender}", senderName);
/* 123 */         textBuilder.addPlaceholder("${receiver}", receiverName);
/* 124 */         texts[i] = textBuilder.build();
/*     */       } 
/* 126 */       for (i = 0; i < texts.length; i++) {
/*     */         
/* 128 */         hover = Text.join(new Text[] { hover, texts[i] });
/* 129 */         if (i + 1 > texts.length) {
/* 130 */           hover = Text.join(new Text[] { hover, (Text)Text.newLine() });
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 135 */       hover = Text.join(new Text[] { hover, (Text)Text.newLine(), (Text)Text.newLine(), Text.of(new Object[] { TextColors.YELLOW, ">> /", keys[0], Character.valueOf(' '), emote.getName(), Character.valueOf(' '), receiverName, " <<" }), (Text)Text.newLine() });
/* 136 */       lines = emote.getOtherLines();
/* 137 */       texts = new Text[lines.length];
/* 138 */       for (i = 0; i < lines.length; i++) {
/* 139 */         TextBuilder textBuilder = new TextBuilder(lines[i]);
/* 140 */         textBuilder.addPlaceholder("${sender}", senderName);
/* 141 */         textBuilder.addPlaceholder("${receiver}", receiverName);
/* 142 */         texts[i] = textBuilder.build();
/*     */       } 
/* 144 */       for (i = 0; i < texts.length; i++) {
/*     */         
/* 146 */         hover = Text.join(new Text[] { hover, texts[i] });
/* 147 */         if (i + 1 > texts.length) {
/* 148 */           hover = Text.join(new Text[] { hover, (Text)Text.newLine() });
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 155 */       LiteralText.Builder builder1 = Text.builder("/" + keys[0] + " " + emote.getName()).onHover((HoverAction)TextActions.showText(hover)).onClick((ClickAction)TextActions.suggestCommand("/" + keys[0] + " " + emote.getName()));
/*     */       
/* 157 */       if (src.hasPermission("emotes.command.${emote}".replace("${emote}", emote.getName()))) {
/* 158 */         builder1.color(TextColors.AQUA);
/*     */       } else {
/* 160 */         builder1.color(TextColors.RED);
/*     */       } 
/* 162 */       contentMap.put(emote, builder1.build());
/*     */     } 
/*     */     
/* 165 */     List<Text> contentList = new ArrayList<>(contentMap.size());
/* 166 */     for (Map.Entry<Emote, Text> entry : contentMap.entrySet()) {
/* 167 */       if (src.hasPermission("emotes.command.${emote}".replace("${emote}", ((Emote)entry.getKey()).getName())))
/* 168 */         contentList.add(entry.getValue()); 
/* 169 */     }  for (Map.Entry<Emote, Text> entry : contentMap.entrySet()) {
/* 170 */       if (!src.hasPermission("emotes.command.${emote}".replace("${emote}", ((Emote)entry.getKey()).getName())))
/* 171 */         contentList.add(entry.getValue()); 
/*     */     } 
/* 173 */     PaginationList.Builder builder = PaginationList.builder();
/* 174 */     builder.title(
/* 175 */         (Text)Text.builder("Emotes")
/* 176 */         .color(TextColors.BLUE)
/* 177 */         .onHover((HoverAction)TextActions.showText(Text.of(new Object[] {
/*     */                 
/*     */                 TextColors.GOLD, "Created by: ", TextColors.YELLOW, "TPNils", Text.newLine(), TextColors.GOLD, "Version: ", TextColors.YELLOW, "1.1.1"
/* 180 */               }))).build());
/* 181 */     builder.contents(contentList);
/* 182 */     builder.linesPerPage(10);
/* 183 */     builder.sendTo((MessageReceiver)src);
/*     */     
/* 185 */     return CommandResult.success();
/*     */   }
/*     */ 
/*     */   
/*     */   private String getRandomPlayerName(String... exclude) {
/* 190 */     List<String> playerNames = new ArrayList<>();
/* 191 */     playerNames.add("Notch");
/* 192 */     playerNames.add("Console");
/* 193 */     playerNames.add("TPNils");
/* 194 */     playerNames.add("Shmeeb");
/* 195 */     playerNames.add("Pokerpleb99");
/*     */     
/* 197 */     for (Player player : Sponge.getServer().getOnlinePlayers()) {
/*     */       
/* 199 */       boolean doExclude = false;
/* 200 */       for (String s : exclude) {
/* 201 */         if (s.equalsIgnoreCase(player.getName()))
/* 202 */           doExclude = true; 
/*     */       } 
/* 204 */       if (!doExclude) {
/* 205 */         playerNames.add(player.getName());
/*     */       }
/*     */     } 
/* 208 */     return playerNames.get(rand.nextInt(playerNames.size()));
/*     */   }
/*     */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\ListEmotesCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */