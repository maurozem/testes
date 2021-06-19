package br.com.alura.leilao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.alura.leilao.exception.LanceMenorQueUltimoLanceException;
import br.com.alura.leilao.exception.LanceSeguidoDoMesmoUsuarioException;
import br.com.alura.leilao.exception.UsuarioJaDeuCincoLancesException;

public class Leilao implements Serializable {

    private final String descricao;
    private final List<Lance> lances;
    private double maiorLance = 0.0;
    private double menorLance = 0.0;

    public Leilao(String descricao) {
        this.descricao = descricao;
        this.lances = new ArrayList<>();
    }

    public void propoe(Lance lance){
        validarLance(lance);
        lances.add(lance);
        if (primeiroLance(lance)) {
            return;
        }
        Collections.sort(lances);
        calcularMaiorLance(lance.getValor());
        calcularMenorLance(lance.getValor());
    }

    private void validarLance(Lance lance) {
        double valorLance = lance.getValor();
        if (lanceMenorQueLanceAnterior(valorLance))
            throw new LanceMenorQueUltimoLanceException("lance menor que último lance");
        if (temLances()) {
            if (usuarioIgualUsuarioUltimoLance(lance))
                throw new LanceSeguidoDoMesmoUsuarioException();
            if (usuarioDeuCincoLances(lance))
                throw new UsuarioJaDeuCincoLancesException();
        }
    }

    private boolean lanceMenorQueLanceAnterior(double valorLance) {
        if (maiorLance >= valorLance) {
            return true;
        }
        return false;
    }

    private boolean temLances() {
        return !lances.isEmpty();
    }

    private boolean usuarioDeuCincoLances(Lance lance) {
        int contador = 0;
        for (Lance l : lances) {
            if (l.getUsuario().equals(lance.getUsuario())) {
                contador++;
            }
            if (contador >= 5) {
                return true;
            }
        }
        return false;
    }

    private boolean usuarioIgualUsuarioUltimoLance(Lance lance) {
        if (lance.getUsuario().equals(lances.get(0).getUsuario())){
            return true;
        }
        return false;
    }

    private boolean primeiroLance(Lance lance) {
        if (lances.size() == 1){
            maiorLance = lance.getValor();
            menorLance = lance.getValor();
            return true;
        } else {
            return false;
        }
    }

    private void calcularMaiorLance(double valorLance) {
        if (valorLance > maiorLance) {
            maiorLance = valorLance;
        }
    }

    private void calcularMenorLance(double valorLance) {
        if (valorLance < menorLance) {
            menorLance = valorLance;
        }
    }

    public double getMenorLance() {
        return menorLance;
    }

    public double getMaiorLance() {
        return maiorLance;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Lance> tresMaioresLances() {
        int tamanho = lances.size() >= 3 ? 3 : lances.size();
        return lances.subList(0, tamanho);
    }

    public int quantidadeLances() {
        return lances.size();
    }
}
