package utfpr.edu.br.twentyone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utfpr.edu.br.twentyone.Utils.RandomNumber;
import utfpr.edu.br.twentyone.domain.Banca;
import utfpr.edu.br.twentyone.domain.Jogo;

import javax.management.InvalidAttributeValueException;
import java.util.Objects;

@Service
public class JogoService {
    @Autowired
    private BancaService bancaService;

    private Double multiploGanhos = 1.0;
    private boolean isFecharBanca = false;

    public String iniciarJogo(Double value, Banca banca, Jogo jogo) {
        StringBuilder message = new StringBuilder();
        try {
            System.out.println(banca.getSaldo());
            if (value < 0) {
                throw new InvalidAttributeValueException("Valor precisa ser positivo");
            }
            if (banca.getSaldo() == 0.0) {
                throw new InvalidAttributeValueException("Você precisa iniciar a banca");
            }
            if (value > banca.getSaldo()) {
                throw new InvalidAttributeValueException("Valor não pode ser maior que a banca");
            }
            if (Objects.equals(banca.getSaldo(), value)) {
                isFecharBanca = true;
            }

            jogo.setSaldo(value);
            Integer random = RandomNumber.generateRandomNumber();
            jogo.setPontos(random);

            message.append(" O jogo foi iniciado com o valor de " + value +
                    "\n"+" Valor sorteado:" + random +
                    "\n"+" Pontos atuais:" + jogo.getPontos());
            message.append(" Banca atualizada para:" + banca.getSaldo() + "\n");

        } catch (InvalidAttributeValueException e) {
            message.append(e.getMessage());
        }

        return message.toString();
    }

    public String continuarJogo(Banca banca, Jogo jogo) {
        StringBuilder message = new StringBuilder();

        try {
            if (jogo.getSaldo() <= 0){
                throw new InvalidAttributeValueException("Valor de aposta do jogo precisa ser maior que zero");
            }
            Integer pontosAtuais = jogo.getPontos();
            Integer random = RandomNumber.generateRandomNumber(); //mais uma carta

            Integer novosPontos = pontosAtuais + random;
            jogo.setPontos(novosPontos); //seta os novos pontos somados

            message.append(" Novo numero sorteado" + random + "\n");
            message.append(" Seus novos pontos" + jogo.getPontos() + ".\n");

            if (jogo.getPontos() > 21){
                message.append(" Ops, você perdeu!" + "\n");
                banca.setSaldo(banca.getSaldo() - jogo.getSaldo()); //desconta da banca inicial o apostado
                message.append(" Banca atualizada para:" + banca.getSaldo() + "\n");
                this.finalizarJogo(jogo, banca); //para zerar os valores apostados e pontos somados
                multiploGanhos = 1.0;
            }

            if (jogo.getPontos() >= 18 && jogo.getPontos() <= 20){
                message.append(" Seus pontos estão entre 18 e 20, se continuar seus ganhos aumentam para 2.5x " + "\n");
                multiploGanhos = 2.5;
            }

            if (jogo.getPontos() == 21){
                message.append(" Parabéns, você ganhou!" + "\n");
                //multiplica os ganhos pelo multiplo definido (1.0 ou 2.5)  e soma ao saldo da banca
                banca.setSaldo(banca.getSaldo() + (jogo.getSaldo() * multiploGanhos));
                message.append(" Banca atualizada para:" + banca.getSaldo() + "\n");
                this.finalizarJogo(jogo, banca); //zera os valores apostados e pontos somados
            }
        } catch (InvalidAttributeValueException e){
            message.append(e.getMessage());
        }

        return message.toString();
    }

    public String pararJogo(Banca banca, Jogo jogo) {
        StringBuilder message = new StringBuilder();

        try {
            if (jogo.getSaldo() <= 0){
                throw new InvalidAttributeValueException("Valor de aposta do jogo precisa ser maior que zero");
            }

            Integer pontosAtuais = jogo.getPontos();
            Integer random = RandomNumber.generateRandomNumber(); //mais uma carta

            Integer novosPontos = pontosAtuais + random;
            jogo.setPontos(novosPontos); //seta os novos pontos somados

            message.append("Novo numero sorteado" + random + "\n");
            message.append("Seus novos pontos" + jogo.getPontos() + ".\n");

            if (jogo.getPontos() > 21){
                message.append("Ops, você perdeu!" + "\n");
                banca.setSaldo(banca.getSaldo() - jogo.getSaldo()); //desconta da banca inicial o apostado
                message.append(" Banca atualizada para:" + banca.getSaldo() + "\n");
                this.finalizarJogo(jogo, banca); //para zerar os valores apostados e pontos somados
                multiploGanhos = 1.0;
            } else{
                message.append("Parabéns, você ganhou!" + "\n");
                //multiplica os ganhos pelo multiplo definido (1.0 ou 2.5)  e soma ao saldo da banca
                banca.setSaldo(banca.getSaldo() + (jogo.getSaldo() * multiploGanhos));
                message.append("Banca atualizada para:" + banca.getSaldo() + "\n");

                this.finalizarJogo(jogo, banca); //zera os valores apostados e pontos somados
            }
        } catch (InvalidAttributeValueException e){
            message.append(e.getMessage());
        }

        return message.toString();
    }

    public void finalizarJogo(Jogo jogo, Banca banca) {
        System.out.println(isFecharBanca);
        jogo.setPontos(0);
        jogo.setSaldo(0.0);
        if (isFecharBanca){
            bancaService.fecharBanca(banca);
        }
    }
}
