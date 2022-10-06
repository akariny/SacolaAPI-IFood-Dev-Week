package me.dio.sacola.resource;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.resource.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ifood-devweek/sacolas")
@RequiredArgsConstructor
public class SacolaResource {
    private final SacolaService sacolaService;

    //metodo http post para incluir dados
    @PostMapping
    public Item incluirItemSacola( @RequestBody ItemDto itemDto) {
        return sacolaService.incluirItemSacola(itemDto);
    }

    //metodo http get que retorna os dados da sacola pelo id
    @GetMapping("/{id}")
    public Sacola verSacola (@PathVariable("id") Long id) {
        return sacolaService.verSacola(id);
    }

    //metodo http patch para atualizar dados do bd
    @PatchMapping("/fechar-sacola/{idSacola}")
    public Sacola fecharSacola(@PathVariable("idSacola") Long idSacola, @RequestParam("formaPagamento") int formaPagamento){
        return sacolaService.fecharSacola(idSacola, formaPagamento);
    }

    //incluir metodo de excluir item
}
