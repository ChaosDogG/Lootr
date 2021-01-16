package noobanidus.mods.lootr.world.processor;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.util.Constants;
import noobanidus.mods.lootr.init.ModBlocks;
import noobanidus.mods.lootr.init.ModMisc;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NullableProblems")
public class LootrChestProcessor extends StructureProcessor {
  public static final LootrChestProcessor INSTANCE = new LootrChestProcessor();
  public static final Codec<LootrChestProcessor> CODEC = Codec.unit(() -> INSTANCE);

  @Override
  protected IStructureProcessorType<?> getType() {
    return ModMisc.LOOTR_PROCESSOR;
  }

  private static Map<Block, Block> replacements = null;

  @Nullable
  public Template.BlockInfo process(IWorldReader world, BlockPos pos, BlockPos blockPos, Template.BlockInfo info1, Template.BlockInfo info2, PlacementSettings placement, @Nullable Template template) {
    if (replacements == null) {
      replacements = new HashMap<>();
      replacements.put(Blocks.CHEST, ModBlocks.CHEST);
      replacements.put(Blocks.BARREL, ModBlocks.BARREL);
      replacements.put(Blocks.TRAPPED_CHEST, ModBlocks.TRAPPED_CHEST);
    }

    BlockState state = info2.state;
    Block replacement = replacements.get(state.getBlock());
    if (replacement == null || info2.nbt == null) {
      return info2;
    }

    if (!info2.nbt.contains("LootTable", Constants.NBT.TAG_STRING)) {
      return info2;
    }

    BlockState newState = replacement.getDefaultState();
    if (replacement == ModBlocks.CHEST || replacement == ModBlocks.TRAPPED_CHEST) {
      newState = newState.with(ChestBlock.FACING, state.get(ChestBlock.FACING)).with(ChestBlock.WATERLOGGED, state.get(ChestBlock.WATERLOGGED));
    } else if (replacement == ModBlocks.BARREL) {
      newState = newState.with(BarrelBlock.PROPERTY_OPEN, state.get(BarrelBlock.PROPERTY_OPEN)).with(BarrelBlock.PROPERTY_FACING, state.get(BarrelBlock.PROPERTY_FACING));
    }
    return new Template.BlockInfo(info2.pos, newState, info2.nbt);
  }
}
