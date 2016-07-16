package mycash.manager;

import java.util.regex.Pattern;

import cn.nukkit.Player;
import mycash.cash.Account;
import mycash.database.DataBase;
import mycash.exception.PlayerAlreadyHaveAccountException;
import mycash.exception.PlayerNotHaveAccountException;
import mycash.exception.PlayerNotHaveEnoughCashExeception;

public class CashManager {
	private static CashManager instance;
	
	public CashManager() {
		if (instance == null) instance = this;
	}
	public DataBase getDB() {
		return DataBase.getInstance();
	}
	
	public static CashManager getInstance() {
		return instance;
	}
	
	
	public void initCash(Player player) throws PlayerAlreadyHaveAccountException {
		initCash(player.getName());
	}
	public void initCash(String player) throws PlayerAlreadyHaveAccountException {
		if (hasAccount(player)) {
			throw new PlayerAlreadyHaveAccountException();
		}
		getDB().getDB("cash").set(player, getDB().getDB("config").getInt("default-cash", 0));
	}
	
	
	public boolean hasAccount(Player player) {
		return hasAccount(player.getName());
	}
	public boolean hasAccount(String player) {
		player = player.toLowerCase();
		if (getDB().getDB("cash").getInt(player, -1) == -1) {
			return false;
		}
		return true;
	}
	
	public Account getAccount(Player player) {
		return new Account(player);
	}
	public Account getAccount(String player) {
		return new Account(player);
	}
	
	/**
	 * 
	 * @param pinNumber 
	 * pinNumber pattern is xxxx-xxxx-xxxx-xxxxxx
	 * @return boolean
	 */
	public boolean isRightPinNumber(String pinNumber) {
		return Pattern.matches("....-....-....-....", pinNumber) ? true : Pattern.matches("....-....-....-......", pinNumber);
	}
}
