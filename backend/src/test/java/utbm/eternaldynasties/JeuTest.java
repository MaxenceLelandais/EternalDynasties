package utbm.eternaldynasties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utbm.eternaldynasties.jeu.Jeu;
import utbm.eternaldynasties.jeu.Joueur;
import utbm.eternaldynasties.jeu.arbreDeRessources.Bonus;
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
    double tempsJeu = 0;
    int tempsPast = 0;
    ArrayList<Double> tempsList = new ArrayList<>();
    Map<String, ArrayList<Double>> allData = new HashMap<>();
    Jeu jeu;

    Joueur joueur;

    @Test
    void contextLoads() {
        jeu = this.jeuService.getJeu();
        joueur = jeu.startPartie(civilisation, environnement);
        HashMap<String, Double> listeRessourcePossedee = joueur.getRessources();

        while (!joueur.recherchesPossibles().isEmpty()) {

            List<Recherche> listeRecherchesPossibles = joueur.recherchesPossibles();
            Recherche rechercheLaMoinsCouteuseEnTemps = null;
            double tempsLePLusCourt = 0;
            ArrayList<String> listeActionsRechercheCourte = null;

            for (Recherche rechercheActuelleAComparer : listeRecherchesPossibles) {

                ArrayList<String> listeActions = joueur.besoinRessources(rechercheActuelleAComparer.getListeCout());
                joueur.ajoutStockage(listeActions);
                double tempsEstime = 0;
                for (String action : listeActions) {
                    tempsEstime += 1;
                    tempsEstime += Double.parseDouble(action.split(":")[1]) / this.nbrCliquesParSecondes;
                }
                if (tempsLePLusCourt == 0) {
                    tempsLePLusCourt = tempsEstime;
                    rechercheLaMoinsCouteuseEnTemps = rechercheActuelleAComparer;
                    listeActionsRechercheCourte = listeActions;
                } else if (tempsLePLusCourt > tempsEstime) {
                    tempsLePLusCourt = tempsEstime;
                    rechercheLaMoinsCouteuseEnTemps = rechercheActuelleAComparer;
                    listeActionsRechercheCourte = listeActions;
                }
            }
            if (rechercheLaMoinsCouteuseEnTemps != null) {
                System.out.println("Recherche : " + rechercheLaMoinsCouteuseEnTemps.getNom());

                for(String action : listeActionsRechercheCourte) {
                    String[] data = action.split(":");
                    String nomRessource = data[0];
                    double quantite = Double.parseDouble(data[1]);
                    this.addTempsJeu(1.0);
                    for(int n=0;n<quantite;n++) {
                        joueur.clickAchat(nomRessource);
                        this.addTempsJeu(1.0 / this.nbrCliquesParSecondes);
                    }
                }
                this.addTempsJeu(1.0);

                String retour = joueur.activerRecherche(rechercheLaMoinsCouteuseEnTemps.getNom());
                System.out.println(retour);
                if (retour.contains("RECHERCHE EFFECTUEE")) {
                    System.out.println("Temps collecte ressources : " + this.tempsJeu);
                }
            }
        }

        this.generateGraphiques();
    }

    private void addTempsJeu(double val) {
        this.tempsJeu += val;
        this.tempsList.add(this.tempsJeu);
        if(this.tempsPast!=(int)this.tempsJeu){
            this.tempsPast = (int)this.tempsJeu;
            joueur.tickBonus();
        }
        for (String nomRessource : joueur.getRessources().keySet()) {
            if (this.allData.containsKey(nomRessource)) {
                this.allData.get(nomRessource).add(joueur.getRessources().get(nomRessource));
            } else {
                ArrayList<Double> list = new ArrayList<>();
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

            ArrayList<Double> x = this.tempsList;
            ArrayList<Double> y = this.allData.get(nomRessource);
            new GraphGenerator(x, y, dossier + "/" + nomRessource + ".png", nomRessource);
        }

        new GraphGenerator(this.allData, this.tempsList, dossier + "/all.png");
    }
}
