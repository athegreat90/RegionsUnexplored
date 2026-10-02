package net.regions_unexplored.block.type.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.state.BlockState;
import net.regions_unexplored.block.RUBlockUtils;

import java.util.function.Supplier;

public class RUGrowingPlantBodyBlock extends GrowingPlantBodyBlock {
	
	protected final ResourceKey<Block> headBlock;
	protected final float width;
	
	public RUGrowingPlantBodyBlock(ResourceKey<Block> headBlock, float width, Properties properties) {
		super(properties, Direction.DOWN, RUBlockUtils.column(width, 0, 16), false);
		this.headBlock = headBlock;
		this.width = width;
	}
	
	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return (GrowingPlantHeadBlock) BuiltInRegistries.BLOCK.getValue(this.headBlock);
	}
}
