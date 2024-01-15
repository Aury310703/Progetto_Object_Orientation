package MODEL;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Frase_Corrente extends Frase {
    private int Numerazione;
    private Date dataInserimento;
    private Time oraInserimento;

    public Frase_Corrente(String s, int n, Pagina p, Date dataInserimento, Time oraInserimento) {
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

    public Date getDataInserimento() {
        return dataInserimento;
    }

    public Time getOraInserimento() {
        return oraInserimento;
    }

    public void setDataInserimento(Date dataInserimento) {
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


}
