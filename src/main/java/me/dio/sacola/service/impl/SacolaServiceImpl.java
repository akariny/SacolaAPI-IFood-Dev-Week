package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.ItemRepository;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.resource.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemRepository itemRepository;

    //logicas dos metodos referente a sacola para manipular o banco de dados

    //incluir dados na sacola
    @Override
    public Item incluirItemSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getSacolaId());

        if (sacola.isFechada()) {
            throw new RuntimeException("Esta sacola está fechada.");
        }

        Item itemInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe");
                        }
                ))
                .build();

        List<Item> itensSacola = sacola.getItens();
        if (itensSacola.isEmpty()) {
            itensSacola.add(itemInserido);
        } else {
            Restaurante restauranteAtual = itensSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteItemInserido = itemInserido.getProduto().getRestaurante();

            if (restauranteAtual.equals(restauranteItemInserido)) {
                itensSacola.add(itemInserido);
            } else {
                throw new RuntimeException("Não é possivel adicionar itens de restaurantes diferentes! Conclua a compra ou esvazie a sacola.");
            }
        }

        List<Double> valorItens = new ArrayList<>();

        for (Item itemSacola : itensSacola) {
            double valorTotalItens =
                    itemSacola.getProduto().getValorUnitario() * itemSacola.getQuantidade();
            valorItens.add(valorTotalItens);
        }

       double valorTotalSacola = valorItens.stream()
                       .mapToDouble(valorTotalItens -> valorTotalItens)
                       .sum();

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        return itemInserido;
    }

    //retornar dados da sacola
    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe");
                }
        );
    }

    //atualizar sacola
    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
        Sacola sacola = verSacola(id);

        if (sacola.getItens().isEmpty()){
            throw new RuntimeException("Inclua itens na sacola");
        }
        /*
        if (numeroFormaPagamento == 0){
            sacola.setFormaPagamento(FormaPagamento.DINHEIRO);
        } else {
            sacola.setFormaPagamento(FormaPagamento.MAQUINETA);
        }
        */

        FormaPagamento formaPagamento =
                numeroFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;

        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }

    //excluir item da sacola
}
