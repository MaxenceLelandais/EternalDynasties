/**
 * Classe Log écrite par Clément, Maxence et Nicolas.
 * FISA Informatique UTBM en PR70 2023.
 */

package utbm.eternaldynasties.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * La classe Log est utilisé comme classe statique.
 * Elle permet d'écrire les évènements du programme dans plusieurs fichier.
 */
public class Log {

    public static HashMap<String, FileWriter> listeEcriture = new HashMap<>();

    // Chemin des fichiers logs.
    public static String chemin = "Logs//";
    // Format de la date du nom du dossier.
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Format de la date pour chaque ligne de log.
    private static final DateTimeFormatter chronoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    /**
     * Enregistrement d'une ligne d'erreur.
     *
     * @param nomClasse String, nom du fichier où sera enregistré la ligne d'erreur.
     * @param message   String, la description de la ligne d'erreur.
     */
    public static void error(String nomClasse, String message) {
        send("error", nomClasse, message);
    }

    /**
     * Enregistrement d'une ligne d'alerte.
     *
     * @param nomClasse String, nom du fichier où sera enregistré la ligne d'alerte.
     * @param message   String, la description de la ligne d'alerte.
     */
    public static void warn(String nomClasse, String message) {
        send("warn", nomClasse, message);
    }

    /**
     * Enregistrement d'une ligne d'informations.
     *
     * @param nomClasse String, nom du fichier où sera enregistré la ligne d'informations.
     * @param message   String, la description de la ligne d'informations.
     */
    public static void info(String nomClasse, String message) {
        send("info", nomClasse, message);
    }

    /**
     * Enregistrement du message.
     * Chaque ligne sera dans le format suivant :
     * AAAA-MM-JJ HH:MM:SS:MS [type] = message
     *
     * @param type      String, type du log.
     * @param nomClasse String, nom du fichier.
     * @param message   String, description du log.
     */
    private static void send(String type, String nomClasse, String message) {


        // Création du chemin où le fichier log sera sauvegardé.
        String pathWritter = chemin + dateFormat.format(LocalDateTime.now()) + "//" + nomClasse + ".log";
        try {

            // Création des dossiers pour les logs (dossier logs et dossier avec la date actuelle).
            new File(chemin).mkdir();
            new File(chemin + dateFormat.format(LocalDateTime.now())).mkdir();

            // Création du fichier.
            File file = new File(pathWritter);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            String text = chronoFormat.format(LocalDateTime.now()) + " [" + type + "] = " + message + "\n";
            // Ecriture des logs.
            fw.write(text);
            System.out.println(text);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}