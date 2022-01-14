package com.company.busbooking.server;

import com.company.busbooking.interfacce.ServizioAddetto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

public class ServizioAddettoProxyServer extends ProxyServer {
    private final ServizioAddetto servizio;

    public ServizioAddettoProxyServer(ServizioAddetto servizio) {
        this.servizio = servizio;
    }

    @Override
    public void inizializza(Javalin server) {
        server.post("/addetto/{idAddetto}/convalida", ctx -> {
            ctx.json(servizio.convalidaBiglietto(
                    Long.parseLong(ctx.pathParam("idAddetto")),
                    new ObjectMapper().readValue(ctx.formParam("codice"), byte[].class)
            ));
        });

        server.put("/addetto/{idAddetto}/corsa/{idCorsa}", ctx -> {
            ctx.json(servizio.aggiornaCorsa(
                    Long.parseLong(ctx.pathParam("idAddetto")),
                    Long.parseLong(ctx.pathParam("idCorsa"))
            ));
        });
    }
}
