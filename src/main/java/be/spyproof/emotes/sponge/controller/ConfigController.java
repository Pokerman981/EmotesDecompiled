 package be.spyproof.emotes.sponge.controller;

 import ninja.leaping.configurate.ConfigurationNode;
 import ninja.leaping.configurate.ConfigurationOptions;
 import ninja.leaping.configurate.commented.CommentedConfigurationNode;
 import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
 import ninja.leaping.configurate.loader.ConfigurationLoader;

 import java.io.File;
 import java.io.IOException;
 
 
 
 
 
 
 
 public class ConfigController
 {
   private File file;
   private ConfigurationLoader<CommentedConfigurationNode> loader;
   private CommentedConfigurationNode root;
   
   public ConfigController(File file) {
     this.file = file;
   }
 
   
   public void load() throws IOException {
     if (!this.file.exists()) {
       this.file.createNewFile();
     }
     this
 
 
       
       .loader = (ConfigurationLoader<CommentedConfigurationNode>)((HoconConfigurationLoader.Builder)((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setDefaultOptions(ConfigurationOptions.defaults())).setFile(this.file)).build();
     this.root = (CommentedConfigurationNode)this.loader.load();
 
     
     ConfigurationNode defaults = ((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setURL(getClass().getResource("/default.conf"))).build().load();
     if (defaults.getNode(new Object[] { "config version" }).getInt() != this.root.getNode(new Object[] { "config version" }).getInt()) {
       
       this.root.getNode(new Object[] { "config version" }).setValue(Integer.valueOf(defaults.getNode(new Object[] { "config version" }).getInt()));
       this.root.mergeValuesFrom(defaults);
       this.loader.save((ConfigurationNode)this.root);
     } 
   }
 
   
   public String getMySQLHost() {
     return this.root.getNode(new Object[] { "MySQL", "Host" }).getString("127.0.0.1");
   }
 
   
   public String getMySQLDb() {
     return this.root.getNode(new Object[] { "MySQL", "Database" }).getString("emotes");
   }
 
   
   public int getMySQLPort() {
     return this.root.getNode(new Object[] { "MySQL", "Port" }).getInt(3306);
   }
 
   
   public String getMySQLUser() {
     return this.root.getNode(new Object[] { "MySQL", "User" }).getString("Derp");
   }
 
   
   public String getMySQLPass() {
     return this.root.getNode(new Object[] { "MySQL", "Password" }).getString("MyUberPassWord");
   }
 
   
   public String giveEmotePermissionCmd() {
     return this.root.getNode(new Object[] { "Command", "Give emote permission" }).getString();
   }
 
   
   public String backupCmd() {
     return this.root.getNode(new Object[] { "Command", "Backup" }).getString();
   }
 
   
   public String noPermissionMsg() {
     return this.root.getNode(new Object[] { "Command", "No permission" }).getString();
   }
 
   
   public String giveEmotePermissionMsg() {
     return this.root.getNode(new Object[] { "Message", "Give emote permission" }).getString();
   }
 
   
   public String backupMsg() {
     return this.root.getNode(new Object[] { "Message", "Backup command" }).getString();
   }
 }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\controller\ConfigController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */