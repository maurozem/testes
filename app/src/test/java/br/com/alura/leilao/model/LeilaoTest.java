package br.com.alura.leilao.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import br.com.alura.leilao.exception.LanceMenorQueUltimoLanceException;
import br.com.alura.leilao.exception.LanceSeguidoDoMesmoUsuarioException;
import br.com.alura.leilao.exception.UsuarioJaDeuCincoLancesException;

import static org.junit.Assert.*;

public class LeilaoTest {

    public static final double DELTA = 0.00001;
    private final Leilao console = new Leilao("Console");
    private final Usuario mauro = new Usuario("Mauro Zem");
    private final Lance lance200 = new Lance(mauro, 200.0);

    @Rule // para testes de exception
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getDescricao() {
        //criar cenario de teste
        Leilao console = new Leilao("Console");

        // executar açao esperada
        String descricao = console.getDescricao();

        // testar resultado esperado
        assertEquals("Console", descricao);
    }

    @Test
    public void getMaiorLance_quandoTemosApenasUmLance(){
        console.propoe(lance200);
        double maiorLanceConsole = console.getMaiorLance();
        assertEquals(200.0, maiorLanceConsole, 0.0001);
    }

    @Test
    public void deve_devolverMenorLance_quandoApenasUmLance(){
        console.propoe(lance200);
        double menorLance = console.getMenorLance();
        assertEquals(200.0, menorLance, 0.0001);
    }

    @Test
    public void getMaiorLance_quandoTemosLancesCrescentes(){
        console.propoe(new Lance(mauro, 1000.0));
        console.propoe(new Lance(new Usuario("Maria"), 2000.0));
        double maiorLanceComputador = console.getMaiorLance();
        assertEquals(2000.0, maiorLanceComputador, 0.0001);
    }

    @Test
    public void deve_devolverMenorLance_quandoTemosLancesCrescentes(){
        console.propoe(new Lance(mauro, 1000.0));
        console.propoe(new Lance(new Usuario("Mauricio"), 2000.0));
        double menorLance = console.getMenorLance();
        assertEquals(1000.0, menorLance, 0.0001);
    }

    @Test
    public void getMaiorLance_quandoTemosLancesDecrescentes(){
        console.propoe(new Lance(mauro, 21000.0));
        console.propoe(new Lance(new Usuario("Alex"), 22000.0));
        double maiorLanceCarro = console.getMaiorLance();
        assertEquals(22000.0, maiorLanceCarro, DELTA);
    }

    @Test
    public void deve_devolverTresMaioresLances_quandoRecebeTresLances() {
        console.propoe(lance200);
        console.propoe(new Lance(new Usuario("Elias"), 500));
        console.propoe(new Lance(new Usuario("Zem"), 600));
        console.propoe(new Lance(new Usuario("Helio"), 1000));

        List<Lance> lances = console.tresMaioresLances();
        assertEquals(3, lances.size());
        assertEquals(1000, lances.get(0).getValor(), DELTA);
        assertEquals(600, lances.get(1).getValor(), DELTA);
        assertEquals(500, lances.get(2).getValor(), DELTA);
    }

    @Test
    public void deve_devolverTresMaioresLances_quandoRecebeUmLances() {
        console.propoe(lance200);

        List<Lance> lances = console.tresMaioresLances();
        assertEquals(1, lances.size());
        assertEquals(200, lances.get(0).getValor(), DELTA);
    }

    @Test
    public void deve_devolverTresMaioresLances_quandoRecebeDoisLances() {
        console.propoe(lance200);
        console.propoe(new Lance(new Usuario("Jose"), 300));

        List<Lance> lances = console.tresMaioresLances();
        assertEquals(2, lances.size());
        assertEquals(300, lances.get(0).getValor(), DELTA);
        assertEquals(200, lances.get(1).getValor(), DELTA);
    }

    @Test
    public void deve_devolverTresMaioresLances_quandoNaoRecebeLances() {
        List<Lance> lances = console.tresMaioresLances();
        assertEquals(0, lances.size());
    }

    @Test
    public void deve_devolverValorZeroParaMaiorLance_quandoNaoTiverLances() {
        double maiorLance = console.getMaiorLance();
        assertEquals(0.0, maiorLance, DELTA);
    }

    @Test
    public void deve_devolverValorZeroParaMenorLance_quandoNaoTiverLances() {
        double lance = console.getMenorLance();
        assertEquals(0.0, lance, DELTA);
    }

    @Test
    public void naoDeve_aceitarLance_quandoLanceMenoQueMaiorLance() {
        exception.expect(LanceMenorQueUltimoLanceException.class);
        exception.expectMessage("lance menor que último lance");
        console.propoe(new Lance(mauro, 300));
        console.propoe(lance200);
    }

    @Test(expected = LanceSeguidoDoMesmoUsuarioException.class)
    public void naoDeve_adicionarLance_quandoMesmoUsuario() {
        console.propoe(new Lance(mauro, 500));
        console.propoe(new Lance(mauro, 600));
    }

    @Test(expected = UsuarioJaDeuCincoLancesException.class)
    public void naoDeve_adicionarLance_quandoMaisDe5LancesMesmoUsuario() {
        console.propoe(new Lance(mauro, 100));
        Usuario mauricio = new Usuario("Mauricio");
        console.propoe(new Lance(mauricio, 200));
        console.propoe(new Lance(mauro, 300));
        console.propoe(new Lance(mauricio, 400));
        console.propoe(new Lance(mauro, 500));
        console.propoe(new Lance(mauricio, 600));
        console.propoe(new Lance(mauro, 700));
        console.propoe(new Lance(mauricio, 800));
        console.propoe(new Lance(mauro, 900));
        console.propoe(new Lance(mauricio, 1000));
        console.propoe(new Lance(mauro, 1100));
    }

}