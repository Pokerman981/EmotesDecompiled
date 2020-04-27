/*    */ package be.spyproof.emotes.sponge.controller;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import ninja.leaping.configurate.ConfigurationNode;
/*    */ import ninja.leaping.configurate.ConfigurationOptions;
/*    */ import ninja.leaping.configurate.commented.CommentedConfigurationNode;
/*    */ import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
/*    */ import ninja.leaping.configurate.loader.ConfigurationLoader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigController
/*    */ {
/*    */   private File file;
/*    */   private ConfigurationLoader<CommentedConfigurationNode> loader;
/*    */   private CommentedConfigurationNode root;
/*    */   
/*    */   public ConfigController(File file) {
/* 24 */     this.file = file;
/*    */   }
/*    */ 
/*    */   
/*    */   public void load() throws IOException {
/* 29 */     if (!this.file.exists()) {
/* 30 */       this.file.createNewFile();
/*    */     }
/* 32 */     this
/*    */ 
/*    */ 
/*    */       
/* 36 */       .loader = (ConfigurationLoader<CommentedConfigurationNode>)((HoconConfigurationLoader.Builder)((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setDefaultOptions(ConfigurationOptions.defaults())).setFile(this.file)).build();
/* 37 */     this.root = (CommentedConfigurationNode)this.loader.load();
/*    */ 
/*    */     
/* 40 */     ConfigurationNode defaults = ((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setURL(getClass().getResource("/default.conf"))).build().load();
/* 41 */     if (defaults.getNode(new Object[] { "config version" }).getInt() != this.root.getNode(new Object[] { "config version" }).getInt()) {
/*    */       
/* 43 */       this.root.getNode(new Object[] { "config version" }).setValue(Integer.valueOf(defaults.getNode(new Object[] { "config version" }).getInt()));
/* 44 */       this.root.mergeValuesFrom(defaults);
/* 45 */       this.loader.save((ConfigurationNode)this.root);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMySQLHost() {
/* 51 */     return this.root.getNode(new Object[] { "MySQL", "Host" }).getString("127.0.0.1");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMySQLDb() {
/* 56 */     return this.root.getNode(new Object[] { "MySQL", "Database" }).getString("emotes");
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMySQLPort() {
/* 61 */     return this.root.getNode(new Object[] { "MySQL", "Port" }).getInt(3306);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMySQLUser() {
/* 66 */     return this.root.getNode(new Object[] { "MySQL", "User" }).getString("Derp");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMySQLPass() {
/* 71 */     return this.root.getNode(new Object[] { "MySQL", "Password" }).getString("MyUberPassWord");
/*    */   }
/*    */ 
/*    */   
/*    */   public String giveEmotePermissionCmd() {
/* 76 */     return this.root.getNode(new Object[] { "Command", "Give emote permission" }).getString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String backupCmd() {
/* 81 */     return this.root.getNode(new Object[] { "Command", "Backup" }).getString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String noPermissionMsg() {
/* 86 */     return this.root.getNode(new Object[] { "Command", "No permission" }).getString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String giveEmotePermissionMsg() {
/* 91 */     return this.root.getNode(new Object[] { "Message", "Give emote permission" }).getString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String backupMsg() {
/* 96 */     return this.root.getNode(new Object[] { "Message", "Backup command" }).getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\TroyG\Desktop\Emotes-server-sponge-1.12.2-1.1.1.jar!\be\spyproof\emotes\sponge\controller\ConfigController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */