package MODEL;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Frase_Corrente extends Frase {
    private LocalDate dataInserimento;
    private Time oraInserimento;
    private Pagina paginaCollegata = null;

    public Frase_Corrente(String s, int n, Pagina p, LocalDate dataInserimento, Time oraInserimento) {
        super(s);
        this.Numerazione = n;
        this.pagina = p;
        this.dataInserimento = dataInserimento;
        this.oraInserimento = oraInserimento;
        p.getFrasi().add(this);
    }
    private ArrayList<ModificaProposta> proposte = new ArrayList<>();
    private Pagina pagina;

    public int getNumerazione() {
        return Numerazione;
    }

    public void setNumerazione(int numerazione) {
        Numerazione = numerazione;
    }

    public LocalDate getDataInserimento() {
        return dataInserimento;
    }

    public Time getOraInserimento() {
        return oraInserimento;
    }

    public void setDataInserimento(LocalDate dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public void setOraInserimento(Time oraInserimento) {
        this.oraInserimento = oraInserimento;
    }

    @Override
    public Pagina getPagina() {
        return pagina;
    }

    @Override
    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }

    public ArrayList<ModificaProposta> getProposte() {
        return proposte;
    }

    public void setProposte(ArrayList<ModificaProposta> proposte) {
        this.proposte = proposte;
    }

    public void addProposte(ModificaProposta m){
        proposte.add(m);
    }

    public void removeProposta(ModificaProposta m){
        proposte.remove(m);
    }

    public Pagina getPaginaCollegata() {
        return paginaCollegata;
    }
    public void setPaginaCollegata(Pagina paginaCollegata) {
        this.paginaCollegata = paginaCollegata;
    }
}
