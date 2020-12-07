
package be.spyproof.emotes.sponge.commands;


import be.spyproof.emotes.da.IStorage;
import be.spyproof.emotes.model.Emote;
import be.spyproof.emotes.sponge.Main;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class GiveRandomEmoteCommand implements CommandExecutor {
    public static final String[] keys = new String[]{"give-random-emote", "gre"};
    private final Random random = new Random(System.currentTimeMillis());

    private final IStorage<Emote, ?> storage;
    private final String givePermissionCommand;
    private final String backupCommand;
    private final String givePermissionMessage;
    private final String backupMessage;


    public GiveRandomEmoteCommand(IStorage<Emote, ?> storage, String givePermissionCommand, String backupCommand, String givePermissionMessage, String backupMessage) {
        this.storage = storage;
        this.givePermissionCommand = givePermissionCommand.replaceFirst("^/", "");
        this.backupCommand = backupCommand.replaceFirst("^/", "");
        this.givePermissionMessage = (givePermissionMessage == null) ? "" : givePermissionMessage;
        this.backupMessage = (backupMessage == null) ? "" : backupMessage;

    }


    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<Player> optional = args.getOne("player");
        Player player = optional.get();
        List<Emote> emotes = new ArrayList<>(this.storage.getAll());

        if (emotes.size() == 0) {
            System.out.println("no emotes loaded, WTF???");

            Sponge.getCommandManager().process(
                    Sponge.getServer().getConsole(), this.backupCommand
                            .replace("${user}", player.getName()).replaceFirst("^/", ""));


            if (!this.backupMessage.isEmpty()) {
                player.sendMessage(TextSerializers.FORMATTING_CODE
                        .deserialize(this.backupMessage
                                .replace("${user}", player.getName())));
            }

            return CommandResult.empty();
        }

        Task.builder().delayTicks(7).execute(() -> {
            List<Emote> emotesNew = new ArrayList<>(this.storage.getAll());
            MessageChannel.TO_CONSOLE.send(Text.of(emotesNew.size()));
            emotesNew.removeIf(e -> player.hasPermission("emotes.command.${emote}".replace("${emote}", e.getName())));
            MessageChannel.TO_CONSOLE.send(Text.of(emotesNew.size()));
            emotesNew.removeIf(e -> player.hasPermission("emotes.command.${emote}".replace("${emote}", e.getName())));
            MessageChannel.TO_CONSOLE.send(Text.of(emotesNew.size()));

            if (emotesNew.size() == 0) {
                Sponge.getCommandManager().process(
                        Sponge.getServer().getConsole(), this.backupCommand
                                .replace("${user}", player.getName()).replaceFirst("^/", ""));


                if (!this.backupMessage.isEmpty()) {
                    player.sendMessage(TextSerializers.FORMATTING_CODE
                            .deserialize(this.backupMessage
                                    .replace("${user}", player.getName())));
                }

                return;
            }

            Emote emote = emotesNew.get(this.random.nextInt(emotesNew.size()));

            Sponge.getCommandManager().process(
                        Sponge.getServer().getConsole(), this.givePermissionCommand
                            .replace("${user}", player.getName())
                            .replace("${perm}", "emotes.command.${emote}".replace("${emote}", emote.getName()))
                            .replaceFirst("^/", ""));

            if (!this.givePermissionMessage.isEmpty()) {
                player.sendMessage(TextSerializers.FORMATTING_CODE
                        .deserialize(this.givePermissionMessage

                                .replace("${user}", player.getName())
                                .replace("${emote}", emote.getName())));
            }
        }).submit(Main.instance);




        return CommandResult.empty();
    }

}


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\commands\GiveRandomEmoteCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */