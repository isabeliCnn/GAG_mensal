package org.example.service;

import org.example.model.Ficha;
import org.example.model.Pedido;

import java.util.ArrayList;
import java.util.UUID;

public class FichaService {

    private ArrayList<Ficha> fichas;

    public FichaService() {
        this.fichas = new ArrayList<>();
    }

    public Ficha abrirFicha(String nomeCliente) {
        for (Ficha f : fichas) {
            if (f.getNomeCliente().equalsIgnoreCase(nomeCliente) && f.isAberta()) {
                System.out.println("Já existe ficha aberta para: " + nomeCliente
                        + " [ID: " + f.getId().toString().substring(0, 8) + "]");
                return f;
            }
        }

        Ficha ficha = new Ficha(nomeCliente);
        fichas.add(ficha);
        System.out.printf("Ficha aberta | Cliente: %-15s | ID: %s%n",
                nomeCliente, ficha.getId().toString().substring(0, 8));
        return ficha;
    }

    public boolean adicionarPedido(Ficha ficha, Pedido pedido) {
        boolean sucesso = ficha.adicionarPedido(pedido);
        if (sucesso) {
            System.out.printf("Pedido [%s] adicionado à ficha de %s%n",
                    pedido.getId().toString().substring(0, 8), ficha.getNomeCliente());
        }
        return sucesso;
    }

    public void fecharFicha(Ficha ficha) {
        int pendentes = 0;
        for (Pedido p : ficha.getPedidos()) {
            Pedido.StatusPedido s = p.getStatus();
            if (s == Pedido.StatusPedido.PENDENTE || s == Pedido.StatusPedido.PREPARANDO) {
                pendentes++;
            }
        }

        if (pendentes > 0) {
            System.out.printf("Atenção: %d pedido(s) ainda não foram entregues na ficha de %s.%n",
                    pendentes, ficha.getNomeCliente());
        }

        ficha.fechar();
        System.out.printf("Ficha FECHADA | Cliente: %s | Total a pagar: R$ %.2f%n",
                ficha.getNomeCliente(), ficha.calcularTotal());
    }

    public Ficha buscarPorId(UUID id) {
        for (Ficha f : fichas) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        System.out.println("Ficha não encontrada para o ID: " + id);
        return null;
    }

    public Ficha buscarPorIdParcial(String idParcial) {
        for (Ficha f : fichas) {
            if (f.getId().toString().startsWith(idParcial)) {
                return f;
            }
        }
        System.out.println("Ficha não encontrada para: " + idParcial);
        return null;
    }

    public Ficha buscarAbertaPorNome(String nomeCliente) {
        for (Ficha f : fichas) {
            if (f.getNomeCliente().equalsIgnoreCase(nomeCliente) && f.isAberta()) {
                return f;
            }
        }
        System.out.println("Nenhuma ficha aberta para: " + nomeCliente);
        return null;
    }

    public ArrayList<Ficha> listarAbertas() {
        ArrayList<Ficha> abertas = new ArrayList<>();
        for (Ficha f : fichas) {
            if (f.isAberta()) {
                abertas.add(f);
            }
        }
        return abertas;
    }

    public ArrayList<Ficha> getTodas() {
        return fichas;
    }

    public void exibirResumo(Ficha ficha) {
        System.out.println("════════════════════════════════════");
        System.out.println("         RESUMO DA FICHA           ");
        System.out.println("════════════════════════════════════");
        System.out.println(ficha);
        System.out.println("────────────────────────────────────");
        if (ficha.getPedidos().isEmpty()) {
            System.out.println("  Nenhum pedido registrado.");
        } else {
            for (Pedido p : ficha.getPedidos()) {
                System.out.println(p);
                System.out.println("  ···································");
            }
        }
        System.out.printf("TOTAL A PAGAR: R$ %.2f%n", ficha.calcularTotal());
    }

    public void exibirFichasAbertas() {
        ArrayList<Ficha> abertas = listarAbertas();
        if (abertas.isEmpty()) {
            System.out.println("Nenhuma ficha aberta no momento.");
            return;
        }
        System.out.println("═══════ FICHAS ABERTAS ═══════");
        for (Ficha f : abertas) {
            System.out.println(f);
        }
    }
}