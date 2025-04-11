package thederpgamer.coleniaSec.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import thederpgamer.coleniaSec.data.DataManager;

import java.util.Objects;

public class ViewDataCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(args.length < 4) {
			sender.sendMessage("Usage: /cs viewdata <type|*/all> <uid>");
			return true;
		}
		try {
			if(args[2].equalsIgnoreCase("all") || args[2].equalsIgnoreCase("*")) {
				sender.sendMessage(DataManager.getAllData(DataManager.DataTypes.valueOf(args[2])).getAsString());
			} else {
				sender.sendMessage(Objects.requireNonNull(DataManager.getData(DataManager.DataTypes.valueOf(args[2]), args[3])).getAsString());
			}
		} catch(Exception exception) {
			exception.printStackTrace();
			sender.sendMessage("Usage: /cs viewdata <type> <*/all|uid>");
		}
		return true;
	}
}