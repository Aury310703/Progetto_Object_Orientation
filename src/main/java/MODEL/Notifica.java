package MODEL;

import javax.xml.crypto.Data;
import java.sql.Time;

public class Notifica {
    private Data dataInvio;
    private Time oraInvio;
    private String messaggio;

    public Notifica(){}

    public Data getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(Data dataInvio) {
        this.dataInvio = dataInvio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public Time getOraInvio() {
        return oraInvio;
    }

    public void setOraInvio(Time oraInvio) {
        this.oraInvio = oraInvio;
    }
}
