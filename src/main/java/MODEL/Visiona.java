package MODEL;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Rappresenta la cronologia di visualizzazioni alle pagine effettuate dagli utenti.
 */
public class Visiona {
    private LocalDate dataVisione;
    private LocalTime oraVisione;
    private Pagina pagina;
    private Utente utente;

    /**
     * Istanzia una nuova visione.
     *
     * @param dataVisione indica la data di visione effettuata da un utente a una pagina
     * @param oraVisione  indica l'ora di visione effettuata dall'utente ad una pagina
     * @param pagina      indica la pagina visualizzata dall'utente
     * @param utente      indica l'utente che effettua la visualizzazione ad una pagina
     */
    public Visiona(LocalDate dataVisione, LocalTime oraVisione, Pagina pagina, Utente utente){
        this.utente = utente;
        this.pagina = pagina;
        this.dataVisione = dataVisione;
        this.oraVisione = oraVisione;
        utente.addPagineVisualizzate(this);
        pagina.addViaualizzatori(this);
    }

    /**
     * Restituisce la data di visione ad una pagina effettuata da un utente.
     *
     * @return dataVisione
     */
    public LocalDate getDataVisione() {
        return dataVisione;
    }

    /**
     * Setta la data di visione ad una pagina effettuata da un utente.
     *
     * @param dataVisione indica la data di visione effettuata da un utente a una pagina
     */
    public void setDataVisione(LocalDate dataVisione) {
        this.dataVisione = dataVisione;
    }

    /**
     * Restituisce l'ora di visione ad una pagina effettuata da un utente
     *
     * @return oraVisione
     */
    public LocalTime getOraVisione() {
        return oraVisione;
    }

    /**
     * Setta l'ora di visione ad una pagina effettuata da un utente.
     *
     * @param oraVisione indica l'ora di visione effettuata dall'utente ad una pagina
     */
    public void setOraVisione(LocalTime oraVisione) {
        this.oraVisione = oraVisione;
    }

    /**
     * Setta l'utente che andrà a visualizzare una determinata pagina.
     *
     * @param utente indicia l'utente che effettuerà la visualizzazione ad una pagina
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Restituisce l'utente che effettua la visualizzazione ad una pagina.
     *
     * @return utente
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Setta la pagina visualizzata da una utente.
     *
     * @param pagina indica la pagina che è stata visualizzata da un utente
     */
    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }

    /**
     * Restituisce una pagina visualizzata da un utente
     *
     * @return pagina
     */
    public Pagina getPagina() {
        return pagina;
    }
}
