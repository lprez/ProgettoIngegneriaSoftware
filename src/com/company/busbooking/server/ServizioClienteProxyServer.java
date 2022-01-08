package com.company.busbooking.server;

import com.company.busbooking.interfacce.ServizioCliente;
import io.javalin.Javalin;

import java.util.Map;

public class ServizioClienteProxyServer extends ProxyServer {
    private final ServizioCliente servizio;

    public ServizioClienteProxyServer(ServizioCliente servizio) {
        this.servizio = servizio;
    }

    @Override
    public void inizializza(Javalin server) {
        server.get("/cliente/listacitta", (ctx) -> {
            ctx.json(servizio.richiediListaCitta());
        });

        server.get("/cliente/catalogobiglietti/{citta}", ctx -> {
            ctx.json(servizio.richiediCatalogoBiglietti(ctx.pathParam("citta")));
        });

        server.post("/cliente/acquisto", (ctx) -> {
            ctx.json(servizio.acquistaBiglietto(ctx.formParam("citta"), Long.parseLong(ctx.formParam("idDescrizione"))));
        });

        server.post("/cliente/pagamento", (ctx) -> {
            ctx.json(servizio.effettuaPagamento(
                    Long.parseLong(ctx.formParam("idCliente")),
                    Integer.parseInt(ctx.formParam("idAcquisto")),
                    Long.parseLong(ctx.formParam("idCarta")),
                    Integer.parseInt(ctx.formParam("codiceSicurezza"))
            ));
        });

        server.delete("/cliente/acquisto/{id}", (ctx) -> {
            servizio.annullaAcquisto(Integer.parseInt(ctx.pathParam("id")));
        });
    }
}
