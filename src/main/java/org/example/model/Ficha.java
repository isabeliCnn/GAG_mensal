package org.example.model;

import java.util.UUID;

public class Ficha {

    private UUID id;
    private int  pedidoId;
    private boolean usada;

    public Ficha(int pedidoId) {
        this.id       = UUID.randomUUID();
        this.pedidoId = pedidoId;
        this.usada    = false;
    }

    public Ficha(UUID id, int pedidoId, boolean usada) {
        this.id       = id;
        this.pedidoId = pedidoId;
        this.usada    = usada;
    }

    public void fechar() {
        if (this.usada) {
            System.out.println("Ficha já está fechada: " + id.toString().substring(0, 8));
            return;
        }
        this.usada = true;
    }

    public boolean isAberta() { return !usada; }


    public UUID getId()       { return id; }
    public int getPedidoId()  { return pedidoId; }
    public boolean isUsada()  { return usada; }

    @Override
    public String toString() {
        return String.format("Ficha [%s] | Pedido #%d | Status: %s",
                id.toString().substring(0, 8),
                pedidoId,
                usada ? "FECHADA" : "ABERTA");
    }
}