package MODEL;

import java.util.ArrayList;

public class Frase_Corrente extends Frase {
    private int Numerazione;

    public Frase_Corrente(String s, int n, Pagina p) {
        super(s);
        this.Numerazione = n;
        this.pagina = p;
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
