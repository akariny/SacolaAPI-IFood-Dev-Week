package me.dio.sacola.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.util.List;

@AllArgsConstructor //cria o metodo construtor como esta lá em baixo automaticamente
@Builder //ajuda na criação dos objetos
@Data //traz os getters e setters de todos os atributos
@Entity //vira uma tabela no banco de dados
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //para ignorar erros relacionados ao hibernate(Lazy)
@NoArgsConstructor //cria o construtor vazio
public class Sacola {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //forma que gera o id (auto incrementado)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)  //relacionamento entre tabelas (1 cliente - n sacolas)
    @JsonIgnore
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL) //relação de um para muitos (1 sacola - n itens)
    private List<Item> itens;
    private Double valorTotal;
    @Enumerated
    private FormaPagamento FormaPagamento;
    private boolean fechada;

    //construtor
    //um vazio pois o hibernate exige e um para todos os atributos.
    /*public Sacola() {
    }

    public Sacola(long id, Cliente cliente, List<Item> itens, Double valorTotal,
                  FormaPagamento formaPagamento, boolean fechada) {
        this.id = id;
        this.cliente = cliente;
        this.itens = itens;
        this.valorTotal = valorTotal;
        FormaPagamento = formaPagamento;
        this.fechada = fechada;
    }*/
}
