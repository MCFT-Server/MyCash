package mycash.listener;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import mycash.Main;

public class EventListener implements Listener {
	private Main plugin;
	public EventListener(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals("캐쉬")) {
			if (args.length < 1) {
				return false;
			}
			switch (args[0].toLowerCase()) {
			case "내캐쉬" :
				
				break;
			}
		}
		return true;
	}
}
