package model;
/*Struttura di un email, gestisce la validazione degli indirizzi*/

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;

public class Email{
    private int id;
    private String mittente;
    private List<String> destinatari;
    private String oggetto;
    private String testo;
    private LocalDateTime dataInvio;


    public Email(int id, String mittente, List<String> destinatari, String oggetto, String testo){
        if (!isValidEmail(mittente)) {
            System.out.println("Indirizzo mittente non valido: " + mittente);
        }else{
            this.id=id;
            this.mittente = mittente;
        }

        for (String destinatario : destinatari) {
            if (!isValidEmail(destinatario)) {
                System.out.println("Indirizzo destinatario non valido: " + destinatario);
            } else {
                this.destinatari = destinatari;
                this.oggetto = oggetto;
                this.testo = testo;
            }
        }

    }

    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@[a-zA-Z]+(?:\\.[a-zA-Z]+)*(?:\\.(com|net|org|it))+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

}