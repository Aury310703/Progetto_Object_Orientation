package MODEL;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Rappresenta la Notifica inviata all'autore di una pagina nel momento in cui viene proposta una modifica alle frasi correnti delle sue pagine da parte di un Utente/Autore.
 */
public class Notifica {
    private LocalDate dataInvio;
    private Timestamp oraInvio;
    private ModificaProposta modifica;
    private Autore autore;
    private String titolo;

    /**
     * Instanzia una nuova Notifica.
     *
     * @param autore           indica l'Autore che riceverà la notifica
     * @param modificaProposta indica la modifica proposta da un utente a cui è associata la notifica
     * @param titolo           indica il titolo della pagina su cui c'è stata una proposta di modifica
     */
    public Notifica(Autore autore, ModificaProposta modificaProposta, String titolo){
        this.modifica = modificaProposta;
        this.autore = autore;
        this.titolo = titolo;
        autore.addNotificheRicevute(this);
    }

    /**
     * Restituisce la data di invio della notifica.
     *
     * @return  dataInvio
     */
    public LocalDate getDataInvio() {
        return dataInvio;
    }

    /**
     * Setta la data di invio della notifica.
     *
     * @param dataInvio indica la data di invio della notifica
     */
    public void setDataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
    }

    /**
     * Restituisce  l'ora di invio della notifica.
     *
     * @return oraInvio
     */
    public Timestamp getOraInvio() {
        return oraInvio;
    }

    /**
     * Setta l'ora di invio della notifica.
     *
     * @param oraInvio indica l'ora di invio della notifica
     */
    public void setOraInvio(Timestamp oraInvio) {
        this.oraInvio = oraInvio;
    }

    /**
     * Restituisce  la modifica proposta associata alla notifica.
     *
     * @return modifica
     */
    public ModificaProposta getModifica() {return modifica;}

    /**
     * Setta la modifica proposta associata alla notifica.
     *
     * @param modifica indica la modifica proposta.
     */
    public void setModifica(ModificaProposta modifica) {this.modifica = modifica;}

    /**
     * Restituisce l'autore che riceverà la notifica.
     *
     * @return autore
     */
    public Autore getAutore() {
        return autore;
    }

    /**
     * Setta l'autore che riceverà la notifica.
     *
     * @param autore indica l'autore.
     */
    public void setAutore(Autore autore) {
        this.autore = autore;
    }

    /**
     * Restituisce il titolo della pagina su cui c'è stata una proposta di modifica.
     *
     * @return titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Setta il titolo della pagina su cui c'è stata una proposta di modifica..
     *
     * @param titolo indica il titolo della pagina
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}
