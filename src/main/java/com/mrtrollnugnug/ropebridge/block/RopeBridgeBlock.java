package com.mrtrollnugnug.ropebridge.block;

import com.mrtrollnugnug.ropebridge.handler.ConfigHandler;
import com.mrtrollnugnug.ropebridge.handler.ContentHandler;
import com.mrtrollnugnug.ropebridge.lib.Constants.Messages;
import com.mrtrollnugnug.ropebridge.lib.ModUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class RopeBridgeBlock extends Block {

	public RopeBridgeBlock(Properties properties) {
		super(properties);
	}

	public static final IntegerProperty PROPERTY_HEIGHT = IntegerProperty.create("level", 0, 3);
	public static final IntegerProperty PROPERTY_BACK = IntegerProperty.create("back", 0, 3);
	public static final BooleanProperty ROTATED = BooleanProperty.create("rotated");

	public static final VoxelShape ZERO_AABB = Block.box(0, 0, 0, 16, 4, 16);
	public static final VoxelShape ONE_AABB = Block.box(0, 4, 0, 16, 8, 16);
	public static final VoxelShape TWO_AABB = Block.box(0, 8, 0, 16, 12, 16);
	public static final VoxelShape THREE_AABB = Block.box(0, 12, 0, 16, 16, 16);

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(PROPERTY_HEIGHT, PROPERTY_BACK, ROTATED);
	}

	private Block slab;

	public Block getSlab() {
		return slab;
	}

	public void setSlab(Block slab) {
		this.slab = slab;
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		int level = state.getValue(PROPERTY_HEIGHT);
		switch (level) {
			case 1:
				return ONE_AABB;
			case 2:
				return TWO_AABB;
			case 3:
				return THREE_AABB;
			case 0:
			default:
				return ZERO_AABB;
		}
	}

	@Override
	public void playerWillDestroy(Level world, final BlockPos pos, BlockState state, Player player) {
		super.playerWillDestroy(world, pos, state, player);
		if (!world.isClientSide && player.getMainHandItem().getItem() == ContentHandler.bridge_builder.get() && player.isCrouching()) {
			ModUtils.tellPlayer(player, Messages.WARNING_BREAKING);
			boolean rotate = world.getBlockState(pos).getValue(RopeBridgeBlock.ROTATED);
			if (rotate) {
				breakNorth(pos, (ServerLevel) world);
				breakSouth(pos, (ServerLevel) world);
			} else {
				breakEast(pos, (ServerLevel) world);
				breakWest(pos, (ServerLevel) world);
			}
		}
	}

	public void breakSouth(BlockPos posToBreak, ServerLevel world) {
		BlockPos south = posToBreak.south();
		BlockPos up = south.above();
		BlockPos down = south.below();
		BlockState stateDown = world.getBlockState(down);
		BlockState stateUp = world.getBlockState(up);
		BlockState state = world.getBlockState(south);
		if (state.getBlock() == this) {
			world.destroyBlock(south, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakSouth(south, world);
				}
			}, 100);
		}
		if (stateUp.getBlock() == this) {
			world.destroyBlock(up, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakSouth(up, world);
				}
			}, 100);
		}
		if (stateDown.getBlock() == this) {
			world.destroyBlock(down, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakSouth(down, world);
				}
			}, 100);
		}
	}

	public void breakNorth(BlockPos posToBreak, ServerLevel world) {
		BlockPos north = posToBreak.north();
		BlockPos up = north.above();
		BlockPos down = north.below();
		BlockState stateDown = world.getBlockState(down);
		BlockState stateUp = world.getBlockState(up);
		BlockState state = world.getBlockState(north);
		if (state.getBlock() == this) {
			world.destroyBlock(north, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakNorth(north, world);
				}
			}, 100);
		}
		if (stateUp.getBlock() == this) {
			world.destroyBlock(up, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakNorth(up, world);
				}
			}, 100);
		}
		if (stateDown.getBlock() == this) {
			world.destroyBlock(down, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakNorth(down, world);
				}
			}, 100);
		}
	}

	public void breakEast(BlockPos posToBreak, ServerLevel world) {
		BlockPos east = posToBreak.east();
		BlockPos up = east.above();
		BlockPos down = east.below();
		BlockState stateDown = world.getBlockState(down);
		BlockState stateUp = world.getBlockState(up);
		BlockState state = world.getBlockState(east);
		if (state.getBlock() == this) {
			world.destroyBlock(east, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakEast(east, world);
				}
			}, 100);
		}
		if (stateUp.getBlock() == this) {
			world.destroyBlock(up, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakEast(up, world);
				}
			}, 100);
		}
		if (stateDown.getBlock() == this) {
			world.destroyBlock(down, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakEast(down, world);
				}
			}, 100);
		}
	}

	public void breakWest(BlockPos posToBreak, ServerLevel world) {
		BlockPos west = posToBreak.west();
		BlockPos up = west.above();
		BlockPos down = west.below();
		BlockState stateDown = world.getBlockState(down);
		BlockState stateUp = world.getBlockState(up);
		BlockState state = world.getBlockState(west);
		if (state.getBlock() == this) {
			world.destroyBlock(west, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakWest(west, world);
				}
			}, 100);
		}
		if (stateUp.getBlock() == this) {
			world.destroyBlock(up, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakWest(up, world);
				}
			}, 100);
		}
		if (stateDown.getBlock() == this) {
			world.destroyBlock(down, true);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					breakWest(down, world);
				}
			}, 100);
		}
	}

	//убрал, так как теперь нужно переходить на datapack с loottable, но можно и так. Проблема , что этот метод лезет в конфиг мода, который не загружен почемуто.
	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drops = new ArrayList<>();
		drops.add(new ItemStack(ContentHandler.rope.get(), ConfigHandler.getRopePerBridge()));
		drops.add(new ItemStack(slab, ConfigHandler.getSlabsPerBridge()));
		return drops;
	}
}
