package com.company.busbooking.appcliente;

import com.company.busbooking.dto.DescrizioneBigliettoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import kong.unirest.GenericType;
import kong.unirest.Unirest;

import java.util.Collection;

public class ServizioClienteProxyClient implements ServizioCliente {
    private final String indirizzo;

    public ServizioClienteProxyClient(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @Override
    public Collection<String> richiediListaCitta() {
        return Unirest.get(indirizzo + "/cliente/listacitta").asJson().getBody().getArray().toList();
    }

    @Override
    public Collection<DescrizioneBigliettoDTO> richiediCatalogoBiglietti(String citta) {
        return Unirest.get(indirizzo + "/cliente/catalogobiglietti/{citta}")
                .routeParam("citta", citta)
                .asObject(new GenericType<Collection<DescrizioneBigliettoDTO>>(){})
                .getBody();
    }

    @Override
    public int acquistaBiglietto(String citta, long idDescrizione) {
        return Unirest.post(indirizzo + "/cliente/acquisto")
                .field("citta", citta)
                .field("idDescrizione", Long.toString(idDescrizione))
                .asObject(Integer.class)
                .getBody();
    }

    @Override
    public boolean effettuaPagamento(long idCliente, int idAcquisto, long idCartaCredito, int codiceSicurezza) {
        return Unirest.post(indirizzo + "/cliente/pagamento")
                .field("idCliente", Long.toString(idCliente))
                .field("idAcquisto", Integer.toString(idAcquisto))
                .field("idCarta", Long.toString(idCartaCredito))
                .field("codiceSicurezza", Integer.toString(codiceSicurezza))
                .asObject(Boolean.class)
                .getBody();
    }

    @Override
    public void annullaAcquisto(int idAcquisto) {
        Unirest.delete(indirizzo + "/cliente/acquisto/{id}").routeParam("id", Integer.toString(idAcquisto));
    }
}
