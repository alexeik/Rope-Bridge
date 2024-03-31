package com.mrtrollnugnug.ropebridge.handler;

import com.mrtrollnugnug.ropebridge.lib.BlockItemUseContextExt;
import com.mrtrollnugnug.ropebridge.lib.ModUtils;
import com.mrtrollnugnug.ropebridge.lib.Constants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;


import java.util.Timer;
import java.util.TimerTask;

public class LadderBuildingHandler {
	public static void newLadder(BlockPos selected, Player player, Level world, Direction hitSide,
								 ItemStack builder) {
		if (!hitSide.getAxis().isHorizontal()) {
			ModUtils.tellPlayer(player, Constants.Messages.BAD_SIDE,
				hitSide == Direction.UP ? I18n.get(Constants.Messages.TOP) : I18n.get(Constants.Messages.BOTTOM));
			return;
		}

		BlockState ladderState = ContentHandler.oak_rope_ladder.get().defaultBlockState().setValue(LadderBlock.FACING, hitSide);

		if (!ladderState.canSurvive(world, selected)) {
			ModUtils.tellPlayer(player, Constants.Messages.NOT_SOLID);
			return;
		}

		int count = countBlocks(selected.relative(hitSide), world);

		if (count <= 0) {
			ModUtils.tellPlayer(player, Constants.Messages.OBSTRUCTED);
			return;
		}

		int woodNeeded = count * ConfigHandler.getWoodPerLadder();
		int ropeNeeded = count * ConfigHandler.getRopePerLadder();
		Block slabToUse = getSlabToUse(player);

		if (!player.getAbilities().instabuild) {
			if (!hasMaterials(player, woodNeeded, ropeNeeded, slabToUse)) {
				ModUtils.tellPlayer(player, Constants.Messages.UNDERFUNDED_LADDER, woodNeeded, ropeNeeded);
				return;
			}
		}

		if (!player.getAbilities().instabuild)
			builder.hurtAndBreak(ConfigHandler.getLadderDamage(), player, playerEntity -> playerEntity.broadcastBreakEvent(player.getUsedItemHand()));

		consume(player, woodNeeded, ropeNeeded, slabToUse);
		build(world, selected.relative(hitSide), count, hitSide, slabToUse);
	}


	public static int countBlocks(BlockPos start, Level world) {

		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		mutable.set(start);

		int count = 0;
		BlockState state = world.getBlockState(start);

		while (isReplaceable(world, start, state)) {
			count++;
			start = start.below();
			state = world.getBlockState(start);
		}
		return count;
	}

	public static boolean isReplaceable(Level world, BlockPos pos, BlockState state) {
		BlockPlaceContext blockItemUseContext = new BlockItemUseContextExt(world, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY,
			new BlockHitResult(new Vec3((double) pos.getX() + 0.5D + (double) Direction.DOWN.getStepX() * 0.5D, (double) pos.getY() + 0.5D + (double) Direction.DOWN.getStepY() * 0.5D, (double) pos.getZ() + 0.5D + (double) Direction.DOWN.getStepZ() * 0.5D), Direction.DOWN, pos, false));
		return pos.getY() > 0 && state.canBeReplaced(blockItemUseContext);
	}

	private static void build(Level world, BlockPos start, int count, final Direction facing, final Block type) {
		build(world, start, count, 0, facing, type);
	}

	private static void build(final Level world, final BlockPos start, final int count, final int iterations,
							  final Direction facing, final Block slabToUse) {
		ServerLifecycleHooks.getCurrentServer().execute(() -> {

			BlockState state = ModUtils.map.get(slabToUse).getRight().defaultBlockState().setValue(LadderBlock.FACING, facing);
			world.setBlockAndUpdate(start.below(iterations), state);
		});
		if (iterations + 1 < count)
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					build(world, start, count, iterations + 1, facing, slabToUse);
				}
			}, 100);
	}

	private static void consume(Player player, int woodNeeded, int ropeNeeded,
								Block woodType) {
		boolean noCost = ConfigHandler.getRopePerLadder() == 0 && ConfigHandler.getWoodPerLadder() == 0;
		if (player.getAbilities().instabuild || noCost) {
			return;
		}
		player.getInventory().clearOrCountMatchingItems(stack -> stack.getItem() == ContentHandler.rope.get(), ropeNeeded, player.inventoryMenu.getCraftSlots());
		player.getInventory().clearOrCountMatchingItems(stack -> stack.getItem() == woodType.asItem(), woodNeeded, player.inventoryMenu.getCraftSlots());
	}

	private static Block getSlabToUse(Player player) {
		return player.getInventory().items.stream().filter(stack -> stack.is(ItemTags.WOODEN_SLABS)).findFirst().map(stack -> Block.byItem(stack.getItem())).orElse(Blocks.OAK_SLAB);
	}

	private static boolean hasMaterials(Player player, int woodNeeded, int ropeNeeded,
										Block toFind) {
		boolean noCost = ConfigHandler.getRopePerLadder() == 0 && ConfigHandler.getWoodPerLadder() == 0;
		if (noCost || player.getAbilities().instabuild)
			return true;
		for (ItemStack i : player.getInventory().items) {
			if (i.isEmpty())
				continue;
			Item item = i.getItem();
			if (item == ContentHandler.rope.get()) {
				ropeNeeded -= i.getCount();
			} else if (item == toFind.asItem()) {
				woodNeeded -= i.getCount();
			}
		}
		return woodNeeded <= 0 && ropeNeeded <= 0;
	}
}
