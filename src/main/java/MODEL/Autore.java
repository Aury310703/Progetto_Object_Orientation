package MODEL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * L'Autore è l'utente che ha creato almeno una pagina
 */
public class Autore extends Utente {
    /**
     * Istanzia un nuovo autore, con i suoi dati personali e l'aggiunta di una pagina esistente.
     *
     * @param nome     nome dell'autore
     * @param cognome  cognome dell'autore
     * @param login    nome utente dell'autore (ed è unico)
     * @param password password dell'autore
     * @param email    email dell'autore (ed è unica)
     * @param data     data di nascita dell'autore
     * @param pg       rappresenta la prima pagina creata dall'autore
     */
    public Autore(String nome, String cognome, String login, String password, String email, Date data, Pagina pg) {
        super(nome, cognome, login, password, email, data);
        this.creazioni.add(pg);
    }

    /**
     * Istanzia un nuovo autore,dopo che un utente ha creato la sua prima pagina, con i suoi dati personali, titolo e data della pagina che è stata creata .
     *
     * @param nome     nome dell'autore
     * @param cognome  cognome dell'autore
     * @param login    nome utente dell'autore (ed è unico)
     * @param password password dell'autore
     * @param email    email dell'autore (ed è unica)
     * @param data     data di nascita dell'autore
     * @param titolo   titolo della pagina appena creata
     * @param dataOra  data in cui è stata creata la pagina
     */
    public Autore(String nome, String cognome, String login, String password, String email, Date data, String titolo, LocalDateTime dataOra) {
        super(nome, cognome, login, password, email, data);
        Pagina pagina = new Pagina(titolo, dataOra, this);
        this.creazioni.add(pagina);
    }
    private ArrayList<ModificaProposta> valutate = new ArrayList<>();
    private ArrayList<Pagina> creazioni = new ArrayList<>();

    /**
     * restituisce un ArrayList ModificaProposta che contiene tutte le proposte di modifica valutate dall'autore, che sono state fatte sui testi da lui creati .
     *
     * @return ArrayList<ModificaProposta>
     */
    public ArrayList<ModificaProposta> getValutate() {
        return valutate;
    }

    /**
     * Setta l'ArrayList ModificaProposta valutate uguale ad un ArrayList passato come parametro.
     *
     * @param valutate Arraylist ModificaProposta che contiene tutte le proposte di modifica valutate dall'autore, che sono state fatte sui testi da lui creati .
     */
    public void setValutate(ArrayList<ModificaProposta> valutate) {
        this.valutate = valutate;
    }

    /**
     * Aggiunge all'ArrayList ModificaProposta valutate una nuova modifica proposta dall'utente o dall'autore stesso.
     *
     * @param modificaProposta Modifica proposta da aggingere all'ArrayList
     */
    public void addValutate(ModificaProposta modificaProposta){
        valutate.add(modificaProposta);
    }

    /**
     * Rimuove dall'ArrayList ModificaProposta valutate le proposte valiutate dall'autore.
     *
     * @param modificaProposta indica la modifica proposta da voler eliminare dall'ArrayList
     */
    public void removeValutate(ModificaProposta modificaProposta){
        valutate.remove(modificaProposta);
    }

    /**
     * Restituisce un ArrayList di pagine create dall'autore .
     *
     * @return ArrayList creazioni
     */
    public ArrayList<Pagina> getCreazioni() {
        return creazioni;
    }

    /**
     * Setta le pagine create dall'autore.
     *
     * @param creazioni ArrayList di pagine create dall'autore.
     */
    public void setCreazioni(ArrayList<Pagina> creazioni) {
        this.creazioni = creazioni;
    }

    /**
     * Aggiunge una pagina creata dall'autore all'ArrayList creazioni.
     *
     * @param pagina Indica la pagina da aggiungere all'ArrayList creazioni
     */
    public void addCreazioni(Pagina pagina){
        creazioni.add(pagina);
    }

    /**
     * Rimuove dall'ArrayList creazioni una pagina.
     *
     * @param pagina Indica la pagina da rimuovere dall'ArrayList creazioni
     */
    public void rempveCreazioni(Pagina pagina){
        creazioni.remove(pagina);
    }
}
