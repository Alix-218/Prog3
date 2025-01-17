package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;

/* Come client, ma qui serve per gestire i messaggi sul server (mailbox)*/
public class Email {

    private int id;
    private String mittente;
    private ArrayList<String> destinatari;
    private String oggetto;
    private String testo;
    private LocalDateTime dataInvio;

    public Email() {
    }

    public Email(int id, String mittente, ArrayList<String> destinatari, String oggetto, String testo){
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

    public ArrayList<String> getDestinatari() {
        return destinatari;
    }

    public String getOggetto() {
        return oggetto;
    }

    public String getTesto() {
        return testo;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public void setDestinatari(ArrayList<String> destinatari) {
        this.destinatari = destinatari;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", mittente='" + mittente + '\'' +
                ", destinatari=" + destinatari +
                ", oggetto='" + oggetto + '\'' +
                ", testo='" + testo + '\'' +
                ", dataInvio=" + dataInvio +
                '}';
    }
}
