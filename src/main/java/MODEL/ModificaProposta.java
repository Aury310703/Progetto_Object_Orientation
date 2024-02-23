package MODEL;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Modifica proposta è una specializzazione di Frase e rappresenta le modifiche proposte dai vari utenti alle frasi correnti (Se l'autore della pagina si propone delle modifiche a una delle sue pagine la modifica verrà direttamente accettata).
 */
public class ModificaProposta extends Frase {
    private int stato = 0;
    private LocalDate dataProposta;
    private LocalTime oraProposta;
    private LocalDate dataValutazione;
    private LocalTime oraValutazione;
    private Notifica notifica;

    /**
     * Istanzia una Modifica proposta.
     *
     * @param dataProposta    indica la data in cui viene proposta la modifica
     * @param oraProposta     indica l'ora in cui viene proposta la modifica
     * @param autore          indica l'autore della pagina in cui viene proposta la modifica
     * @param utente          indica utente che propone la modifica a una pagina
     * @param fraseCorrente   indica la frase corrente a cui viene proposta la modifica
     * @param stringaProposta indica la frase proposta come modifica a una frase corrente
     * @param numerazione     indica la numerazione della frase corrente
     */
    public ModificaProposta(LocalDate dataProposta, LocalTime oraProposta, Autore autore, Utente utente, Frase_Corrente fraseCorrente, String stringaProposta, int numerazione){
        super(stringaProposta);
        this.dataProposta = dataProposta;
        this.oraProposta = oraProposta;
        this.autore = autore;
        this.utente = utente;
        this.fraseCorrente = fraseCorrente;
        this.Numerazione = numerazione;
        autore.getValutate().add(this);
        utente.frasiProposte.add(this);
        fraseCorrente.getProposte().add(this);
        notifica = new Notifica(autore, this, fraseCorrente.getPagina().getTitolo());
    }

    /**
     * Istanzia una Modifica proposta indicando anche lo stato.
     *
     * @param dataProposta    indica la data in cui viene proposta la modifica
     * @param oraProposta     indica l'ora in cui viene proposta la modifica
     * @param autore          indica l'autore della pagina in cui viene proposta la modifica
     * @param utente          indica utente che propone la modifica a una pagina
     * @param fraseCorrente   indica la frase corrente a cui viene proposta la modifica
     * @param stringaProposta indica la frase proposta come modifica a una frase corrente
     * @param numerazione     indica la numerazione della frase corrente
     * @param stato           indica lo stato di accettazione della modifica (-1 rifiutata, 1 accettata, 0 in attesa)
     */
    public ModificaProposta(LocalDate dataProposta, LocalTime oraProposta, Autore autore, Utente utente, Frase_Corrente fraseCorrente, String stringaProposta, int numerazione, int stato){
        super(stringaProposta);
        this.dataProposta = dataProposta;
        this.oraProposta = oraProposta;
        this.autore = autore;
        this.utente = utente;
        this.fraseCorrente = fraseCorrente;
        this.Numerazione = numerazione;
        this.stato = stato;
        autore.getValutate().add(this);
        utente.frasiProposte.add(this);
        fraseCorrente.getProposte().add(this);
        if(stato == 0)
            notifica = new Notifica(autore, this, fraseCorrente.getPagina().getTitolo());
    }

    private Autore autore;
    private Utente utente;
    private Frase_Corrente fraseCorrente;

    /**
     * Restituisce lo stato di accettazione della frase.
     *
     * @return stato
     */
    public int getStato() {
        return stato;
    }

    /**
     * Setta lo stato di accettazione della frase.
     *
     * @param stato indica lo stato di accettazione da settare per una modifica
     */
    public void setStato(int stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la data proposta della modifica.
     *
     * @return dataProposta
     */
    public LocalDate getDataProposta() {
        return dataProposta;
    }

    /**
     * Setta la data di quando viene proposta la modifica da parte di un utente sulla frase.
     *
     * @param dataProposta indica la data di proposta della modifica
     */
    public void setDataProposta(LocalDate dataProposta) {
        this.dataProposta = dataProposta;
    }

    /**
     * Restituisce la data di quando l'autore valuta la modifica proposta da un utente.
     *
     * @return dataValutazione
     */
    public LocalDate getDataValutazione() {
        return dataValutazione;
    }

    /**
     * Setta la data di quando l'autore valuta la modifica proposta da un utente.
     *
     * @param dataValutazione indica la data di valutazione
     */
    public void setDataValutazione(LocalDate dataValutazione) {
        this.dataValutazione = dataValutazione;
    }

    /**
     * Restituisce l'ora di quando viene proposta la modifica da parte di un utente sulla frase.
     *
     * @return oraProposta
     */
    public LocalTime getOraProposta() {
        return oraProposta;
    }

    /**
     * Setta l'ora di quando viene proposta la modifica da parte di un utente sulla frase.
     *
     * @param oraProposta indica l'ora di proposta della modifica.
     */
    public void setOraProposta(LocalTime oraProposta) {
        this.oraProposta = oraProposta;
    }

    /**
     * Restituisce l'ora di quando l'autore valuta la modifica proposta da un utente.
     *
     * @return oraValutazione
     */
    public LocalTime getOraValutazione() {
        return oraValutazione;
    }

    /**
     * Setta l'ora di quando l'autore valuta la modifica proposta da un utente.
     *
     * @param oraValutazione indica l'ora di valutazione.
     */
    public void setOraValutazione(LocalTime oraValutazione) {
        this.oraValutazione = oraValutazione;
    }

    /**
     * Restituisce l'autore della pagina in cui è stata proposta la modifica.
     *
     * @return autore
     */
    public Autore getAutore() {
        return autore;
    }

    /**
     * Setta l'autore della pagina in cui è stata proposta la modifica.
     *
     * @param autore indica l'autore della pagina.
     */
    public void setAutore(Autore autore) {
        this.autore = autore;
    }

    /**
     * Restituisce l'utente che ha proposto la modifica alla pagina.
     *
     * @return utente.
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Setta l'utente che propone la modifica alla pagina.
     *
     * @param utente indica l'utente che propone la modifica.
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Indica la frase corrente su cui è stata proposta la modfica dall'utente.
     *
     * @return fraseCorrente
     */
    public Frase_Corrente getFraseCorrente() {
        return fraseCorrente;
    }

    /**
     * Setta la frase corrente su cui è stata proposta la modfica dall'utente.
     *
     * @param fraseCorrente indica la fraseCorrente.
     */
    public void setFraseCorrente(Frase_Corrente fraseCorrente) {
        this.fraseCorrente = fraseCorrente;
    }

    /**
     * Restituisce la notifica collegata alla modifica proposta.
     *
     * @return notifica
     */
    public Notifica getNotifica() {
        return notifica;
    }

    /**
     * Setta la notifica collegata alla modifica proposta.
     *
     * @param notifica indica la notifica
     */
    public void setNotifica(Notifica notifica) {
        this.notifica = notifica;
    }
}
