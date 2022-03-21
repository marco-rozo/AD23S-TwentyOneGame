package utfpr.edu.br.twentyone.services;

import org.springframework.stereotype.Service;
import utfpr.edu.br.twentyone.domain.Banca;

import javax.management.InvalidAttributeValueException;

@Service
public class BancaService {

    public String abrirBanca(Double value, Banca banca) {
        StringBuilder message = new StringBuilder();
        try {
            if (value < 0) {
                throw new InvalidAttributeValueException("Valor precisa ser positivo");
            }

            value = 20.0;
            banca.setSaldo(value);
            message.append("Banca aberta com o valor inicial de " + value);
        } catch (InvalidAttributeValueException e) {
            message.append(e.getMessage());
        }

        return message.toString();
    }

    public String fecharBanca(Banca banca) {
        StringBuilder message = new StringBuilder();
        try {
            Double saldo = banca.getSaldo();
            banca.setSaldo(0.0); //zera a banca
            message.append("\nBanca fechada, valor final: " + saldo);
        } catch (Exception e) {
            message.append(e.getMessage());
        }

        return message.toString();
    }
}
