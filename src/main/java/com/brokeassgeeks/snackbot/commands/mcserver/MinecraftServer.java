package com.brokeassgeeks.snackbot.commands.mcserver;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class MinecraftServer {
    private String name;
    private String pack;
    private String version;
    private String host;
    private int port;
    private LastActivity lastactivity;

    @Data
    public class LastActivity {
        private String user;
        private long time;
        public LastActivity() {
            this.user ="";
            this.time = 0;
        }

    }

    public MinecraftServer() {
        this.name = "";
        this.pack = "";
        this.version = "";
        this.host = "";
        this.port = 0;
        this.lastactivity = new LastActivity();
    }

    public InetSocketAddress getHost() { return new InetSocketAddress(this.host,this.port); }

    public boolean isOnServer(String player) {
        StatusResponse response =  new ServerConnection(this).getResponse();
        for (StatusResponse.Players.Player p : response.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(player))
                return true;
        }
        return false;
    }

    public void updateActivity(String user, long time) {
        this.lastactivity.time = time;
        this.lastactivity.user = user;
    }
}