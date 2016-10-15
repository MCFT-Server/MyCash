package mycash.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import mycash.Main;
import mycash.cash.Account;
import mycash.database.DataBase;
import mycash.exception.PlayerAlreadyHaveAccountException;
import mycash.exception.PlayerNotHaveAccountException;
import mycash.exception.PlayerNotHaveEnoughCashExeception;
import mycash.manager.CashManager;
import mycash.manager.PageCreater;

public class EventListener implements Listener {
	private Main plugin;

	public EventListener(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals(getMessage("commands-cash"))) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.ingame"));
				return true;
			}
			if (args.length < 1) {
				alert(sender, getMessage("commands-cash-usage"));
				return true;
			}
			if (args[0].toLowerCase().equals(getMessage("commands-mycash"))) {
				message(sender, getMessage("show-cash").replace("%cash",
						new Integer(new Account((Player) sender).getCash()).toString()));
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-charge"))) {
				if (args.length < 2) {
					alert(sender, getMessage("commands-cash-charge-usage"));
					return true;
				}
				String pinNumber = arr2pin(Arrays.copyOfRange(args, 1, args.length));
				if (!CashManager.getInstance().isRightPinNumber(pinNumber)) {
					alert(sender, getMessage("not-right-pin").replace("%pin", pinNumber));
					return true;
				}
				List<Object> list = getDB().getDB("waitlist").get(sender.getName().toLowerCase(),
						new ArrayList<Object>());
				list.add(pinNumber);
				getDB().getDB("waitlist").set(sender.getName().toLowerCase(), list);
				message(sender, getMessage("complete-charge-ask"));
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-log"))) {
				if (args.length < 2) {
					alert(sender, getMessage("commands-cash-log-usage"));
					return true;
				}
				try {
					int page = Integer.parseInt(args[1]);
					PageCreater creater = new PageCreater();
					Arrays.asList(creater.getPage(CashManager.getInstance().getLog(sender.getName()).toArray(), page))
							.forEach(msg -> {
								if (msg == null) return;
								message(sender, msg.toString());
							});
				} catch (NumberFormatException e) {
					alert(sender, getMessage("page-must-integer"));
				}
				return true;
			} else {
				alert(sender, getMessage("commands-cash-usage"));
				return true;
			}
		} else if (command.getName().equals(getMessage("commands-managecash"))) {
			if (!CashManager.getInstance().hasPermission(sender.getName()) && !(sender instanceof ConsoleCommandSender)) {
				sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
				return true;
			}
			if (args.length < 1) {
				alert(sender, getMessage("commands-managecash-usage"));
				return true;
			}
			if (args[0].toLowerCase().equals(getMessage("commands-see"))) {
				if (args.length < 2) {
					alert(sender, getMessage("commands-managecash-see-usage"));
					return true;
				}
				try {
					message(sender, getMessage("see-cash").replace("%player", args[1]).replace("%cash",
							new Integer(new Account(args[1]).getCash()).toString()));
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
					try {
						account.reduceCash(cash);
					} catch (PlayerNotHaveEnoughCashExeception e) {
						account.setCash(0);
					}
					message(sender, getMessage("take-cash").replace("%player", args[1]).replace("%cash", args[2]));
				} catch (PlayerNotHaveAccountException e) {
					alert(sender, getMessage("not-have-account").replace("%player", args[1]));
				}
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-chargelist"))) {
				if (args.length < 2) {
					alert(sender, getMessage("commands-managecash-chargelist-usage"));
					return true;
				}
				try {
					int page = Integer.parseInt(args[1]);
					PageCreater creater = new PageCreater();
					Arrays.asList(creater.getPage(Arrays.copyOfRange(getWaitList().toArray(), 0, getWaitList().size(), String[].class), page)).forEach(result -> {
						message(sender, result);
					});
				} catch (NumberFormatException e) {
					alert(sender, getMessage("page-must-integer"));
				}
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-check"))) {
				if (args.length < 3) {
					alert(sender, getMessage("commands-managecash-check-usage"));
					return true;
				}
				CashManager.getInstance().addLog(args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
				getDB().getDB("waitlist").remove(args[1].toLowerCase());
				message(sender, getMessage("check-cash").replace("%player", args[1]));
			} else if (args[0].toLowerCase().equals(getMessage("commands-givepermission"))) {
				if (!(sender instanceof ConsoleCommandSender)) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					return true;
				}
				if (args.length < 2) {
					alert(sender, getMessage("commands-managecash-givepermission"));
					return true;
				}
				CashManager.getInstance().givePermission(args[1]);
				message(sender, getMessage("give-permission").replace("%player", args[1]));
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-takepermission"))) {
				if (!(sender instanceof ConsoleCommandSender)) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					return true;
				}
				if (args.length < 2) {
					alert(sender, getMessage("commands-managecash-givepermission"));
					return true;
				}
				CashManager.getInstance().removePermission(args[0]);
				message(sender, getMessage("remove-permission").replace("%player", args[1]));
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-log"))) {
				if (args.length < 3) {
					alert(sender, getMessage("commands-managecash-log-usage"));
					return true;
				}
				try {
					int page = Integer.parseInt(args[2]);
					PageCreater creater = new PageCreater();
					Arrays.asList(creater.getPage(CashManager.getInstance().getLog(args[1]).toArray(), page))
							.forEach(msg -> {
								if (msg == null) return;
								message(sender, msg.toString());
							});
				} catch (NumberFormatException e) {
					alert(sender, getMessage("page-must-integer"));
				}
				return true;
			} else {
				alert(sender, getMessage("commands-managecash-usage"));
				return true;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private List<String> getWaitList() {
		List<String> list = new ArrayList<>();
		getDB().getDB("waitlist").getAll().keySet().forEach(name -> {
			getDB().getDB("waitlist").getList(name).forEach(pin -> {
				list.add(name + ": " + pin);
			});
		});
		return list;
	}

	private static String arr2pin(String[] array) {
		if (array.length == 1) {
			return String.join(" ", array[0].split("-"));
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
		} catch (PlayerAlreadyHaveAccountException e) {
		}
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
