package MODEL;

public class Frase {
    protected String stringa_inserita;
    public Frase(String s) {
        this.stringa_inserita = s;
    }
    protected Pagina pagina;

    public String getStringa_inserita() {
        return stringa_inserita;
    }

    public void setStringa_inserita(String stringa_inserita) {
        this.stringa_inserita = stringa_inserita;
    }

    public Pagina getPagina() {
        return pagina;
    }

    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }
}
