package mycash.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import mycash.Main;
import mycash.cash.Account;
import mycash.database.DataBase;
import mycash.exception.PlayerAlreadyHaveAccountException;
import mycash.exception.PlayerNotHaveAccountException;
import mycash.manager.CashManager;

public class EventListener implements Listener {
	private Main plugin;
	public EventListener(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals(getMessage("commands-cash"))) {
			if (args.length < 1) { 
				alert(sender, getMessage("commands-cash-usage"));
				return true;
			}
			if (args[0].toLowerCase().equals(getMessage("commands-mycash"))) { 
				message(sender, getMessage("show-cash").replace("%cash", String.valueOf(new Account((Player) sender).getCash())));
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-charge"))) { 
				String pinNumber = arr2pin(popArray(args));
				if (!CashManager.getInstance().isRightPinNumber(pinNumber)) {
					alert(sender, getMessage("not-right-pin").replace("%pin", pinNumber));
					return true;
				}
				List<Object> list = getDB().getDB("waitlist").get(sender.getName(), new ArrayList<Object>());
				list.add(pinNumber);
				getDB().getDB("waitlist").set(sender.getName().toLowerCase(), list);
				message(sender, getMessage("complete-charge-ask"));
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-log"))) {
				
			} else {
				alert(sender, getMessage("commands-cash-usage"));
				return true;
			}
		} else if (command.getName().equals(getMessage("commands-managecash"))) {
			if (args.length < 1) { 
				alert(sender, getMessage("commands-cash-usage"));
				return true;
			}
			if (args[0].toLowerCase().equals(getMessage("commands-see"))) {
				if (args.length < 2) {
					alert(sender, getMessage("commands-managecash-see-usage"));
					return true;
				}
				try {
					message(sender, getMessage("see-cash").replace("%player", args[1]).replace("%cash", String.valueOf(new Account(args[1]).getCash())));
				} catch (PlayerNotHaveAccountException e) {
					alert(sender, getMessage("not-have-account").replace("%player", args[1]));
				}
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-give"))) {
				if (args.length < 3) {
					alert(sender, getMessage("commands-managecash-give-usage"));
					return true;
				}
				try {
					Account account = new Account(args[1]);
					int cash = Integer.parseInt(args[2]);
					account.addCash(cash);
					message(sender, getMessage("give-cash").replace("%player", args[1]).replace("%cash", args[2]));
				} catch (PlayerNotHaveAccountException e) {
					alert(sender, getMessage("not-have-account").replace("%player", args[1]));
				}
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-take"))) {
				if (args.length < 3) {
					alert(sender, getMessage("commands-managecash-take-usage"));
					return true;
				}
				try {
					Account account = new Account(args[1]);
					int cash = Integer.parseInt(args[2]);
					account.addCash(cash);
					message(sender, getMessage("take-cash").replace("%player", args[1]).replace("%cash", args[2]));
				} catch (PlayerNotHaveAccountException e) {
					alert(sender, getMessage("not-have-account").replace("%player", args[1]));
				}
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-chargelist"))) {
				//TODO
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T[] popArray(T[] array) {
		List<T> newArray = new ArrayList<>();
		for (int i = 1; i < array.length; i++) {
			newArray.add(array[i]);
		}
		return (T[]) newArray.toArray();
	}
	
	private static String arr2pin(String[] array) {
		if (array.length == 1) {
			return String.join(" ", array[0].split(" "));
		}
		return String.join(" ", array);
	}
	
	public Main getPlugin() {
		return plugin;
	}
	public DataBase getDB() {
		return plugin.getDB();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		try {
			CashManager.getInstance().initCash(event.getPlayer());
		} catch (PlayerAlreadyHaveAccountException e) { }
	}
	
	private String getMessage(String msg) {
		return getDB().get(msg);
	}
	private void message(CommandSender player, String msg) {
		getDB().message(player, msg);
	}
	private void alert(CommandSender player, String msg) {
		getDB().alert(player, msg);
	}
}
