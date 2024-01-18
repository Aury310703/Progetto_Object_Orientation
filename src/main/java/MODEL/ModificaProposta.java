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
    public ModificaProposta(LocalDate dP, LocalTime oP, Autore a, Utente u, Frase_Corrente fc, String s, int numerazione){
        super(s);
        this.dataProposta = dP;
        this.oraProposta = oP;
        this.autore = a;
        this.utente = u;
        this.fraseCorrente = fc;
        this.Numerazione = numerazione;
        a.getValutate().add(this);
        u.frasiProposte.add(this);
        fc.getProposte().add(this);
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
}
