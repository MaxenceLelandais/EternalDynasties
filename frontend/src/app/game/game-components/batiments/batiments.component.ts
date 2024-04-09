import { Component, Input } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-batiments',
  templateUrl: './batiments.component.html',
  styleUrls: ['./batiments.component.scss']
})
export class BatimentsComponent {
  @Input()
  data:any;

  ressources: Ressources | null = null;
  nomJoueur: string |null = null;
  private subscriptions: Subscription = new Subscription();

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
    this.jeuService.httpAddRessource(nomJoueur, ressource, "1").subscribe({
      next: (response) => {
        console.log("Ressource ajoutée avec succès", response);
        this.ressourcesService.updateRessources(response);
        this.subscriptions.add(
          this.ressourcesService.ressources$.subscribe(ressources => {
            this.ressources = ressources;
            console.log("ressources : " + this.ressources);
          })
        );
      },
      error: (error) => {
        console.error("Erreur lors de l'ajout de la ressource", error);
      }
    });
  }
}
