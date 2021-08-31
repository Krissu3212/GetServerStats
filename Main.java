package me.Hullumeelne.GetServerStats;

import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		System.out.println("GetServerStats has been turned on");
		try {
			modifyFile("online");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error1");
		}
		
		UpdateData thread = new UpdateData();
		thread.start();
		
	}
	@Override
	public void onDisable() {
		System.out.println("GetServerStats has been turned off");
		try {
			modifyFile("offline");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}

	@SuppressWarnings("unchecked")
	public void modifyFile(String status) throws IOException {
		JSONObject object = new JSONObject();
		object.put("mc_server_status", status);
		
		int playerCount = Bukkit.getServer().getWorld("world").getPlayers().size();
		object.put("players_online", playerCount);
		
		if (playerCount != 0) {
			String playerList = "";
			int count = 0;
			for (Player player : Bukkit.getOnlinePlayers())
			{
				if (count == playerCount) {
					playerList = playerList + player.toString();
				} else {
					playerList = playerList + player.toString() + ", ";
				}
				count = count + 1;
			}
			object.put("player_list", playerList);
		} else {
			object.put("player_list", "");
		}
		
		try (FileWriter file = new FileWriter("H:/Minecraft serverid/Veebilehega TUK MC Bukkit 1.16.5 (originaalversioon 1.15.2)/static/server_status.json", false)) {
			file.write(object.toJSONString());
	        file.flush();
		} catch (Exception e) {
			System.out.println("Error saving file");
			e.printStackTrace();
		}
		
	}
	
	class UpdateData extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(15000);
					modifyFile("online");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			 
		}
	}
	
}
