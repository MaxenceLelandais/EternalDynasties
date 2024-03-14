/**
 * Classe Bonus : Elle s'occupe de conserver les données des gains obtenues par ressources.
 */

package utbm.eternaldynasties.jeu.arbreDeRessources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Permet de traiter les bonus de façons à les rendres utilisables par le jeu.
 */
public class Bonus {
    private String ressourceGenere;
    private double quantite = 0.0;
    private double quantiteParSecondes = 0.0;
    private final Map<String, Double> quantiteParRessources = new HashMap<>();
    private double pourcentage = 0.0;
    private double pourcentageParSecondes = 0.0;
    private final Map<String, Double> pourcentageParRessources = new HashMap<>();

    private Bonus() {}

    /**
     * Converti et trie les informations string en gain utilisables.
     *
     * @param ressourceGenere le string contenant les informations des gains
     * @param generation      le nom de la ressource associée.
     */
    public Bonus(String ressourceGenere, String generation) {
        this.ressourceGenere = ressourceGenere;
        for (String production : generation.split(";")) {
            // par
            if (production.contains("/")) {
                if (production.contains("%/s")) {
                    this.pourcentageParSecondes = convertStringToDouble(production.replace("%/s", ""));
                } else if (production.contains("/s")) {
                    this.quantiteParSecondes = convertStringToDouble(production.replace("/s", ""));
                } else if (production.contains("%/")) {
                    String[] data = production.split("%/");
                    this.pourcentageParRessources.put(data[1], convertStringToDouble(data[0]));
                } else {
                    String[] data = production.split("/");
                    this.quantiteParRessources.put(data[1], convertStringToDouble(data[0]));
                }
            }
            // %
            else if (production.contains("%")) {
                this.pourcentage = convertStringToDouble(production.replace("%", ""));
            } else {
                this.quantite = convertStringToDouble(production);
            }
        }
    }

    /**
     * Additionne les données avec un autre bonus.
     */
    public void add(Bonus bonus) {
        this.quantite += bonus.getQuantite();
        this.quantiteParSecondes += bonus.getQuantiteParSecondes();
        for (String key : bonus.getQuantiteParRessources().keySet()) {
            if (this.quantiteParRessources.containsKey(key)) {
                this.quantiteParRessources.replace(key, this.quantiteParRessources.get(key) + bonus.getQuantiteParRessources().get(key));
            } else {
                this.quantiteParRessources.put(key, bonus.getQuantiteParRessources().get(key));
            }
        }
        this.pourcentage += bonus.getPourcentage();
        this.pourcentageParSecondes += bonus.getPourcentageParSecondes();
        for (String key : bonus.getPourcentageParRessources().keySet()) {
            if (this.pourcentageParRessources.containsKey(key)) {
                this.pourcentageParRessources.replace(key, this.pourcentageParRessources.get(key) + bonus.getPourcentageParRessources().get(key));
            } else {
                this.pourcentageParRessources.put(key, bonus.getPourcentageParRessources().get(key));
            }
        }
    }

    /**
     * Soustrait les données avec un autre bonus.
     */
    public void moins(Bonus bonus) {
        this.quantite -= bonus.getQuantite();
        this.quantiteParSecondes -= bonus.getQuantiteParSecondes();
        for (String key : bonus.getQuantiteParRessources().keySet()) {
            if (this.quantiteParRessources.containsKey(key)) {
                this.quantiteParRessources.replace(key, this.quantiteParRessources.get(key) - bonus.getQuantiteParRessources().get(key));
            } else {
                this.quantiteParRessources.put(key, -bonus.getQuantiteParRessources().get(key));
            }
        }
        this.pourcentage -= bonus.getPourcentage();
        this.pourcentageParSecondes -= bonus.getPourcentageParSecondes();
        for (String key : bonus.getPourcentageParRessources().keySet()) {
            if (this.pourcentageParRessources.containsKey(key)) {
                this.pourcentageParRessources.replace(key, this.pourcentageParRessources.get(key) - bonus.getPourcentageParRessources().get(key));
            } else {
                this.pourcentageParRessources.put(key, -bonus.getPourcentageParRessources().get(key));
            }
        }
    }

