package model;

import java.io.File;

/*Gestisce l'autenticazione e altro che non so*/
public class MailServer {

    public static boolean emailExists(String email){
        File file = new File ("src/main/Mailbox/" + email + ".txt");
        return file.exists();
    }
}
