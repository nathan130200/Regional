package me.nfdsr.regional.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.nfdsr.regional.data.IpinfoResponse;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class PlayerListener implements Listener {
	public static final Gson GSON = new GsonBuilder()
										.serializeNulls()
										.setPrettyPrinting()
										.create();
	
	@EventHandler
	public void onLogin( PlayerLoginEvent e ){
		if(e.getAddress().isAnyLocalAddress() || e.getAddress().isLinkLocalAddress()) {
			e.allow();
			return;
		}
		
		try {
			var str = String.format("https://ipinfo.io/%s/json", e.getAddress().getHostAddress());
			var url = new URL(str);
			
			
			
			var conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");;
			conn.setRequestProperty("content-type", "application/json");
			
			var in = new InputStreamReader(conn.getInputStream());
			var reader = new BufferedReader(in);
			
			var line = "";
			var json = "";
			
			while ((line = reader.readLine()) != null)
				json += line;
			
			// System.out.println("[DEBUG] " + str);
			
			var result = GSON.fromJson(json, IpinfoResponse.class);
			if(result.error != null){
				e.disallow(Result.KICK_OTHER, "§4[Remote disconnect] §7reason='Erro interno do servidor';code=500;msg='" + result.error.message + "'");
				
				reader.close();
				reader = null;
				
				in.close();
				in = null;
				
				return;
			}
			else {
				
				// System.out.println("[DEBUG] " + json);
				
				// permite conexão com ip local.
				if(result.bogon != null && result.bogon) {
					e.allow();
					return;
				}
				
				if(!result.country.equals("BR")){
					
					Bukkit.getConsoleSender().sendMessage(String.format("§4[Regional] §7Tentativa de login de jogador %s (%s), endereço %s, região %s não autorizada.",
						e.getPlayer().getName(),
						e.getPlayer().getUniqueId().toString(),
						e.getAddress().getHostAddress(),
						result.region
					));
					
					e.disallow(Result.KICK_BANNED, "§4[Remote disconnect] §7reason='Região não permitida';code='401'");
					
					reader.close();
					reader = null;
					
					in.close();
					in = null;
				}
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
			e.disallow(Result.KICK_OTHER, ex.getMessage());
		}
	}
}
