package MODEL;

import java.sql.Date;
import java.sql.Time;

public class Visiona {
    private Date dataVisione;
    private Time oraVisione;
    public Visiona(Date dv, Time ov){
        this.dataVisione = dv;
        this.oraVisione = ov;
    }

    public Date getDataVisione() {
        return dataVisione;
    }
    public void setDataVisione(Date dataVisione) {
        this.dataVisione = dataVisione;
    }
    public Time getOraVisione() {
        return oraVisione;
    }
    public void setOraVisione(Time oraVisione) {
        this.oraVisione = oraVisione;
    }
}
