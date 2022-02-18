package fr.edencraft.ecjackpot.jackpot;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JackpotCommand extends Command {

	private final Jackpot jackpot;

	protected JackpotCommand(@NotNull String name,
							 @NotNull String description,
							 @NotNull String usageMessage,
							 @NotNull List<String> aliases,
							 @NotNull Permission permission,
							 @NotNull Jackpot jackpot) {
		super(name, description, usageMessage, aliases);
		super.setPermission(permission.getName());
		this.jackpot = jackpot;
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("§cLa commande est reservé aux joueurs.");
			return true;
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("zizi")) {
			ComponentBuilder componentBuilder = new ComponentBuilder("Ahah très drôle gros con.");
			BaseComponent component = componentBuilder.getComponent(0);
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§6Baisse les yeux !")));
			player.spigot().sendMessage(component);
			return true;
		}
		if (args.length > 0) {
			player.sendMessage(super.usageMessage);
			return true;
		}
		if (getPermission() != null && !player.hasPermission(getPermission())) {
			player.sendMessage("§cTu n'as pas la permission d'utiliser cette commande.\n  §8• §e" + getPermission());
			return true;
		}
		return commandStuff(player, commandLabel, args);
	}

	public boolean commandStuff(@NotNull Player player, @NotNull String commandLabel, @NotNull String[] args) {
		jackpot.getJackpotProvider().openInv(player);
		return true;
	}
}
