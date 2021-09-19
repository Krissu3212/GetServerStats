package me.Hullumeelne.GetServerStats;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ChatSaver implements Listener {
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) throws FileNotFoundException, IOException, ParseException {

        String message = event.getMessage();
        String author = event.getPlayer().getDisplayName();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        JSONObject object = new JSONObject();
        object.put("message", message);
        object.put("author", author);
        object.put("time", time);
        
        addToJson(object);
	}
	
	@SuppressWarnings("unchecked")
	public void addToJson(JSONObject message) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader("H:/Minecraft serverid/Veebilehega TUK MC Bukkit 1.16.5 (originaalversioon 1.15.2)/static/server_chat.json"));
		JSONArray getArray = (JSONArray) json.get("messages");
		getArray.add(message);

		try (FileWriter file = new FileWriter("H:/Minecraft serverid/Veebilehega TUK MC Bukkit 1.16.5 (originaalversioon 1.15.2)/static/server_chat.json", false)) {
	        file.write(json.toJSONString());
	        file.flush();
	    } catch (IOException e) {
	    	System.out.println("Can't save file");
	        e.printStackTrace();
	    }
	}
}
