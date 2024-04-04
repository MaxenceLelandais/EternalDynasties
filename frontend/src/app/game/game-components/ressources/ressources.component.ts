import { Component, OnInit } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';


@Component({
  selector: 'app-ressources',
  templateUrl: './ressources.component.html',
  styleUrls: ['./ressources.component.scss']
})
export class RessourcesComponent {
  ressources: Ressources | null = null;
  nomJoueur: string |null = null;

  constructor(private ressourcesService: RessourceService, private jeuService: JeuService, private nomJoueurService: NomJoueurService) {
  }

  ngOnInit() {
    this.loadRessources();
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
  }

  private loadRessources() {
    if (localStorage.getItem('ressources')) {
      const savedRessources = localStorage.getItem('ressources');
      if (savedRessources != null) {
        this.ressources = JSON.parse(savedRessources);
      }
    } else {
      console.log("savedRessources");
      this.ressources = this.ressourcesService.getRessources();
      localStorage.setItem('ressources', JSON.stringify(this.ressources));
    }
  }

  addRessource(nomJoueur: string, ressource: string) {
    if (nomJoueur == null) {
      return;
    }
    this.jeuService.httpAddRessource(nomJoueur, ressource).subscribe({
      next: (response) => {
        console.log("Ressource ajoutée avec succès", response);
        this.ressources= response;
      },
      error: (error) => {
        console.error("Erreur lors de l'ajout de la ressource", error);
      }
    });
  }

}
