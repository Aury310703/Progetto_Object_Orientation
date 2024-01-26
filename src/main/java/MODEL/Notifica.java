package MODEL;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class Notifica {
    private LocalDate dataInvio;
    private Timestamp oraInvio;
    private String messaggio;
    private ModificaProposta modifica;
    private Autore autore;
    private String titolo;

    public Notifica(Autore autore, ModificaProposta modificaProposta, String titolo){
        this.modifica = modificaProposta;
        this.autore = autore;
        this.titolo = titolo;
        autore.addNotificheRicevute(this);
    }

    public LocalDate getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public Timestamp getOraInvio() {
        return oraInvio;
    }

    public void setOraInvio(Timestamp oraInvio) {
        this.oraInvio = oraInvio;
    }
    public ModificaProposta getModifica() {return modifica;}
    public void setModifica(ModificaProposta modifica) {this.modifica = modifica;}
    public Autore getAutore() {
        return autore;
    }
    public void setAutore(Autore autore) {
        this.autore = autore;
    }
    public String getTitolo() {
        return titolo;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}
