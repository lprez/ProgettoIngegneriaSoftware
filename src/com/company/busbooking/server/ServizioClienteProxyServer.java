package com.company.busbooking.server;

import com.company.busbooking.interfacce.ServizioCliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

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

        server.get("/cliente/{idCliente}/biglietti", ctx -> {
            ctx.json(servizio.richiediListaBiglietti(Long.parseLong(ctx.pathParam("idCliente"))));
        });

        server.get("/cliente/{idCliente}/biglietti/{indiceBiglietto}", ctx -> {
            ctx.result(new ObjectMapper().writeValueAsString(servizio.richiediCodice(
                    Long.parseLong(ctx.pathParam("idCliente")),
                    Integer.parseInt(ctx.pathParam("indiceBiglietto"))
            )));
        });

        server.post("/cliente/preferito", (ctx) -> {
            ctx.json(servizio.creaPreferito(
                    Long.parseLong(ctx.formParam("idCliente")),
                    ctx.formParam("citta"),
                    Integer.parseInt(ctx.formParam("idAcquisto")),
                    Long.parseLong(ctx.formParam("idCarta"))
            ));
        });

        server.post("/cliente/carta", (ctx) -> {
            ctx.json(servizio.creaCarta(
                    Long.parseLong(ctx.formParam("idCliente")),
                    ctx.formParam("codice"),
                    ctx.formParam("intestatario"),
                    ctx.formParam("scadenza")
            ));
        });

        server.get("/cliente/{idCliente}/preferiti", ctx -> {
            ctx.json(servizio.richiediListaPreferiti(Long.parseLong(ctx.pathParam("idCliente"))));
        });

        server.delete("/cliente/{idCliente}/preferito/{idPreferito}", (ctx) -> {
            ctx.json(servizio.eliminaPreferito(
                    Long.parseLong(ctx.pathParam("idCliente")),
                    Integer.parseInt(ctx.pathParam("idPreferito"))
            ));
        });

        server.get("/cliente/{idCliente}/movimenti", ctx -> {
            ctx.json(servizio.richiediElencoMovimenti(Long.parseLong(ctx.pathParam("idCliente"))));
        });

        server.get("/cliente/{idCliente}/carte", ctx -> {
            ctx.json(servizio.richiediListaCarte(Long.parseLong(ctx.pathParam("idCliente"))));
        });

        server.put("/cliente/carta", (ctx) -> {
            ctx.json(servizio.modificaCarta(
                    Long.parseLong(ctx.formParam("idCliente")),
                    Long.parseLong(ctx.formParam("idCarta")),
                    ctx.formParam("codice"),
                    ctx.formParam("intestatario"),
                    ctx.formParam("scadenza")
            ));
        });

        server.delete("/cliente/{idCliente}/carta/{idCarta}", (ctx) -> {
            ctx.json(servizio.eliminaCarta(
                    Long.parseLong(ctx.pathParam("idCliente")),
                    Long.parseLong(ctx.pathParam("idCarta"))
            ));
        });
    }
}
