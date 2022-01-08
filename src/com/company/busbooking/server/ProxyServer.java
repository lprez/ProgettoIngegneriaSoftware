package com.company.busbooking.server;

import io.javalin.Javalin;

public abstract class ProxyServer {
    public abstract void inizializza(Javalin server);
}
