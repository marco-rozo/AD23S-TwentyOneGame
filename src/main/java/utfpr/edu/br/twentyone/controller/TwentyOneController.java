package utfpr.edu.br.twentyone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import utfpr.edu.br.twentyone.domain.Banca;
import utfpr.edu.br.twentyone.domain.Jogo;
import utfpr.edu.br.twentyone.services.BancaService;
import utfpr.edu.br.twentyone.services.JogoService;

@Controller
@RequestMapping
public class TwentyOneController {
    @Autowired
    private BancaService bancaService;

    @Autowired
    private JogoService jogoService;

    Banca banca = new Banca();
    Jogo jogo = new Jogo();

    @GetMapping("abrir-banca")
    public @ResponseBody
    String abreBanca() {
        Double valueInitial = 100.0;
        return bancaService.abrirBanca(valueInitial, banca);
    }

    @GetMapping("iniciar-jogo/{value}")
    public @ResponseBody
    String iniciaJogo(@PathVariable Double value) {
        return jogoService.iniciarJogo(value, banca, jogo);
    }

    @GetMapping("continuar")
    public @ResponseBody
    String continua() {
        return jogoService.continuarJogo(banca, jogo);
    }

    @GetMapping("parar")
    public @ResponseBody
    String para() {
        return jogoService.pararJogo(banca, jogo);
    }


    @GetMapping("fechar-banca")
    public @ResponseBody String fechaBanca() {
        return bancaService.fecharBanca(banca);
    }

}