    private Double convertStringToDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (IllegalArgumentException e) {
            return 0.0;
        }
    }

    /**
     * Traduit à l'instant T les ressources qui seront potentiellement gagnées.
     *
     * @param ressourcesActuelles Liste des ressources possédées.
     * @return Un bonus qui résume ces données.
     */
    public Bonus resume(HashMap<String, Long> ressourcesActuelles) {
        Bonus resumeBonus = new Bonus();
        double q = this.getQuantite();
        for (String nom : this.getQuantiteParRessources().keySet()) {
            q += this.getQuantiteParRessources().get(nom) * ressourcesActuelles.get(nom);
        }

        double p = this.getPourcentage();
        for (String nom : this.getPourcentageParRessources().keySet()) {
            p += this.getPourcentageParRessources().get(nom) * ressourcesActuelles.get(nom);
        }

        q *= 1 + (p / 100);
        resumeBonus.setQuantite(q);
        resumeBonus.setQuantiteParSecondes(this.getQuantiteParSecondes());
        resumeBonus.setPourcentageParSecondes(this.getPourcentageParSecondes());
        return resumeBonus;
    }

    /**
     * Traduit à l'instant T les ressources qui seront potentiellement gagnées.
     *
     * @param ressourcesActuelles Liste des ressources possédées.
     * @return Un long qui résume ces données.
     */
    public long estimationValeur(HashMap<String, Long> ressourcesActuelles) {
        double q = this.getQuantite();
        for (String nom : this.getQuantiteParRessources().keySet()) {
            q += this.getQuantiteParRessources().get(nom) * ressourcesActuelles.get(nom);
        }

        double p = this.getPourcentage();
        for (String nom : this.getPourcentageParRessources().keySet()) {
            p += this.getPourcentageParRessources().get(nom) * ressourcesActuelles.get(nom);
        }

        q *= 1 + (p / 100);
        return (long) q;
    }

    public String getRessourceGenere() {
        return ressourceGenere;
    }

    public double getQuantite() {
        return quantite;
    }

    public double getQuantiteParSecondes() {
        return quantiteParSecondes;
    }

    public Map<String, Double> getQuantiteParRessources() {
        return quantiteParRessources;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public double getPourcentageParSecondes() {
        return pourcentageParSecondes;
    }

    public Map<String, Double> getPourcentageParRessources() {
        return pourcentageParRessources;
    }

    public void addQuantite(double quantite) {
        this.quantite+=quantite;
    }

    private void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    private void setQuantiteParSecondes(double quantiteParSecondes) {
        this.quantiteParSecondes = quantiteParSecondes;
    }

    private void setPourcentageParSecondes(double pourcentageParSecondes) {
        this.pourcentageParSecondes = pourcentageParSecondes;
    }

    /**
     * Traduit l'instance en string transmissible.
     */
    public String toString() {
        ArrayList<String> text = new ArrayList<>();
        text.add(this.getQuantite() + "");

        if (this.getQuantiteParSecondes() != 0.0) {
            text.add(this.getQuantiteParSecondes() + "/s");
        }
        if (this.getPourcentage() != 0.0) {
            text.add(this.getPourcentage() + "%");
        }
        if (this.getPourcentageParSecondes() != 0.0) {
            text.add(this.getPourcentageParSecondes() + "%/s");
        }
        for (String key : this.getQuantiteParRessources().keySet()) {
            text.add(this.getQuantiteParRessources().get(key) + "/" + key);
        }
        for (String key : this.getPourcentageParRessources().keySet()) {
            text.add(this.getPourcentageParRessources().get(key) + "%/" + key);
        }
        return String.join(";", text);
    }
}
