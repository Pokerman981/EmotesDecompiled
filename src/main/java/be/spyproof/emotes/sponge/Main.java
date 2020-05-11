/*     */ package be.spyproof.emotes.sponge;
/*     */ 
/*     */

import be.spyproof.emotes.check.EmoteNameCheck;
import be.spyproof.emotes.check.EmoteStringKeyCheck;
import be.spyproof.emotes.da.CachedEmoteStorage;
import be.spyproof.emotes.da.MemoryStorage;
import be.spyproof.emotes.da.MySqlEmoteStorage;
import be.spyproof.emotes.event.EmoteEventBus;
import be.spyproof.emotes.event.EmoteUpdateEvent;
import be.spyproof.emotes.model.Emote;
import be.spyproof.emotes.service.PermissionService;
import be.spyproof.emotes.service.RegisterService;
import be.spyproof.emotes.sponge.channel.EmotesChannel;
import be.spyproof.emotes.sponge.commands.*;
import be.spyproof.emotes.sponge.controller.ConfigController;
import be.spyproof.emotes.sponge.controller.INameTransformer;
import be.spyproof.emotes.sponge.controller.NameTransformer;
import be.spyproof.emotes.sponge.controller.NicknameManagerTransformer;
import be.spyproof.emotes.sponge.listener.InvalidStorageNotifier;
import be.spyproof.emotes.sponge.listener.UChatListener;
import be.spyproof.emotes.sponge.util.ConversionUtils;
import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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
/*     */ @Plugin(id = "emotes-sponge", name = "Emotes", version = "1.1.1", description = "Emotes implementation for sponge", authors = {"TPNils"})
/*     */ public class Main
/*     */ {
/*     */   @Inject
/*     */   private Logger logger;
/*     */   @Inject
/*     */   @DefaultConfig(sharedRoot = true)
/*     */   private File configDir;
/*     */   private MemoryStorage<Emote, String> emoteStorage;
/*     */   private EmotesChannel emoteMessageChannel;
/*     */   private ConfigController config;
            public static Main instance;
/*     */   
/*     */   @Listener
/*     */   public void onPreInit(GamePreInitializationEvent event) throws IOException {
/*  77 */     this.logger.trace("PreInit");
/*     */
              instance = this;

/*  79 */     this.config = new ConfigController(this.configDir);
/*  80 */     this.config.load();
/*     */     
/*  82 */     if (MySqlEmoteStorage.canConnect(this.config.getMySQLHost(), this.config.getMySQLPort(), this.config.getMySQLDb(), this.config.getMySQLUser(), this.config.getMySQLPass())) {
/*  83 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  91 */         .emoteStorage = new CachedEmoteStorage<>(EmoteNameCheck.get(), EmoteStringKeyCheck.get(), new MySqlEmoteStorage(this.config.getMySQLHost(), this.config.getMySQLPort(), this.config.getMySQLDb(), this.config.getMySQLUser(), this.config.getMySQLPass()));
/*     */     }
/*     */     else {
/*     */       
/*  95 */       this
/*     */         
/*  97 */         .emoteStorage = new MemoryStorage<>(EmoteNameCheck.get(), EmoteStringKeyCheck.get());
/*     */     } 
/*     */ 
/*     */     
/* 101 */     EmoteEventBus.addListener(e -> {
/*     */           if (EmoteUpdateEvent.Action.SAVE.equals(e.getAction())) {
/*     */             this.emoteStorage.save(e.getEmote());
/*     */           } else if (EmoteUpdateEvent.Action.DELETE.equals(e.getAction())) {
/*     */             this.emoteStorage.remove(e.getEmote());
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 110 */     this.emoteMessageChannel = new EmotesChannel();
/*     */ 
/*     */     
/* 113 */     if (this.emoteStorage.getAll().size() == 0) {
/*     */       
/* 115 */       this.emoteStorage.save(ConversionUtils.createEmote("cutie", 
/* 116 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is a cutie ", TextColors.RESET, "◕ ◡ ◕"),
/* 117 */               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " thinks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is a cutie", TextColors.RESET, " ◕ ◡ ◕")));
/* 118 */       this.emoteStorage.save(ConversionUtils.createEmote("dealwithit", 
/* 119 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GRAY, " ╰▄︻▄╯", TextColors.DARK_GREEN, " deal with it"),
/* 120 */               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GRAY, " ╰▄︻▄╯", TextColors.DARK_GREEN, " deal with it ", TextColors.RESET, "${receiver}")));
/* 121 */       this.emoteStorage.save(ConversionUtils.createEmote("derp", 
/* 122 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " are a herp a derp ◖|◔◡◉|◗"),
/* 123 */               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " thinks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " are a herp a derp ◖|◔◡◉|◗")));
/* 124 */       this.emoteStorage.save(ConversionUtils.createEmote("facedesk", 
/* 125 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " slammed a desk on its face *"),
/* 126 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " slammed a desk on ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "'s face *")));
/* 127 */       this.emoteStorage.save(ConversionUtils.createEmote("facepalm", 
/* 128 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " facepalmed *") ,
/* 129 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " facepalmed for ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " *")));
/* 130 */       this.emoteStorage.save(ConversionUtils.createEmote("gasp", 
/* 131 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gasps * ⊙▃⊙"),
/* 132 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gasps at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ⊙▃⊙")));
/* 133 */       this.emoteStorage.save(ConversionUtils.createEmote("hugs", 
/* 134 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " hugs everyone * (づ｡◕‿‿◕｡)づ"),
/* 135 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " hugs ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * (づ｡◕‿‿◕｡)づ")));
/* 136 */       this.emoteStorage.save(ConversionUtils.createEmote("omg", 
/* 137 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " Oh... My... God... ಠ_ಠ"),
/* 138 */               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " Oh... My... God... Why ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "? ಠ_ಠ")));
/* 139 */       this.emoteStorage.save(ConversionUtils.createEmote("rage", 
/* 140 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rages out! * (╯°□°）╯︵ ┻━┻"),
/* 141 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rages out at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "! * (╯°□°）╯︵ ┻━┻  ╰(”◕﹏◕”)つ")));
/* 142 */       this.emoteStorage.save(ConversionUtils.createEmote("runs", 
/* 143 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " runs away * ᕕ(◉Д◉ )ᕗ"),
/* 144 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " runs away from ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ᕕ(◉Д◉ )ᕗ")));
/* 145 */       this.emoteStorage.save(ConversionUtils.createEmote("brb", 
/* 146 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " will be right back *"),
/* 147 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${receiver}", TextColors.DARK_GREEN, " o/ ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " will be right back ")));
/* 148 */       this.emoteStorage.save(ConversionUtils.createEmote("fart", 
/* 149 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " farted *"),
/* 150 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " farted in ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "'s face * ")));
/* 151 */       this.emoteStorage.save(ConversionUtils.createEmote("star", 
/* 152 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gives golden star to self * ", TextColors.GOLD, "★"),
/* 153 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gives ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " a golden star * ",
/* 154 */                   TextColors.GOLD, "★")));
                this.emoteStorage.save(ConversionUtils.createEmote("inlove", 
/* 155 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is in love ", TextColors.LIGHT_PURPLE, "♥.♥"),
/* 156 */               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is in love with ", TextColors.RESET, "${receiver} ", TextColors.LIGHT_PURPLE, "♥.♥")));
/* 157 */       this.emoteStorage.save(ConversionUtils.createEmote("sleepy", 
/* 158 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " feels sleepy right now Zzzz... ≈ ___≈"),
/* 159 */               Text.of(TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " makes ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " very sleepy Zzzz... ≈ ___≈")));
/* 160 */       this.emoteStorage.save(ConversionUtils.createEmote("bored", 
/* 161 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels bored * SIGH!"),
/* 162 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " SIGHS * ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is sooooo boring!")));
/* 163 */       this.emoteStorage.save(ConversionUtils.createEmote("crazy", 
/* 164 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is going crazzzyyyy!?! 0___0"),
/* 165 */               Text.of(TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is making ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " crazy! 0___0")));
/* 166 */       this.emoteStorage.save(ConversionUtils.createEmote("book", 
/* 167 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " reads a book *"),
/* 168 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " stops reading a book and throws it at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " *")));
/* 169 */       this.emoteStorage.save(ConversionUtils.createEmote("cry", 
/* 170 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " cries * ;-;"),
/* 171 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${receiver}", TextColors.DARK_GREEN, " makes ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " cry * ;-;")));
/* 172 */       this.emoteStorage.save(ConversionUtils.createEmote("stare", 
/* 173 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " stares into the distance * >.>"),
/* 174 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " stares at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " awkwardly * <.<")));
/* 175 */       this.emoteStorage.save(ConversionUtils.createEmote("notime", 
/* 176 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " ain't gat no time for that! \\>.</"),
/* 177 */               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " gat no time for ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "! \\>.</")));
/* 178 */       this.emoteStorage.save(ConversionUtils.createEmote("goofball", 
/* 179 */             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " feels like a goofball v_v"),
/* 180 */               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " thinks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is a goofball v.v")));
/* 181 */       this.emoteStorage.save(ConversionUtils.createEmote("rofl", 
/* 182 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rolls over the floor, laughing * "),
/* 183 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rolls over the floor, laughing at ", TextColors.RESET, "${receiver}")));
/* 184 */       this.emoteStorage.save(ConversionUtils.createEmote("daydream", 
/* 185 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " starts daydreaming * ◔.◔"),
/* 186 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " starts daydreaming about ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ◔.◔")));
/* 187 */       this.emoteStorage.save(ConversionUtils.createEmote("sorry", 
/* 188 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels sorry * D;"),
/* 189 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels sorry for ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * D;")));
/* 190 */       this.emoteStorage.save(ConversionUtils.createEmote("bite", 
/* 191 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " bites self * 0w0"),
/* 192 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " nibbles on ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ^w^")));
/* 193 */       this.emoteStorage.save(ConversionUtils.createEmote("scared", 
/* 194 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels scared * 【•】_【•】"),
/* 195 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " was scared by ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * 【•】_【•】")));
/* 196 */       this.emoteStorage.save(ConversionUtils.createEmote("poke", 
/* 197 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " pokes everyone * ☚"),
/* 198 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " pokes ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ☚")));
/* 199 */       this.emoteStorage.save(ConversionUtils.createEmote("wave", 
/* 200 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " waves * o/"),
/* 201 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " waves at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * o/")));
/* 202 */       this.emoteStorage.save(ConversionUtils.createEmote("cookie", 
/* 203 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " eats a cookie * ^_^"),
/* 204 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " shares a cookie with ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ^_^")));
/* 205 */       this.emoteStorage.save(ConversionUtils.createEmote("lick", 
/* 206 */             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " licks * ^_^"),
/* 207 */               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " licks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "'s face * ")));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Listener
/*     */   public void onInit(GameInitializationEvent event) {
/* 220 */     this.logger.trace("Init");
/* 221 */     if (this.emoteStorage.getClass() == MemoryStorage.class) {
/* 222 */       Sponge.getEventManager().registerListener(this, ClientConnectionEvent.Join.class, new InvalidStorageNotifier());
/*     */     }
/*     */     
/*     */     try {
/* 226 */       Class.forName("br.net.fabiozumbi12.UltimateChat.Sponge.UChat");
/* 227 */       this.emoteMessageChannel.addListener(new UChatListener());
/* 228 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Listener
/*     */   public void onPostInit(GamePostInitializationEvent event) {
/* 240 */     this.logger.trace("PostInit"); //Not sure if this works at all but what was decompiled surely didn't work either
/* 241 */     RegisterService.register(new PermissionService((perm, uuid) -> Sponge.getServer().getPlayer(uuid).map((player) -> null), false));
/*     */ 
/*     */           
/* 244 */
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Listener
/*     */   public void onServerStart(GameStartingServerEvent event) {
/*     */     NicknameManagerTransformer nicknameManagerTransformer = null;
/*     */     NameTransformer nameTransformer = new NameTransformer();
/* 257 */     this.logger.trace("Start");
/*     */ 
/*     */     
/* 260 */     CommandManager cmdService = Sponge.getCommandManager();
/* 261 */     INameTransformer transformer = null;
/*     */ 
/*     */     
/*     */     try {
/* 265 */       Class.forName("be.spyproof.nickmanager.controller.ISpongeNicknameController");
/* 266 */       Optional<ISpongeNicknameController> nicknameController = Sponge.getServiceManager().provide(ISpongeNicknameController.class);
/*     */       
/* 268 */       if (nicknameController.isPresent())
/* 269 */         nicknameManagerTransformer = new NicknameManagerTransformer(nicknameController.get()); 
/* 270 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/* 273 */     if (nicknameManagerTransformer == null) {
/* 274 */       nameTransformer = new NameTransformer();
/*     */     }
/* 276 */     CommandSpec.Builder emotesCmdBuilder = CommandSpec.builder();
/* 277 */     emotesCmdBuilder.executor(new ListEmotesCommand(this.emoteStorage, nameTransformer, this.emoteMessageChannel, this.config.noPermissionMsg()));
/* 278 */     emotesCmdBuilder.arguments(/* 279 */           GenericArguments.optional(new EmoteArgument("emote", this.emoteStorage)),
            /* 280 */           GenericArguments.optional(new RemainingArgsOrPlayerArgument(Text.of("receiver")))
            /*     */);
/*     */     
/* 283 */     CommandSpec.Builder randomEmoteBuilder = CommandSpec.builder();
/* 284 */     randomEmoteBuilder.executor(new GiveRandomEmoteCommand(this.emoteStorage, this.config
/*     */           
/* 286 */           .giveEmotePermissionCmd(), this.config
/* 287 */           .backupCmd(), this.config
/* 288 */           .giveEmotePermissionMsg(), this.config
/* 289 */           .backupMsg()));
/*     */     
/* 291 */     randomEmoteBuilder.arguments(GenericArguments.player(Text.of("player")));
/* 292 */     randomEmoteBuilder.permission("emotes.give-random");
/*     */     
/* 294 */     CommandSpec.Builder giveEmoteBuilder = CommandSpec.builder();
/* 295 */     giveEmoteBuilder.executor(new GiveEmoteCommand(this.config
/* 296 */           .giveEmotePermissionCmd(), this.config
/* 297 */           .giveEmotePermissionMsg()));
/*     */     
/* 299 */     giveEmoteBuilder.arguments(/* 300 */           GenericArguments.player(Text.of("player")), new EmoteArgument("emote", this.emoteStorage)
            /*     */);
/*     */     
/* 303 */     giveEmoteBuilder.permission("emotes.give");
/*     */     
/* 305 */     cmdService.register(this, emotesCmdBuilder.build(), ListEmotesCommand.keys);
/* 306 */     cmdService.register(this, randomEmoteBuilder.build(), GiveRandomEmoteCommand.keys);
/* 307 */     cmdService.register(this, giveEmoteBuilder.build(), GiveEmoteCommand.keys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Listener
/*     */   public void onServerStop(GameStoppingEvent event) {
/* 318 */     this.logger.trace("Stop");
/*     */   }
/*     */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */