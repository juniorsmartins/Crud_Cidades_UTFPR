package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CidadeController 
{
    private final CidadeRepository cidadeRepository;

    public CidadeController(CidadeRepository cidadeRepository) 
    {this.cidadeRepository = cidadeRepository;}

    @GetMapping("/")
    public String listar(Model memoria) {

        memoria.addAttribute("listaCidades", cidadeRepository
                                                    .findAll()
                                                    .stream()
                                                    .map(cidadePersistenteAtual -> 
                                                            new Cidade(
                                                                cidadePersistenteAtual.getNome(),
                                                                cidadePersistenteAtual.getEstado()
                                                            ) 
                                                    ).collect(Collectors.toList()));

        return "/crud";
    }

    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria) {

        if (validacao.hasErrors()) {
            validacao
                .getFieldErrors()
                .forEach(error -> 
                        memoria.addAttribute(
                            error.getField(), 
                            error.getDefaultMessage())
                        );

                memoria.addAttribute("nomeInformado", cidade.getNome());
                memoria.addAttribute("estadoInformado", cidade.getEstado());
                memoria.addAttribute("listaCidades", cidadeRepository.findAll());
                
                return ("/crud");
        } else {
            var novaCidade = new CidadeEntidade();
            novaCidade.setNome(cidade.getNome());
            novaCidade.setEstado(cidade.getEstado());

            cidadeRepository.save(novaCidade);
        }

        return "redirect:/";
    }

    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome, 
            @RequestParam String estado) {
        
        var cidadeEstadoEncontrada = cidadeRepository.findByNomeAndEstado(nome, estado);

        cidadeEstadoEncontrada.ifPresent(cidadeRepository::delete);

        return "redirect:/";
    }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
        @RequestParam String nome, 
        @RequestParam String estado,
        Model memoria) {

            var cidadeAtual = cidadeRepository.findByNomeAndEstado(nome, estado);

            cidadeAtual.ifPresent(cidadeEncontrada -> {
                memoria.addAttribute("cidadeAtual", cidadeEncontrada);
                memoria.addAttribute("listaCidades", cidadeRepository.findAll());
            });

            return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
        @RequestParam String nomeAtual, 
        @RequestParam String estadoAtual,
        Cidade cidade) {

            var cidadeAtual = cidadeRepository.findByNomeAndEstado(nomeAtual, estadoAtual);

            if (cidadeAtual.isPresent()) 
            {    
                var cidadeEncontrada = cidadeAtual.get();
                cidadeEncontrada.setNome(cidade.getNome());
                cidadeEncontrada.setEstado(cidade.getEstado());

                cidadeRepository.saveAndFlush(cidadeEncontrada);
            }

            return "redirect:/";
    }
}











