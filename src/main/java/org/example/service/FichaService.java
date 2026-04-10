package org.example.service;

import org.example.model.Ficha;
import org.example.model.Pedido;
import org.example.repository.FichaRepository;

import java.util.ArrayList;
import java.util.UUID;

public class FichaService {

    private final FichaRepository fichaRepository;
    private final PedidoService   pedidoService;

    public FichaService(FichaRepository fichaRepository, PedidoService pedidoService) {
        this.fichaRepository = fichaRepository;
        this.pedidoService   = pedidoService;
    }

    public Ficha abrirFicha(int pedidoId) {
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        if (pedido == null) {
            System.out.println("Pedido #" + pedidoId + " não encontrado.");
            return null;
        }
        if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO
                || pedido.getStatus() == Pedido.StatusPedido.ENTREGUE) {
            System.out.println("Não é possível abrir ficha para pedido " + pedido.getStatus() + ".");
            return null;
        }

        Ficha ficha = new Ficha(pedidoId);
        fichaRepository.salvar(ficha);
        System.out.println("Ficha aberta: [" + ficha.getId().toString().substring(0, 8)
                + "] → Pedido #" + pedidoId);
        return ficha;
    }

    public boolean fecharFicha(UUID fichaId) {
        Ficha ficha = fichaRepository.buscarPorId(fichaId);
        if (ficha == null) {
            System.out.println("Ficha não encontrada: " + fichaId.toString().substring(0, 8));
            return false;
        }
        if (!ficha.isAberta()) {
            System.out.println("Ficha já está fechada: [" + fichaId.toString().substring(0, 8) + "]");
            return false;
        }

        Pedido pedido = pedidoService.buscarPorId(ficha.getPedidoId());
        if (pedido != null) {
            Pedido.StatusPedido s = pedido.getStatus();
            if (s == Pedido.StatusPedido.ABERTO || s == Pedido.StatusPedido.EM_PREPARO) {
                System.out.printf("ATENÇÃO: Pedido #%d ainda está %s. Fechar assim mesmo?%n",
                        pedido.getId(), s);
            }
            System.out.printf("TOTAL DA FICHA: R$ %.2f (Pedido #%d)%n",
                    pedido.calcularTotal(), pedido.getId());
        }

        boolean ok = fichaRepository.marcarUsada(fichaId);
        if (ok) System.out.println("Ficha [" + fichaId.toString().substring(0, 8) + "] FECHADA.");
        return ok;
    }

    public boolean fecharFichaParcial(String idParcial) {
        Ficha ficha = fichaRepository.buscarPorIdParcial(idParcial);
        if (ficha == null) {
            System.out.println("Ficha não encontrada: " + idParcial);
            return false;
        }
        return fecharFicha(ficha.getId());
    }

    public ArrayList<Ficha> listarAbertas() { return fichaRepository.listarAbertas(); }
    public ArrayList<Ficha> listarTodas()   { return fichaRepository.listarTodas(); }

    public Ficha buscarPorIdParcial(String idParcial) {
        return fichaRepository.buscarPorIdParcial(idParcial);
    }

    public void exibirFichasAbertas() {
        ArrayList<Ficha> abertas = listarAbertas();
        if (abertas.isEmpty()) { System.out.println("Nenhuma ficha aberta."); return; }
        System.out.println("═══════ FICHAS ABERTAS ═══════");
        for (Ficha f : abertas) System.out.println(f);
    }
}