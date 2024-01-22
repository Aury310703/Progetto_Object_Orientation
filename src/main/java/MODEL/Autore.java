package MODEL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Autore extends Utente {
    public Autore(String nome, String cognome, String login, String password, String email, Date data, Pagina pg) {
        super(nome, cognome, login, password, email, data);
        this.creazioni.add(pg);
    }

    public Autore(String nome, String cognome, String login, String password, String email, Date data, String titolo, LocalDateTime dataOra) {
        super(nome, cognome, login, password, email, data);
        Pagina pagina = new Pagina(titolo, dataOra, this);
        this.creazioni.add(pagina);
    }
    private ArrayList<ModificaProposta> valutate = new ArrayList<>();
    private ArrayList<Pagina> creazioni = new ArrayList<>();

    public ArrayList<ModificaProposta> getValutate() {
        return valutate;
    }

    public void setValutate(ArrayList<ModificaProposta> valutate) {
        this.valutate = valutate;
    }

    public void addValutate(ModificaProposta m){
        valutate.add(m);
    }

    public void removeValutate(ModificaProposta m){
        valutate.remove(m);
    }

    public ArrayList<Pagina> getCreazioni() {
        return creazioni;
    }

    public void setCreazioni(ArrayList<Pagina> creazioni) {
        this.creazioni = creazioni;
    }

    public void addCreazioni(Pagina p){
        creazioni.add(p);
    }

    public void rempveCreazioni(Pagina p){
        creazioni.remove(p);
    }
}
