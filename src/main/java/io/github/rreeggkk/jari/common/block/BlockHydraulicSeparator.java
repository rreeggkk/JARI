package io.github.rreeggkk.jari.common.block;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.reference.GuiIDs;
import io.github.rreeggkk.jari.common.reference.ModInformation;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cofh.lib.util.helpers.FluidHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHydraulicSeparator extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon onTexture, offTexture, topTexture;

	public BlockHydraulicSeparator() {
		super(Material.iron);
		setCreativeTab(JARI.tabRreeactors);
		setBlockName("blockHydraulicSeparator");
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		unknownNameOfFunction(world, x, y, z);
	}

	private void unknownNameOfFunction(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block block = world.getBlock(x, y, z - 1);
			Block block1 = world.getBlock(x, y, z + 1);
			Block block2 = world.getBlock(x - 1, y, z);
			Block block3 = world.getBlock(x + 1, y, z);
			byte b0 = 3;

			if (block.func_149730_j() && !block1.func_149730_j()) {
				b0 = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j()) {
				b0 = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j()) {
				b0 = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j()) {
				b0 = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	 @SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? topTexture : side != meta / 2 ? blockIcon
				: isOn(meta) ? onTexture : offTexture;
	}

	@Override
	 @SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegistry) {
		blockIcon = iconRegistry.registerIcon(ModInformation.ID
				+ ":hydraulicSeparator_side");
		onTexture = iconRegistry.registerIcon(ModInformation.ID
				+ ":hydraulicSeparator_front_on");
		offTexture = iconRegistry.registerIcon(ModInformation.ID
				+ ":hydraulicSeparator_front_off");
		topTexture = iconRegistry.registerIcon(ModInformation.ID
				+ ":hydraulicSeparator_top");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	 public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int hitx, float hity, float hitz, float side) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		
		
		if (FluidHelper.isPlayerHoldingFluidContainerItem(player)) {
			FluidStack f = FluidHelper.extractFluidFromHeldContainer(player, 0, false);
			if (f.fluidID == FluidHelper.WATER.fluidID) {
				TileEntityHydraulicSeparator t = (TileEntityHydraulicSeparator)world.getTileEntity(x, y, z);
				if (t.getWaterCount() + f.amount <= TileEntityHydraulicSeparator.maxWater) {
					FluidHelper.extractFluidFromHeldContainer(player, 0, true);
					t.setWaterCount(t.getWaterCount() + f.amount);
				}
			}
		}
		
		player.openGui(JARI.instance, GuiIDs.HYDRAULIC_SEPARATOR, world, x, y,
				z);
		return true;
	}

	/**
	 * Update which block the furnace is using depending on whether or not it is
	 * burning
	 */
	public static void updateBlockState(boolean isRunning, World world, int x,
			int y, int z) {
		int l = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getTileEntity(x, y, z);

		if (isRunning) {
			world.setBlockMetadataWithNotify(x, y, z, l - 1, 3);
		} else {
			world.setBlockMetadataWithNotify(x, y, z, l + 1, 3);
		}

		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(x, y, z, tileentity);
		}
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	 public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityHydraulicSeparator();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	 public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase e, ItemStack item) {
		int l = MathHelper
				.floor_double(e.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 10, 2);
		}

		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 6, 2);
		}

		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 8, 2);
		}
	}

	@Override
	 public void breakBlock(World world, int x, int y, int z, Block block,
			int p_149749_6_) {
		TileEntityHydraulicSeparator tile = (TileEntityHydraulicSeparator) world
				.getTileEntity(x, y, z);

		if (tile != null) {
			for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
				ItemStack itemstack = tile.getStackInSlot(i1);

				if (itemstack != null) {
					float f = JARI.random.nextFloat() * 0.8F + 0.1F;
					float f1 = JARI.random.nextFloat() * 0.8F + 0.1F;
					float f2 = JARI.random.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j1 = JARI.random.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						EntityItem entityitem = new EntityItem(world, x + f, y
								+ f1, z + f2, new ItemStack(
								itemstack.getItem(), j1,
								itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound(
									(NBTTagCompound) itemstack.getTagCompound()
											.copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (float) JARI.random.nextGaussian()
								* f3;
						entityitem.motionY = (float) JARI.random.nextGaussian()
								* f3 + 0.2F;
						entityitem.motionZ = (float) JARI.random.nextGaussian()
								* f3;
						world.spawnEntityInWorld(entityitem);
					}
				}
			}

			world.func_147453_f(x, y, z, block);
		}

		super.breakBlock(world, x, y, z, block, p_149749_6_);
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	 @SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z,
			Random random) {
		if (isOn(world, x, y, z)) {
			int l = world.getBlockMetadata(x, y, z);
			float f = x + 0.5F;
			float f1 = y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = z + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;

			if (l == 9) {
				world.spawnParticle("splash", f - f3, f1, f2 + f4, 0.0D, 0.0D,
						0.0D);
				world.spawnParticle("splash", f - f3, f1, f2 + f4, 0.0D, 0.0D,
						0.0D);
			} else if (l == 11) {
				world.spawnParticle("splash", f + f3, f1, f2 + f4, 0.0D, 0.0D,
						0.0D);
				world.spawnParticle("splash", f + f3, f1, f2 + f4, 0.0D, 0.0D,
						0.0D);
			} else if (l == 5) {
				world.spawnParticle("splash", f + f4, f1, f2 - f3, 0.0D, 0.0D,
						0.0D);
				world.spawnParticle("splash", f + f4, f1, f2 - f3, 0.0D, 0.0D,
						0.0D);
			} else if (l == 7) {
				world.spawnParticle("splash", f + f4, f1, f2 + f3, 0.0D, 0.0D,
						0.0D);
				world.spawnParticle("splash", f + f4, f1, f2 + f3, 0.0D, 0.0D,
						0.0D);
			}
		}
	}

	/**
	 * If this returns true, then comparators facing away from this block will
	 * use the value from getComparatorInputOverride instead of the actual
	 * redstone signal strength.
	 */
	@Override
	 public boolean hasComparatorInputOverride() {
		return false;
	}

	/**
	 * If hasComparatorInputOverride returns true, the return value from this is
	 * used instead of the redstone signal strength when this block inputs to a
	 * comparator.
	 */
	@Override
	 public int getComparatorInputOverride(World world, int x, int y, int z,
			int p_149736_5_) {
		return Container.calcRedstoneFromInventory((IInventory) world
				.getTileEntity(x, y, z));
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	 @SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(this);
	}

	public boolean isOn(World world, int x, int y, int z) {
		return isOn(world.getBlockMetadata(x, y, z));
	}

	public boolean isOn(int meta) {
		if (meta % 2 == 0) {
			return false;
		}
		return true;
	}
}
