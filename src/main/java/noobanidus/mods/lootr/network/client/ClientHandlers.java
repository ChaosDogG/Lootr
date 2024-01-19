package noobanidus.mods.lootr.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;


public class ClientHandlers {
  public static void handleCloseCart(int entityId) {
    Level level = Minecraft.getInstance().level;
    if (level == null) {
      LootrAPI.LOG.info("Unable to mark entity with id '" + entityId + "' as closed as level is null.");
      return;
    }
    Entity cart = level.getEntity(entityId);
    if (cart == null) {
      LootrAPI.LOG.info("Unable to mark entity with id '" + entityId + "' as closed as entity is null.");
      return;
    }

    if (!(cart instanceof LootrChestMinecartEntity lootrCart)) {
      LootrAPI.LOG.info("Unable to mark entity with id '" + entityId + "' as closed as entity is not a Lootr minecart.");
      return;
    }

    lootrCart.setClosed();
  }

  public static void handleOpenCart(int entityId) {
    Level level = Minecraft.getInstance().level;
    if (level == null) {
      LootrAPI.LOG.info("Unable to mark entity with id '" + entityId + "' as opened as level is null.");
      return;
    }
    Entity cart = level.getEntity(entityId);
    if (cart == null) {
      LootrAPI.LOG.info("Unable to mark entity with id '" + entityId + "' as opened as entity is null.");
      return;
    }

    if (!(cart instanceof LootrChestMinecartEntity lootrCart)) {
      LootrAPI.LOG.info("Unable to mark entity with id '" + entityId + "' as opened as entity is not a Lootr minecart.");
      return;
    }

    lootrCart.setOpened();
  }

  public static void handleOpenContainer(BlockPos pos) {
    Level level = Minecraft.getInstance().level;
    if (level == null) {
      LootrAPI.LOG.info("Unable to mark container open for location '" + pos + "' as level is null.");
      return;
    }

    Player player = Minecraft.getInstance().player;
    if (player == null) {
      LootrAPI.LOG.info("Unable to mark container open for location '" + pos + "' as player is null.");
      return;
    }

    BlockEntity be = level.getBlockEntity(pos);
    if (be instanceof ILootBlockEntity tile) {
      tile.getOpeners().remove(player.getUUID());
      be.setChanged();
      level.getModelDataManager().requestRefresh(be);
    } else {
      LootrAPI.LOG.info("Unable to mark container open for location '" + pos + "' as block entity is null or not a Lootr block entity.");
    }
  }

  public static void handleCloseContainer(BlockPos pos) {
    Level level = Minecraft.getInstance().level;
    if (level == null) {
      LootrAPI.LOG.info("Unable to mark container closed for location '" + pos + "' as level is null.");
      return;
    }

    Player player = Minecraft.getInstance().player;
    if (player == null) {
      LootrAPI.LOG.info("Unable to mark container closed for location '" + pos + "' as player is null.");
      return;
    }

    BlockEntity be = level.getBlockEntity(pos);
    if (be instanceof ILootBlockEntity tile) {
      tile.getOpeners().add(player.getUUID());
      be.setChanged();
      level.getModelDataManager().requestRefresh(be);
    } else {
      LootrAPI.LOG.info("Unable to mark container closed for location '" + pos + "' as block entity is null or not a Lootr block entity.");
    }
  }
}
