 package be.spyproof.emotes.sponge.listener;

 import org.spongepowered.api.Sponge;
 import org.spongepowered.api.event.EventListener;
 import org.spongepowered.api.event.network.ClientConnectionEvent;
 import org.spongepowered.api.service.ProviderRegistration;
 import org.spongepowered.api.service.whitelist.WhitelistService;
 import org.spongepowered.api.text.Text;
 import org.spongepowered.api.text.format.TextColors;
 import org.spongepowered.api.text.format.TextStyles;

 import java.util.Optional;
 
 
 public class InvalidStorageNotifier
   implements EventListener<ClientConnectionEvent.Join>
 {
   public void handle(ClientConnectionEvent.Join event) throws Exception {
     Optional<ProviderRegistration<WhitelistService>> whitelistService = Sponge.getServiceManager().getRegistration(WhitelistService.class);
     whitelistService.ifPresent(service -> {
           if (((WhitelistService)service.getProvider()).isWhitelisted(event.getTargetEntity().getProfile()))
             event.getTargetEntity().sendMessage(Text.of(new Object[] { TextColors.RED, TextStyles.BOLD, "The emote plugin failed to connect to the MySQL server. Fix this issue so new emotes can be added!" })); 
         });
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\listener\InvalidStorageNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */