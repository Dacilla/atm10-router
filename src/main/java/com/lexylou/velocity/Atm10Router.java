package com.lexylou.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Optional;

import org.slf4j.Logger;

@Plugin(
    id = "atm10-router",
    name = "ATM10 Router",
    version = "1.5.0",
    description = "Routes clients to the correct backend based on protocol version",
    authors = {"Dacilla"}
)
public class Atm10Router {

    private static final int MC_26_2_PROTOCOL = 776;
    private static final int MC_1_21_1_PROTOCOL = 767;
    private static final int MC_1_20_1_PROTOCOL = 763;

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public Atm10Router(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        String username = player.getUsername();
        int protocol = player.getProtocolVersion().getProtocol();

        if (protocol == MC_1_21_1_PROTOCOL) {
            logger.info("{} routed to ATM10 (protocol {})", username, protocol);
            routeToServer(event, "atm10");
            return;
        }

        if (protocol == MC_1_20_1_PROTOCOL) {
            logger.info("{} routed to Liminal Industries (protocol {})", username, protocol);
            routeToServer(event, "liminal");
            return;
        }

        if (protocol == MC_26_2_PROTOCOL) {
            logger.info("{} routed to vanilla (protocol 26.2)", username);
            routeToServer(event, "vanilla");
            return;
        }

        logger.info("{} routed to vanilla (protocol {})", username, protocol);
    }

    private void routeToServer(PlayerChooseInitialServerEvent event, String serverName) {
        Optional<RegisteredServer> target = server.getServer(serverName);
        if (target.isPresent()) {
            event.setInitialServer(target.get());
        } else {
            logger.warn("Server '{}' not registered in Velocity!", serverName);
        }
    }
}
