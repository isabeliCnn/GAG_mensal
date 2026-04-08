package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class Ficha {

    private UUID id;
    private String nomeCliente;
    private ArrayList<Pedido> pedidos;
    private boolean aberta;
    private LocalDateTime abertura;
    private LocalDateTime fechamento;

    public Ficha(String nomeCliente) {
        this.id = UUID.randomUUID();
        this.nomeCliente = nomeCliente;
        this.pedidos = new ArrayList<>();
        this.aberta = true;
        this.abertura = LocalDateTime.now();
        this.fechamento = null;
    }

    public boolean adicionarPedido(Pedido pedido) {
        if (!aberta) {
            System.out.println("Ficha fechada! Não é possível adicionar pedidos para " + nomeCliente + ".");
            return false;
        }
        pedidos.add(pedido);
        return true;
    }

    public double calcularTotal() {
        double total = 0;
        for (Pedido pedido : pedidos) {
            if (pedido.getStatus() != Pedido.StatusPedido.CANCELADO) {
                total += pedido.calcularTotal();
            }
        }
        return total;
    }

    public void fechar() {
        this.aberta = false;
        this.fechamento = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getNomeCliente() { return nomeCliente; }
    public ArrayList<Pedido> getPedidos() { return pedidos; }
    public boolean isAberta() { return aberta; }
    public LocalDateTime getAbertura() { return abertura; }
    public LocalDateTime getFechamento() { return fechamento; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format(
                "Ficha [%s] | Cliente: %-15s | Status: %-7s | Pedidos: %d | Total: R$ %.2f",
                id.toString().substring(0, 8),
                nomeCliente,
                aberta ? "ABERTA" : "FECHADA",
                pedidos.size(),
                calcularTotal()
        );
    }
}