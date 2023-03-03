package noobanidus.mods.lootr;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;
import noobanidus.mods.lootr.api.IServerAccess;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.command.CommandLootr;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.init.ModAdvancements;
import noobanidus.mods.lootr.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

@Mod("lootr")
public class Lootr {

  public static IServerAccess serverAccess = new IServerAccess() {
    @Override
    public @Nullable MinecraftServer getServer() {
      return ServerLifecycleHooks.getCurrentServer();
    }
  };

  static {
    ModAdvancements.load();
  }

  public CommandLootr COMMAND_LOOTR;
  public static CreativeModeTab TAB = new CreativeModeTab(LootrAPI.MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModBlocks.CHEST);
    }
  };

  public Lootr() {
    ModLoadingContext context = ModLoadingContext.get();
    context.registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    context.registerConfig(ModConfig.Type.CLIENT, ConfigManager.CLIENT_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(LootrAPI.MODID + "-common.toml"));
    ConfigManager.loadConfig(ConfigManager.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(LootrAPI.MODID + "-client.toml"));
    MinecraftForge.EVENT_BUS.addListener(this::onCommands);
  }

  public void onCommands(RegisterCommandsEvent event) {
    COMMAND_LOOTR = new CommandLootr(event.getDispatcher());
    COMMAND_LOOTR.register();
  }
}
