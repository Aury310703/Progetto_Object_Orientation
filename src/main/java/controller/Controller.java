package controller;

import dao.WikiDAO;
import implementazionePostgresDAO.WikiimplementazionePostgresDAO;

public class Controller {
    public void eseguiQueryDB() {
        WikiDAO w = new WikiimplementazionePostgresDAO();
        try{
            w.eseguiQueryDB();
        }catch (Exception e){
            System.out.println("ggg");
        }

    }
}
