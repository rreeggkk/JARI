package io.github.rreeggkk.jari.common.command;

import io.github.rreeggkk.jari.common.item.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import org.apfloat.Apfloat;

public class MetalLumpCommand implements ICommand {
	@SuppressWarnings("rawtypes")
	private List aliases;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MetalLumpCommand() {
		aliases = new ArrayList();
		aliases.add("getmetallump");
		aliases.add("gml");
		aliases.add("generatemetallump");
	}

	@Override
	public String getCommandName() {
		return "generatemetallump";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "gml [<Element Name/Isotope Name> <Amount>] ...";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] astring) {
		if (astring.length == 0) {
			sender.addChatMessage(new ChatComponentText("Invalid arguments"));
			return;
		}
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;

			ItemStack stack = new ItemStack(ItemRegistry.metalLump);

			for (int i = 1; i < astring.length; i += 2) {
				ItemRegistry.metalLump.addMetalToLump(stack, astring[i - 1],
						new Apfloat(Double.parseDouble(astring[i])));
			}

			if (!player.inventory.addItemStackToInventory(stack)) {
				sender.addChatMessage(new ChatComponentText(
						"Failed to add to inventory"));
			}
		}

		// icommandsender.sendChatToPlayer("Sample: [" + astring[0] + "]");

	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return icommandsender.canCommandSenderUseCommand(0, "gml");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
