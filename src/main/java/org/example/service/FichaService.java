package org.example.service;

import org.example.model.Ficha;
import org.example.model.Pedido;
import org.example.repository.FichaRepository;

import java.util.List;
import java.util.UUID;

public class FichaService {

    private final FichaRepository fichaRepository;
    private final PedidoService pedidoService;

    public FichaService(FichaRepository fichaRepository, PedidoService pedidoService) {
        this.fichaRepository = fichaRepository;
        this.pedidoService = pedidoService;
    }

    public Ficha abrirFicha(int pedidoId) {

        Pedido pedido = pedidoService.buscarPorId(pedidoId);

        if (pedido == null) {
            System.out.println("Pedido #" + pedidoId + " não encontrado.");
            return null;
        }

        if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO
                || pedido.getStatus() == Pedido.StatusPedido.ENTREGUE) {

            System.out.println("Não é possível abrir ficha para pedido " + pedido.getStatus());
            return null;
        }

        // ✅ AGORA CORRETO (usa objeto, não ID)
        Ficha ficha = new Ficha(pedido);

        fichaRepository.salvar(ficha);

        System.out.println("Ficha aberta: [" +
                ficha.getId().toString().substring(0, 8) +
                "] → Pedido #" + pedido.getId());

        return ficha;
    }

    public boolean fecharFicha(UUID fichaId) {

        Ficha ficha = fichaRepository.buscarPorId(fichaId);

        if (ficha == null) {
            System.out.println("Ficha não encontrada.");
            return false;
        }

        if (!ficha.isAberta()) {
            System.out.println("Ficha já está fechada.");
            return false;
        }

        // ✅ AGORA CORRETO
        Pedido pedido = ficha.getPedido();

        if (pedido != null) {

            Pedido.StatusPedido s = pedido.getStatus();

            if (s == Pedido.StatusPedido.ABERTO || s == Pedido.StatusPedido.EM_PREPARO) {
                System.out.printf("ATENÇÃO: Pedido #%d ainda está %s%n",
                        pedido.getId(), s);
            }

            System.out.printf("TOTAL DA FICHA: R$ %.2f (Pedido #%d)%n",
                    pedido.calcularTotal(), pedido.getId());
        }

        boolean ok = fichaRepository.marcarUsada(fichaId);

        if (ok) {
            System.out.println("Ficha fechada!");
        }

        return ok;
    }

    public List<Ficha> listarAbertas() {
        return fichaRepository.listarAbertas();
    }

    public List<Ficha> listarTodas() {
        return fichaRepository.listarTodas();
    }

    public void exibirFichasAbertas() {

        List<Ficha> abertas = listarAbertas();

        if (abertas.isEmpty()) {
            System.out.println("Nenhuma ficha aberta.");
            return;
        }

        System.out.println("═══════ FICHAS ABERTAS ═══════");

        for (Ficha f : abertas) {
            System.out.println(f);
        }
    }
}