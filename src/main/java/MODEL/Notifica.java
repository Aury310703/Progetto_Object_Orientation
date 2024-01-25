package MODEL;

import javax.xml.crypto.Data;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Notifica {
    private LocalDate dataInvio;
    private LocalTime oraInvio;
    private String messaggio;
    private ModificaProposta modifica;
    private Autore autore;

    public Notifica(Autore autore, ModificaProposta modificaProposta){
        this.modifica = modificaProposta;
        this.autore = autore;
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

    public LocalTime getOraInvio() {
        return oraInvio;
    }

    public void setOraInvio(LocalTime oraInvio) {
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
}
