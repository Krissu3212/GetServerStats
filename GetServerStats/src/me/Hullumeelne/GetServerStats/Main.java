package me.Hullumeelne.GetServerStats;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main extends JavaPlugin {
	
	String motd;
	String motdAuthor;
	Boolean stopping = false;
	
	@Override
	public void onEnable() {
		System.out.println("GetServerStats has been turned on");
		this.getCommand("motd").setExecutor((CommandExecutor)new MotdCommand());
		Bukkit.getPluginManager().registerEvents(new ChatSaver(), this);

		// Get MOTD and set it to motd variable to be accessed
		try {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(new FileReader("H:/Minecraft serverid/Veebilehega TUK MC Bukkit 1.16.5 (originaalversioon 1.15.2)/static/server_status.json"));
			motd = (String) json.get("motd");
			motdAuthor = (String) json.get("motd_author");
		} catch (IOException | ParseException e) {
			System.out.println("error here");
		}

		try {
			modifyFile("online");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("error");
		}

		UpdateData thread = new UpdateData();
		thread.start();

		try {
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("messages", array);
			FileWriter file = new FileWriter("H:/Minecraft serverid/Veebilehega TUK MC Bukkit 1.16.5 (originaalversioon 1.15.2)/static/server_chat.json", false);
			file.write(object.toJSONString());
	        file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		stopping = true;
		
		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(new FileReader("H:/Minecraft serverid/Veebilehega TUK MC Bukkit 1.16.5 (originaalversioon 1.15.2)/static/server_chat.json"));
			JSONArray array = (JSONArray) json.get("messages");
			// Change MOTD only if server hasn't been very inactive
			if (array.size() > 5) {
				int random = (int)(Math.random() * array.size());
				JSONObject message = (JSONObject) array.get(random);
				motd = (String) message.get("message");
				motdAuthor = (String) message.get("author");
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("error2");
		}

		try {
			modifyFile("offline");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("error3");
		}
		System.out.println("GetServerStats has been turned off");
	}

	@SuppressWarnings("unchecked")
	public void modifyFile(String status) throws IOException, ParseException {
		// Get data from MC server and save it to a JSON file
		JSONObject object = new JSONObject();
		object.put("mc_server_status", status);
		
		if (status.equals("online")) {
			
			int playerCount = Bukkit.getServer().getWorld("world").getPlayers().size();
			object.put("players_online", playerCount);
			if (playerCount != 0) {
				String playerList = "";
				int count = 1;
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (count == playerCount) {
						playerList = playerList + player.getDisplayName();
					} else {
						playerList = playerList + player.getDisplayName() + ", ";
					}
					count = count + 1;
				}
				object.put("player_list", playerList);
			} else {
				object.put("player_list", "");
			}
		} else {
			object.put("players_online", 0);
			object.put("player_list", "");
		}
		

		object.put("motd", motd);
		object.put("motd_author", motdAuthor);

		// Save file
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
					if (stopping == false) {
						modifyFile("online");
					}
					
				} catch (InterruptedException | IOException | ParseException e) {
					e.printStackTrace();
					System.out.println("error4");
				}
			}
		}
	}

	public class MotdCommand implements CommandExecutor{
		@Override
		public boolean onCommand( CommandSender sender,  Command cmd,  String label, String[] args) {

			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Tänane MOTD: " + ChatColor.AQUA + motd + ChatColor.BLUE + " - " + motdAuthor);

			return true;
		}
	}
}
