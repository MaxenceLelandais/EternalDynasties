package utbm.eternaldynasties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utbm.eternaldynasties.jeu.Jeu;
import utbm.eternaldynasties.jeu.Joueur;
import utbm.eternaldynasties.jeu.arbreDeRessources.Ressource;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.services.JeuService;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JeuTest {
    @Autowired
    private JeuService jeuService;
    String civilisation = "Test";
    String environnement = "Plaine";
    int nbrCliquesParSecondes = 5;
    float tempsJeu = 0;
    int tempsPast = 0;
    ArrayList<Float> tempsList = new ArrayList<>();
    Map<String, ArrayList<Long>> allData = new HashMap<>();
    Jeu jeu;

    Joueur joueur;

    @Test
    void contextLoads() {
        jeu = this.jeuService.getJeu();
        joueur = jeu.startPartie(civilisation, environnement);
        HashMap<String, Long> listeRessourcePossedee = joueur.getRessources();

        while (!joueur.recherchesPossibles().isEmpty()) {

            List<Recherche> listeRecherchesPossibles = joueur.recherchesPossibles();
            Recherche rechercheLaMoinsCouteuse = null;
            long quantiteDeRessourcesAOptenirLaMoinsNombreuse = 0L;

            for (Recherche rechercheActuelleAComparer : listeRecherchesPossibles) {
                long quantiteAOptenir = 0L;
                for (String nomRessource : rechercheActuelleAComparer.getListeCout().keySet()) {
                    long quantiteDeRessourcesAOptenir = rechercheActuelleAComparer.getListeCout().get(nomRessource) - listeRessourcePossedee.get(nomRessource);
                    quantiteAOptenir += quantiteDeRessourcesAOptenir > 0 ? quantiteDeRessourcesAOptenir : 0;
                }
                if (quantiteDeRessourcesAOptenirLaMoinsNombreuse == 0L) {
                    quantiteDeRessourcesAOptenirLaMoinsNombreuse = quantiteAOptenir;
                    rechercheLaMoinsCouteuse = rechercheActuelleAComparer;
                } else {
                    if (quantiteDeRessourcesAOptenirLaMoinsNombreuse > quantiteAOptenir) {
                        quantiteDeRessourcesAOptenirLaMoinsNombreuse = quantiteAOptenir;
                        rechercheLaMoinsCouteuse = rechercheActuelleAComparer;
                    }
                }
            }

            if (rechercheLaMoinsCouteuse != null) {
                System.out.println("Recherche : "+rechercheLaMoinsCouteuse.getNom());
                for (String nomRessource : rechercheLaMoinsCouteuse.getListeCout().keySet()) {
                    this.addTempsJeu(1);
                    Ressource maximum = joueur.getArbreDeRessources().getRessource("Max-" + nomRessource);
                    if (maximum != null && maximum.getListeBonus().get("Max-" + nomRessource).getQuantite() < rechercheLaMoinsCouteuse.getListeCout().get(nomRessource)) {

                        Map<Double, Ressource> map = joueur.getArbreDeRessources().getListeUpMax().get("Max-" + nomRessource);
                        Double valeurMax = map.keySet().stream().sorted().findFirst().orElse(null);
                        if (valeurMax != null) {
                            int diff = (int) ((rechercheLaMoinsCouteuse.getListeCout().get(nomRessource) - maximum.getListeBonus().get("Max-" + nomRessource).getQuantite()) / valeurMax);
                            for (int n = 0; n < diff; n++) {
                                Ressource ressourceAOptenir = map.get(valeurMax);
                                for (String nom : ressourceAOptenir.getListeCout().keySet()) {
                                    while (listeRessourcePossedee.get(nom) - ressourceAOptenir.getListeCout().get(nom) < 0) {
                                        joueur.clickAchat(nom);
                                        this.addTempsJeu(1.0 / this.nbrCliquesParSecondes);
                                    }
                                }
                                joueur.clickAchat(ressourceAOptenir.getNom());
                            }
                        }
                    }

                    while (rechercheLaMoinsCouteuse.getListeCout().get(nomRessource) - listeRessourcePossedee.get(nomRessource) > 0) {
                        joueur.clickAchat(nomRessource);
                        this.addTempsJeu(1.0 / this.nbrCliquesParSecondes);
                    }
                }
                String retour = joueur.activerRecherche(rechercheLaMoinsCouteuse.getNom());
                System.out.println(retour);
                if (retour.contains("RECHERCHE EFFECTUEE")) {
                    System.out.println("Temps collecte ressources : " + this.tempsJeu);
                }
            }
        }

        this.generateGraphiques();
    }

    private void addTempsJeu(double val) {
        this.tempsJeu += (float) val;
        this.tempsList.add(this.tempsJeu);
        if(this.tempsPast!=(int)this.tempsJeu){
            this.tempsPast = (int)this.tempsJeu;
            joueur.tickBonus();
        }
        for (String nomRessource : joueur.getRessources().keySet()) {
            if (this.allData.containsKey(nomRessource)) {
                this.allData.get(nomRessource).add(joueur.getRessources().get(nomRessource));
            } else {
                ArrayList<Long> list = new ArrayList<>();
                list.add(joueur.getRessources().get(nomRessource));
                this.allData.put(nomRessource, list);
            }
        }

    }

    private void generateGraphiques() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss");
        String chemin = "Graphiques/";

        String dossier = chemin + dateFormat.format(LocalDateTime.now());

        File dossierFile = new File(dossier);
        if (!dossierFile.exists()) {
            dossierFile.mkdirs();
        }

        for (String nomRessource : this.allData.keySet()) {

            ArrayList<Float> x = this.tempsList;
            ArrayList<Long> y = this.allData.get(nomRessource);
            new GraphGenerator(x, y, dossier + "/" + nomRessource + ".png", nomRessource);
        }

        new GraphGenerator(this.allData, this.tempsList, dossier + "/all.png");
    }
}
