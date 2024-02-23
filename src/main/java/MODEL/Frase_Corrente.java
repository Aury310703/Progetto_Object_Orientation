package MODEL;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Frase_Corrente è una specializzazione di frase, rappresenta la frase nella versione originale del testo.
 */
public class Frase_Corrente extends Frase {
    private LocalDate dataInserimento;
    private Time oraInserimento;
    private Pagina paginaCollegata = null;

    /**
     * Istanzia una nuova Frase corrente .
     *
     * @param s               La stringa inserit dall'utente
     * @param n               Numerazione della frase
     * @param p               La pagina dove si trova la frase
     * @param dataInserimento data in cui la frase è stata inserita
     * @param oraInserimento  l'ora in cui la frase è stata inserita
     */
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

    /**
     * Restituisce la numerazione della frase.
     *
     * @return la numerazione
     */
    public int getNumerazione() {
        return Numerazione;
    }

    /**
     * Setta la numerazione della frase.
     *
     * @param numerazione indica la numerazione della frase
     */
    public void setNumerazione(int numerazione) {
        Numerazione = numerazione;
    }

    /**
     * Restituisce la data inserimento della frase.
     *
     * @return dataInserimento.
     */
    public LocalDate getDataInserimento() {
        return dataInserimento;
    }

    /**
     * Restituisce l'ora di quando viene inserita la frase.
     *
     * @return oraInserimento
     */
    public Time getOraInserimento() {
        return oraInserimento;
    }

    /**
     * Setta la data di quando viene inserita la frase.
     *
     * @param dataInserimento rappresenta la dayta di inserimento della frase
     */
    public void setDataInserimento(LocalDate dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    /**
     * Setyta l'ora di quando viene inserita la frase.
     *
     * @param oraInserimento Rappresenta l'ora di inserimento della frase
     */
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

    /**
     * Restituisce tutte le modifiche proposte dagli altri utenti su una determinata frase.
     *
     * @return proposte
     */
    public ArrayList<ModificaProposta> getProposte() {
        return proposte;
    }

    /**
     * Seta le modifiche che verranno proposte dall'utente su una determinata frase.
     *
     * @param proposte rappresenta l'ArrayList di modifiche proposte
     */
    public void setProposte(ArrayList<ModificaProposta> proposte) {
        this.proposte = proposte;
    }

    /**
     * Aggiunge all'ArrayList proposte una determinata modifica.
     *
     * @param m rappresenta la modifica da aggiungere all'ArrayList
     */
    public void addProposte(ModificaProposta m){
        proposte.add(m);
    }

    /**
     * Rimuove una determinata proposta dall'ArrayList.
     *
     * @param m indica la modifica da rimuovere dall'ArrayList
     */
    public void removeProposta(ModificaProposta m){
        proposte.remove(m);
    }

    /**
     * Restituisce la pagina collegata a una determinata frase.
     *
     * @return la pagina collegata
     */
    public Pagina getPaginaCollegata() {
        return paginaCollegata;
    }

    /**
     * Setta la pagina da collegare a una determinata frase.
     *
     * @param paginaCollegata indica la pagina che avrà il collegamento con la frase
     */
    public void setPaginaCollegata(Pagina paginaCollegata) {
        this.paginaCollegata = paginaCollegata;
    }
}
