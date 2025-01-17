package model;

import java.util.Date;
import java.util.List;

/* Come client, ma qui serve per gestire i messaggi sul server (mailbox)*/
public class Email {

    private int id;
    private String mittente;
    private List<String> destinatari;
    private String oggetto;
    private String testo;
    private Date dataInvio;


    public Email(int id, String mittente, List<String> destinatari, String oggetto, String testo){
        this.id=id;
        this.mittente = mittente;
        this.destinatari=destinatari;
        this.oggetto=oggetto;
        this.testo=testo;
    }


    public int getId() {
        return id;
    }

    public String getMittente() {
        return mittente;
    }

    public List<String> getDestinatari() {
        return destinatari;
    }

    public String getOggetto() {
        return oggetto;
    }

    public String getTesto() {
        return testo;
    }

    public Date getDataInvio() {
        return dataInvio;
    }

}
