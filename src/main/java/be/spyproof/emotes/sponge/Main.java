 package be.spyproof.emotes.sponge;


 import be.spyproof.emotes.check.EmoteNameCheck;
 import be.spyproof.emotes.check.EmoteStringKeyCheck;
 import be.spyproof.emotes.da.CachedEmoteStorage;
 import be.spyproof.emotes.da.MemoryStorage;
 import be.spyproof.emotes.da.MySqlEmoteStorage;
 import be.spyproof.emotes.event.EmoteEventBus;
 import be.spyproof.emotes.event.EmoteUpdateEvent;
 import be.spyproof.emotes.model.Emote;
 import be.spyproof.emotes.sponge.channel.EmotesChannel;
 import be.spyproof.emotes.sponge.commands.*;
 import be.spyproof.emotes.sponge.controller.ConfigController;
 import be.spyproof.emotes.sponge.controller.INameTransformer;
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
 import org.spongepowered.api.plugin.Dependency;
 import org.spongepowered.api.plugin.Plugin;
 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.format.TextColors;

 import java.io.File;
 import java.io.IOException;

// import be.spyproof.emotes.sponge.controller.NameTransformer;




























































 @Plugin(id = "emotes-sponge", name = "Emotes", version = "1.1.1", description = "Emotes implementation for sponge", authors = {"TPNils", "Pokerman981 (Troy G)"}, dependencies = {
         @Dependency(id = "nickname_manager", optional = false)
 })
 public class Main
 {
   @Inject
   private Logger logger;
   @Inject
   @DefaultConfig(sharedRoot = true)
   private File configDir;
   private MemoryStorage<Emote, String> emoteStorage;
   private EmotesChannel emoteMessageChannel;
   private ConfigController config;
            public static Main instance;
   
   @Listener
   public void onPreInit(GamePreInitializationEvent event) throws IOException {
     this.logger.trace("PreInit");

              instance = this;

     this.config = new ConfigController(this.configDir);
     this.config.load();
     
     if (MySqlEmoteStorage.canConnect(this.config.getMySQLHost(), this.config.getMySQLPort(), this.config.getMySQLDb(), this.config.getMySQLUser(), this.config.getMySQLPass())) {
       this.emoteStorage = new CachedEmoteStorage<>(EmoteNameCheck.get(), EmoteStringKeyCheck.get(), new MySqlEmoteStorage(this.config.getMySQLHost(), this.config.getMySQLPort(), this.config.getMySQLDb(), this.config.getMySQLUser(), this.config.getMySQLPass()));
     } else {
       this.emoteStorage = new MemoryStorage<>(EmoteNameCheck.get(), EmoteStringKeyCheck.get());
     } 
 
     
     EmoteEventBus.addListener(e -> {
           if (EmoteUpdateEvent.Action.SAVE.equals(e.getAction())) {
             this.emoteStorage.save(e.getEmote());
           } else if (EmoteUpdateEvent.Action.DELETE.equals(e.getAction())) {
             this.emoteStorage.remove(e.getEmote());
           } 
         });

     this.emoteMessageChannel = new EmotesChannel();

     if (this.emoteStorage.getAll().size() == 0) {
       
       this.emoteStorage.save(ConversionUtils.createEmote("cutie", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is a cutie ", TextColors.RESET, "◕ ◡ ◕"),
               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " thinks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is a cutie", TextColors.RESET, " ◕ ◡ ◕")));
       this.emoteStorage.save(ConversionUtils.createEmote("dealwithit", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GRAY, " ╰▄︻▄╯", TextColors.DARK_GREEN, " deal with it"),
               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GRAY, " ╰▄︻▄╯", TextColors.DARK_GREEN, " deal with it ", TextColors.RESET, "${receiver}")));
       this.emoteStorage.save(ConversionUtils.createEmote("derp", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " are a herp a derp ◖|◔◡◉|◗"),
               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " thinks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " are a herp a derp ◖|◔◡◉|◗")));
       this.emoteStorage.save(ConversionUtils.createEmote("facedesk", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " slammed a desk on its face *"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " slammed a desk on ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "'s face *")));
       this.emoteStorage.save(ConversionUtils.createEmote("facepalm", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " facepalmed *") ,
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " facepalmed for ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " *")));
       this.emoteStorage.save(ConversionUtils.createEmote("gasp", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gasps * ⊙▃⊙"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gasps at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ⊙▃⊙")));
       this.emoteStorage.save(ConversionUtils.createEmote("hugs", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " hugs everyone * (づ｡◕‿‿◕｡)づ"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " hugs ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * (づ｡◕‿‿◕｡)づ")));
       this.emoteStorage.save(ConversionUtils.createEmote("omg", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " Oh... My... God... ಠ_ಠ"),
               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " Oh... My... God... Why ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "? ಠ_ಠ")));
       this.emoteStorage.save(ConversionUtils.createEmote("rage", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rages out! * (╯°□°）╯︵ ┻━┻"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rages out at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "! * (╯°□°）╯︵ ┻━┻  ╰(”◕﹏◕”)つ")));
       this.emoteStorage.save(ConversionUtils.createEmote("runs", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " runs away * ᕕ(◉Д◉ )ᕗ"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " runs away from ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ᕕ(◉Д◉ )ᕗ")));
       this.emoteStorage.save(ConversionUtils.createEmote("brb", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " will be right back *"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${receiver}", TextColors.DARK_GREEN, " o/ ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " will be right back ")));
       this.emoteStorage.save(ConversionUtils.createEmote("fart", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " farted *"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " farted in ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "'s face * ")));
       this.emoteStorage.save(ConversionUtils.createEmote("star", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gives golden star to self * ", TextColors.GOLD, "★"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " gives ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " a golden star * ",
                   TextColors.GOLD, "★")));
                this.emoteStorage.save(ConversionUtils.createEmote("inlove", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is in love ", TextColors.LIGHT_PURPLE, "♥.♥"),
               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is in love with ", TextColors.RESET, "${receiver} ", TextColors.LIGHT_PURPLE, "♥.♥")));
       this.emoteStorage.save(ConversionUtils.createEmote("sleepy", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " feels sleepy right now Zzzz... ≈ ___≈"),
               Text.of(TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " makes ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " very sleepy Zzzz... ≈ ___≈")));
       this.emoteStorage.save(ConversionUtils.createEmote("bored", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels bored * SIGH!"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " SIGHS * ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is sooooo boring!")));
       this.emoteStorage.save(ConversionUtils.createEmote("crazy", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " is going crazzzyyyy!?! 0___0"),
               Text.of(TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is making ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " crazy! 0___0")));
       this.emoteStorage.save(ConversionUtils.createEmote("book", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " reads a book *"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " stops reading a book and throws it at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " *")));
       this.emoteStorage.save(ConversionUtils.createEmote("cry", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " cries * ;-;"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${receiver}", TextColors.DARK_GREEN, " makes ", TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " cry * ;-;")));
       this.emoteStorage.save(ConversionUtils.createEmote("stare", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " stares into the distance * >.>"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " stares at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " awkwardly * <.<")));
       this.emoteStorage.save(ConversionUtils.createEmote("notime", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " ain't gat no time for that! \\>.</"),
               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " gat no time for ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "! \\>.</")));
       this.emoteStorage.save(ConversionUtils.createEmote("goofball", 
             Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " feels like a goofball v_v"),
               Text.of(TextColors.RESET, "${sender}", TextColors.DARK_GREEN, " thinks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " is a goofball v.v")));
       this.emoteStorage.save(ConversionUtils.createEmote("rofl", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rolls over the floor, laughing * "),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " rolls over the floor, laughing at ", TextColors.RESET, "${receiver}")));
       this.emoteStorage.save(ConversionUtils.createEmote("daydream", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " starts daydreaming * ◔.◔"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " starts daydreaming about ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ◔.◔")));
       this.emoteStorage.save(ConversionUtils.createEmote("sorry", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels sorry * D;"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels sorry for ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * D;")));
       this.emoteStorage.save(ConversionUtils.createEmote("bite", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " bites self * 0w0"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " nibbles on ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ^w^")));
       this.emoteStorage.save(ConversionUtils.createEmote("scared", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " feels scared * 【•】_【•】"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " was scared by ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * 【•】_【•】")));
       this.emoteStorage.save(ConversionUtils.createEmote("poke", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " pokes everyone * ☚"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " pokes ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ☚")));
       this.emoteStorage.save(ConversionUtils.createEmote("wave", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " waves * o/"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " waves at ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * o/")));
       this.emoteStorage.save(ConversionUtils.createEmote("cookie", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " eats a cookie * ^_^"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " shares a cookie with ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, " * ^_^")));
       this.emoteStorage.save(ConversionUtils.createEmote("lick", 
             Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " licks * ^_^"),
               Text.of(TextColors.DARK_GREEN, "*", TextColors.RESET, " ${sender}", TextColors.DARK_GREEN, " licks ", TextColors.RESET, "${receiver}", TextColors.DARK_GREEN, "'s face * ")));
     } 
   }
 
 
 
 
 
 
   
   @Listener
   public void onInit(GameInitializationEvent event) {
     this.logger.trace("Init");
     if (this.emoteStorage.getClass() == MemoryStorage.class) {
       Sponge.getEventManager().registerListener(this, ClientConnectionEvent.Join.class, new InvalidStorageNotifier());
     }
     
     try {
       Class.forName("br.net.fabiozumbi12.UltimateChat.Sponge.UChat");
       this.emoteMessageChannel.addListener(new UChatListener());
     } catch (ClassNotFoundException classNotFoundException) {}
   }
 
 
 
 
 
 
 
   
   @Listener
   public void onPostInit(GamePostInitializationEvent event) {
     this.logger.trace("PostInit"); //Not sure if this works at all but what was decompiled surely didn't work either
       // RegisterService.register(new PermissionService((perm, uuid) -> Sponge.getServer().getPlayer(uuid).map), Boolean.FALSE));
   }
 
 
 
 
 
 
   
   @Listener
   public void onServerStart(GameStartingServerEvent event) throws ClassNotFoundException {
     Class.forName("be.spyproof.nickmanager.controller.ISpongeNicknameController");
     ISpongeNicknameController nicknameController = Sponge.getServiceManager().provide(ISpongeNicknameController.class).get();

     NicknameManagerTransformer nicknameManagerTransformer =  new NicknameManagerTransformer(nicknameController);
     // NameTransformer nameTransformer = new NameTransformer();
     this.logger.trace("Start");
 
     
     CommandManager cmdService = Sponge.getCommandManager();
     INameTransformer transformer = nicknameManagerTransformer;
 
     
//     try {
//       Class.forName("be.spyproof.nickmanager.controller.ISpongeNicknameController");
//       Optional<ISpongeNicknameController> nicknameController = Sponge.getServiceManager().provide(ISpongeNicknameController.class);
//
//       if (nicknameController.isPresent())
//         nicknameManagerTransformer = new NicknameManagerTransformer(nicknameController.get());
//     } catch (ClassNotFoundException classNotFoundException) {}
 
     
//     if (nicknameManagerTransformer == null) {
//       nameTransformer = new NameTransformer();
//     }

     CommandSpec.Builder emotesCmdBuilder = CommandSpec.builder();
     emotesCmdBuilder.executor(new ListEmotesCommand(this.emoteStorage, nicknameManagerTransformer, this.emoteMessageChannel, this.config.noPermissionMsg()));
     emotesCmdBuilder.arguments(           GenericArguments.optional(new EmoteArgument("emote", this.emoteStorage)),
                       GenericArguments.optional(new RemainingArgsOrPlayerArgument(Text.of("receiver")))
            );
     
     CommandSpec.Builder randomEmoteBuilder = CommandSpec.builder();
     randomEmoteBuilder.executor(new GiveRandomEmoteCommand(this.emoteStorage, this.config
           
           .giveEmotePermissionCmd(), this.config
           .backupCmd(), this.config
           .giveEmotePermissionMsg(), this.config
           .backupMsg()));
     
     randomEmoteBuilder.arguments(GenericArguments.player(Text.of("player")));
     randomEmoteBuilder.permission("emotes.give-random");
     
     CommandSpec.Builder giveEmoteBuilder = CommandSpec.builder();
     giveEmoteBuilder.executor(new GiveEmoteCommand(this.config
           .giveEmotePermissionCmd(), this.config
           .giveEmotePermissionMsg()));
     
     giveEmoteBuilder.arguments(           GenericArguments.player(Text.of("player")), new EmoteArgument("emote", this.emoteStorage)
            );
     
     giveEmoteBuilder.permission("emotes.give");
     
     cmdService.register(this, emotesCmdBuilder.build(), ListEmotesCommand.keys);
     cmdService.register(this, randomEmoteBuilder.build(), GiveRandomEmoteCommand.keys);
     cmdService.register(this, giveEmoteBuilder.build(), GiveEmoteCommand.keys);
   }
 
 
 
 
 
 
   
   @Listener
   public void onServerStop(GameStoppingEvent event) {
     this.logger.trace("Stop");
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */