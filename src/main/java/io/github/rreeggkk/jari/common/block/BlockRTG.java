package io.github.rreeggkk.jari.common.block;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityRTG;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *
 * The block for the Hydraulic Separator. It is based of of the Vanilla furnace.
 *
 * @author rreeggkk
 *
 */
public class BlockRTG extends BlockContainer {
	// Block's textures
	@SideOnly(Side.CLIENT)
	private IIcon topTexture;

	/**
	 * The construtor for the Hydraulic Separator class
	 */
	public BlockRTG() {
		super(Material.iron);

		// Set the creative tab
		setCreativeTab(JARI.tabRreeactors);

		// Set the block name
		setBlockName("blockRTG");
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		// Copied the function from vanilla furnace so idk what it does
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
		// Get the correct icon for the side of the block
		return side == 1 || side == 0 ? topTexture :  blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegistry) {
		// Load all textures for the block
		blockIcon = iconRegistry.registerIcon(ModInformation.ID
				+ ":rtg_side");
		topTexture = iconRegistry.registerIcon(ModInformation.ID
				+ ":rtg_top");
	}

	/**
	 * Called upon block activation (right click ons the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int hitx, float hity, float hitz, float side) {

		// Get the tile entity
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		// If the tile entity doesn't exsist or the player is sneaking
		if (tileEntity == null || player.isSneaking()) {
			// Let the normal action happen
			return false;
		}

		// Open the GUI
		player.openGui(JARI.instance, GuiIDs.RTG, world, x, y,
				z);

		// Stop any other right click actions
		return true;
	}

	/**
	 * Update the state is using depending on whether or not it is running
	 */
	public static void updateBlockState(boolean isRunning, World world, int x,
			int y, int z) {

		// Metadata
		int l = world.getBlockMetadata(x, y, z);
		// TileEntity
		TileEntity tileentity = world.getTileEntity(x, y, z);

		// Change the metadata accordingly
		if (isRunning) {
			world.setBlockMetadataWithNotify(x, y, z, l - 1, 3);
		} else {
			world.setBlockMetadataWithNotify(x, y, z, l + 1, 3);
		}

		// Validate the tileentity
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
	public TileEntity createNewTileEntity(World world, int i1) {
		return new TileEntityRTG();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase e, ItemStack item) {

		// Determine the direction
		int l = MathHelper.floor_double(e.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		// Depending on direction set metadata
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
		// Get the tile entity
		TileEntityRTG tile = (TileEntityRTG) world
				.getTileEntity(x, y, z);

		// If it exists
		if (tile != null) {
			// Loop through the inventory
			for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
				// Get the itemstack in the current slot
				ItemStack itemstack = tile.getStackInSlot(i1);

				// If it exists
				if (itemstack != null) {
					// Get random numbers to use for direction stack size and
					// velocity
					float f = JARI.random.nextFloat() * 0.8F + 0.1F;
					float f1 = JARI.random.nextFloat() * 0.8F + 0.1F;
					float f2 = JARI.random.nextFloat() * 0.8F + 0.1F;

					// Until the stack size is 0
					while (itemstack.stackSize > 0) {
						// Get a random size
						int j1 = JARI.random.nextInt(21) + 10;

						// Make sure it is smaller or the same size
						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						// Decrease itemstack size
						itemstack.stackSize -= j1;

						// Create a new item entity
						EntityItem entityitem = new EntityItem(world, x + f, y
								+ f1, z + f2, new ItemStack(
								itemstack.getItem(), j1,
								itemstack.getItemDamage()));

						// Copy NBT data
						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound(
									(NBTTagCompound) itemstack.getTagCompound()
											.copy());
						}

						// Multiplier for speed
						float f3 = 0.05F;
						entityitem.motionX = (float) JARI.random.nextGaussian()
								* f3;
						entityitem.motionY = (float) JARI.random.nextGaussian()
								* f3 + 0.2F;
						entityitem.motionZ = (float) JARI.random.nextGaussian()
								* f3;
						// Spawn it into the world
						world.spawnEntityInWorld(entityitem);
					}
				}
			}

			// Call some function
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
	public void randomDisplayTick(World world, int x, int y, int z,Random random) {
	}

	/**
	 * If this returns true, then comparators facing away from this block will
	 * use the value from getComparatorInputOverride instead of the actual
	 * redstone signal strength.
	 */
	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	/**
	 * If hasComparatorInputOverride returns true, the return value from this is
	 * used instead of the redstone signal strength when this block inputs to a
	 * comparator.
	 */
	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z,
			int p_149736_5_) {
		return ((TileEntityRTG) world.getTileEntity(x, y, z))
				.getComparatorOutput();
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		// Get the item form of the block
		return Item.getItemFromBlock(this);
	}

	public boolean isOn(World world, int x, int y, int z) {
		// Check if the machine is on
		return isOn(world.getBlockMetadata(x, y, z));
	}

	public boolean isOn(int meta) {
		// If right most bit is 0
		if (meta % 2 == 0) {
			// return false
			return false;
		}
		// else return true
		return true;
	}
}
