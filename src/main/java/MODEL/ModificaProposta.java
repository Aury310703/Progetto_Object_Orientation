package MODEL;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ModificaProposta extends Frase {
    private int stato = 0;
    private LocalDate dataProposta;
    private LocalTime oraProposta;
    private LocalDate dataValutazione;
    private LocalTime oraValutazione;
    private Notifica notifica;
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
    }

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
    }
    private Autore autore;
    private Utente utente;
    private Frase_Corrente fraseCorrente;
    public int getStato() {
        return stato;
    }
    public void setStato(int stato) {
        this.stato = stato;
    }
    public LocalDate getDataProposta() {
        return dataProposta;
    }
    public void setDataProposta(LocalDate dataProposta) {
        this.dataProposta = dataProposta;
    }
    public LocalDate getDataValutazione() {
        return dataValutazione;
    }
    public void setDataValutazione(LocalDate dataValutazione) {
        this.dataValutazione = dataValutazione;
    }
    public LocalTime getOraProposta() {
        return oraProposta;
    }
    public void setOraProposta(LocalTime oraProposta) {
        this.oraProposta = oraProposta;
    }
    public LocalTime getOraValutazione() {
        return oraValutazione;
    }
    public void setOraValutazione(LocalTime oraValutazione) {
        this.oraValutazione = oraValutazione;
    }
    public Autore getAutore() {
        return autore;
    }
    public void setAutore(Autore autore) {
        this.autore = autore;
    }
    public Utente getUtente() {
        return utente;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    public Frase_Corrente getFraseCorrente() {
        return fraseCorrente;
    }
    public void setFraseCorrente(Frase_Corrente fraseCorrente) {
        this.fraseCorrente = fraseCorrente;
    }
    public Notifica getNotifica() {
        return notifica;
    }
    public void setNotifica(Notifica notifica) {
        this.notifica = notifica;
    }
}
