CREATE OR REPLACE FUNCTION validar_estoque()
   RETURNS TRIGGER AS $$
   BEGIN
IF NEW.quantidade < 0 THEN
RAISE EXCEPTION 'Estoque não pode ser negativo. Valor recebido: %', NEW.quantidade;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validar_estoque
   BEFORE INSERT OR UPDATE ON produtos
   FOR EACH ROW EXECUTE FUNCTION validar_estoque();


CREATE OR REPLACE FUNCTION validar_ficha()
   RETURNS TRIGGER AS $$
   BEGIN
   IF OLD.usada = TRUE AND NEW.usada = TRUE THEN
    RAISE EXCEPTION 'Ficha % já está fechada. Reutilização negada.', OLD.id;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validar_ficha
    BEFORE UPDATE ON fichas
    FOR EACH ROW EXECUTE FUNCTION validar_ficha();


CREATE OR REPLACE FUNCTION atualizar_total_pedido()
RETURNS TRIGGER AS $$
BEGIN
UPDATE pedidos
SET total = (
    SELECT COALESCE(SUM(subtotal), 0)
    FROM itens_pedido
    WHERE pedido_id = NEW.pedido_id
)
WHERE id = NEW.pedido_id;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_total
    AFTER INSERT ON itens_pedido
    FOR EACH ROW EXECUTE FUNCTION atualizar_total_pedido();