package com.mrtrollnugnug.ropebridge.handler;

import com.mrtrollnugnug.ropebridge.block.RopeBridgeBlock;
import com.mrtrollnugnug.ropebridge.lib.ModUtils;
import com.mrtrollnugnug.ropebridge.lib.Constants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BridgeBuildingHandler
{

	private BridgeBuildingHandler()
	{
	}

	public static void newBridge(Player player, ItemStack stack, BlockPos pos1, BlockPos pos2)
	{
		final LinkedList<SlabPosHandler> bridge = new LinkedList<>();
		boolean allClear = true;
		int x1;
		int x2;
		int y1;
		int y2;
		int z1;
		int z2;
		boolean rotate = getRotate(pos1, pos2);
		if (!rotate)
		{
			x1 = pos1.getX();
			y1 = pos1.getY();
			z1 = pos1.getZ();
			x2 = pos2.getX();
			y2 = pos2.getY();
			z2 = pos2.getZ();
		}
		else
		{
			x1 = pos1.getZ();
			y1 = pos1.getY();
			z1 = pos1.getX();
			x2 = pos2.getZ();
			y2 = pos2.getY();
			z2 = pos2.getX();
		}
		if (Math.abs(z2 - z1) > 3)
		{
			ModUtils.tellPlayer(player, Constants.Messages.NOT_CARDINAL);
			return;
		}
		double m;
		double b;
		double distance;
		int distInt;

		m = (double) (y2 - y1) / (double) (x2 - x1);
		if (!ConfigHandler.isIgnoreSlopeWarnings() && Math.abs(m) > 0.2)
		{
			ModUtils.tellPlayer(player, Constants.Messages.SLOPE_GREAT);
			return;
		}
		b = y1 - m * x1;
		distance = Math.abs(x2 - x1);
		distInt = Math.abs(x2 - x1);
		if (distInt < 2)
		{
			return;
		}
		if (!player.getAbilities().instabuild && !hasMaterials(player, distInt - 1))
		{
			return;
		}
		//происходит отрисовка веревочной вертикальной лестницы из Майнкрафта. Но только в горизонтальном положении. И еще со спусками.
		//Баг: конец спуска всегда имеет баг: веревка от лестницы находится на высоте самой лестницы, а блоки для спуска ниже и от этого она висит в воздухе.
		for (int x = Math.min(x1, x2) + 1; x <= Math.max(x1, x2) - 1; x++)
		{
			for (int y = Math.max(y1, y2); y >= Math.min(y1, y2) - distInt / 8 - 1; y--)
			{
				final double funcVal = m * x + b - distance / 1000 * Math.sin((x - Math.min(x1, x2)) * (Math.PI / distance)) * ConfigHandler.getBridgeDroopFactor() + ConfigHandler.getBridgeYOffset();

				if (y + 0.5 > funcVal && y - 0.5 <= funcVal)
				{
					int level;
					if (funcVal >= y)
					{
						if (funcVal >= y + 0.25)
						{
							level = 3;
						}
						else
						{
							level = 2;
						}
					}
					else
					{
						if (funcVal >= y - 0.25)
						{
							level = 1;
						}
						else
						{
							level = 0;
						}
					}
					allClear = addSlab(player.level, bridge, x, y + 1, z1, level, rotate) && allClear;
				}
			}
		}

		if (allClear)
		{
			final Block slab = getSlabs(player);

			if (slab != null && !player.getAbilities().instabuild)
			{
				takeMaterials(player, distInt - 1);
				stack.hurtAndBreak(ConfigHandler.getBridgeDamage(), player, playerEntity ->
					playerEntity.broadcastBreakEvent(player.getUsedItemHand()));
			}
			buildOneBlockOfBridge(player.level, bridge, slab, 0, rotate);
		}
		else
		{
			ModUtils.tellPlayer(player, Constants.Messages.OBSTRUCTED);
		}
	}

	private static boolean getRotate(BlockPos p1, BlockPos p2)
	{
		return Math.abs(p1.getX() - p2.getX()) <= Math.abs(p1.getZ() - p2.getZ());
	}

	private static boolean hasMaterials(Player player, int dist)
	{
		boolean noCost = ConfigHandler.getSlabsPerBridge() == 0 && ConfigHandler.getRopePerBridge() == 0;
		if (player.getAbilities().instabuild || noCost)
		{
			return true;
		}
		final int ropeNeeded = dist * ConfigHandler.getRopePerBridge();
		final int slabsNeeded = dist * ConfigHandler.getSlabsPerBridge();
		int slabsHad = 0;
		int ropeHad = 0;

		for (int i = 0; i < 36; i++)
		{
			final ItemStack stack = player.getInventory().items.get(i);
			if (stack.isEmpty())
			{
				continue;
			}
			final Item item = stack.getItem();
			if (item == ContentHandler.rope.get())
			{
				ropeHad += stack.getCount();
			}
			if (stack.is(ItemTags.WOODEN_SLABS))
			{
				slabsHad += stack.getCount();
			}
		}
		if (slabsHad >= slabsNeeded && ropeHad >= ropeNeeded)
		{
			return true;
		}
		else
		{
			ModUtils.tellPlayer(player, Constants.Messages.UNDERFUNDED_BRIDGE, dist, ropeNeeded);
			return false;
		}
	}

	private static void takeMaterials(Player player, int dist)
	{
		boolean noCost = ConfigHandler.getSlabsPerBridge() == 0 && ConfigHandler.getRopePerBridge() == 0;
		if (player.getAbilities().instabuild || noCost)
		{
			return;
		}
		int slabsNeeded = dist * ConfigHandler.getSlabsPerBridge();
		int ropeNeeded = dist * ConfigHandler.getRopePerBridge();
		int i = 0;

		for (; i < 36; i++)
		{
			final ItemStack stack = player.getInventory().items.get(i);
			if (stack.isEmpty())
			{
				continue;
			}
			final Item item = stack.getItem();
			if (ropeNeeded > 0 && item == ContentHandler.rope.get())
			{
				int toConsume = Math.min(stack.getCount(), ropeNeeded);
				ropeNeeded -= toConsume;
				stack.shrink(toConsume);
			}
			else if (slabsNeeded > 0 && stack.is(ItemTags.WOODEN_SLABS))
			{
				int toConsume = Math.min(stack.getCount(), slabsNeeded);
				slabsNeeded -= toConsume;
				stack.shrink(toConsume);
			}
		}
	}

	private static boolean addSlab(Level world, LinkedList<SlabPosHandler> list, int x, int y, int z, int level, boolean rotate)
	{
		boolean isClear;
		BlockPos pos;
		if (rotate)
		{
			pos = new BlockPos(z, y, x);
		}
		else
		{
			pos = new BlockPos(x, y, z);
		}

		isClear = ConfigHandler.isBreakThroughBlocks() || world.isEmptyBlock(pos) || LadderBuildingHandler.isReplaceable(world, pos, world.getBlockState(pos));
		list.add(new SlabPosHandler(pos, level, rotate));
		if (!isClear)
		{
			spawnSmoke(world, pos, 15);
		}
		return isClear;
	}

	// Controls if blocks are in the way
	private static void spawnSmoke(Level world, BlockPos pos, int times)
	{

		if (times > 0)
		{
			((ServerLevel) world).sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			final Level finworld = world;
			final BlockPos finPos = pos;
			final int finTimes = times - 1;
			new Timer().schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					spawnSmoke(finworld, finPos, finTimes);
				}
			}, 1000);
		}
	}

	private static void buildOneBlockOfBridge(final Level world, final List<SlabPosHandler> bridge, final Block slabBlock, int index, boolean rotated)
	{

		SlabPosHandler slab;
		if (index < bridge.size())
		{
			slab = bridge.get(index);
			int backLevel = index > 0 ? bridge.get(index - 1).getLevel() : 0;
			BlockState state = ModUtils.map.get(slabBlock).getLeft().defaultBlockState()
				.setValue(RopeBridgeBlock.PROPERTY_HEIGHT, slab.getLevel())
				.setValue(RopeBridgeBlock.PROPERTY_BACK, backLevel)
				.setValue(RopeBridgeBlock.ROTATED, rotated);

			world.setBlockAndUpdate(slab.getBlockPos(), state);
			spawnSmoke(world, new BlockPos(slab.getBlockPos().getX(), slab.getBlockPos().getY(), slab.getBlockPos().getZ()), 1);
			new Timer().schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					buildOneBlockOfBridge(world, bridge, slabBlock, index + 1, rotated);
				}
			}, 100);
		}
	}

	private static Block getSlabs(Player player)
	{
		for (final ItemStack stack : player.getInventory().items)
		{
			if (stack.isEmpty())
			{
				continue;
			}
			if (stack.is(ItemTags.WOODEN_SLABS))
				return Block.byItem(stack.getItem());
		}
		//todo: this is reachable when the player has 0 slabs, a fallback of oak is used in this case, maybe allow for different blocks?
		return Blocks.OAK_SLAB;
	}
}
