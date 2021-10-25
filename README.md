# GetServerStats
(Eclipse project)

Second java plugin made for TUK Minecraft server that saves server statistics to a JSON file.

Data is being refreshed every 15 seconds in the server_status.json file.
Currently saves data for "mc_server_status", "player_count", "player_list", "motd" and "motd_author".
(this is only one of my few plugins)

***

05.09.2021 Update: now also saving last 50 messages of server chat into a seperate JSON file (ChatSaver.java).

19.09.2021 Update: added MOTD which is saved into server_status.json file every time server stops. Server is also saving the whole chat instead of last 50 messages now.

26.09.2021 Update: fixed an issue where "mc_server_status" was being saved as online instead of offline when plugin was turning off.

25.10.2021 Update: fixed comma issue when adding player name strings together for player_list variable
