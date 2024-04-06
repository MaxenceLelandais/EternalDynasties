import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';

@Component({
  selector: 'app-ressources',
  templateUrl: './ressources.component.html',
  styleUrls: ['./ressources.component.scss']
})
export class RessourcesComponent implements OnInit, OnDestroy {
  ressources: Ressources | null = null;
  nomJoueur: string | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private ressourcesService: RessourceService,
    private jeuService: JeuService,
    private nomJoueurService: NomJoueurService
  ) {}

  ngOnInit() {
    this.loadRessources();
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
    // Abonnez-vous à l'Observable pour obtenir les mises à jour automatiques

  }

  ngOnDestroy() {
    // Se désabonner pour éviter les fuites de mémoire
    this.subscriptions.unsubscribe();
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
        // Mise à jour des ressources à travers le service
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
