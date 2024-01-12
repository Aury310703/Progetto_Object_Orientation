package MODEL;

import java.sql.Date;
import java.sql.Time;

public class ModificaProposta extends Frase {
    private int stato = 0;
    private Date dataProposta;
    private Time oraProposta;
    private Date dataValutazione;
    private Time oraValutazione;
    public ModificaProposta(Date dP, Time oP, Autore a, Utente u, Frase_Corrente fc, String s){
        super(s);
        this.dataProposta = dP;
        this.oraProposta = oP;
        this.autore = a;
        this.utente = u;
        this.fraseCorrente = fc;
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

    public Date getDataProposta() {
        return dataProposta;
    }

    public void setDataProposta(Date dataProposta) {
        this.dataProposta = dataProposta;
    }

    public Date getDataValutazione() {
        return dataValutazione;
    }

    public void setDataValutazione(Date dataValutazione) {
        this.dataValutazione = dataValutazione;
    }

    public Time getOraProposta() {
        return oraProposta;
    }

    public void setOraProposta(Time oraProposta) {
        this.oraProposta = oraProposta;
    }

    public Time getOraValutazione() {
        return oraValutazione;
    }

    public void setOraValutazione(Time oraValutazione) {
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
