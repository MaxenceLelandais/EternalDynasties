import { Component, Input } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';
import { Subscription } from 'rxjs';
import { RessourcesCompletes } from 'src/app/model/ressourceComplete';

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
  donnees!:RessourcesCompletes;
  infos: Map<string, string> = new Map<string, string>();

  constructor(private ressourcesService: RessourceService, private jeuService: JeuService, private nomJoueurService: NomJoueurService) {
  }

  ngOnInit() {
    this.subscribeToRessources();
    this.loadRessources();
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
    if(this.nomJoueur!=null)
      {
        this.jeuService.httpListeRessourcesComplete(this.nomJoueur).subscribe({
          next: (response) => {
            this.donnees = response;
            
            Object.values(this.donnees).forEach(ressourceComplete=>{
              this.infos.set(ressourceComplete.nom,this.detail(ressourceComplete.nom));
            });
            Object.values(this.donnees).forEach(ressourceComplete=>{
              this.infos.set(ressourceComplete.nom,this.detail(ressourceComplete.nom));
            });
            Object.values(this.donnees).forEach(ressourceComplete=>{
              this.infos.set(ressourceComplete.nom,this.detail(ressourceComplete.nom));
            });
            Object.values(this.donnees).forEach(ressourceComplete=>{
              this.infos.set(ressourceComplete.nom,this.detail(ressourceComplete.nom));
            });
          },
          error: (error) => {
            console.error("Erreur lors de l'ajout de la ressource", error);
          }
        });
      }
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
        this.ressourcesService.updateRessources(response);
        this.subscribeToRessources();
        localStorage.setItem('ressources', JSON.stringify(this.ressources));
      },
      error: (error) => {
        console.error("Erreur lors de l'ajout de la ressource", error);
      }
    });
  }

  detail(nom: string): string {
    const ressource = this.donnees[nom];
    let text = `Description : ${this.donnees[nom].description}`;
  
    const coutKeys = Object.keys(ressource.listeCout);
    const bonusKeys = Object.keys(ressource.listeBonusEstime);
    let presenceCout = false;
    if (coutKeys.length > 0) {
      presenceCout = true;
      text += `\n\nCoûts : \n`;
      for (const key of coutKeys) {
        text += `- ${key} : ${ressource.listeCout[key]}\n`;
      }
    }
  
    if (bonusKeys.length > 0) {
      if(presenceCout){
        text += `\nBonus : \n`;
      }else{
        text += `\n\nBonus : \n`;
      }
      
      for (const key of bonusKeys) {
        text += `- ${key} : ${ressource.listeBonusEstime[key]}\n`;
      }
    }
  
    return text;
  }

  private subscribeToRessources() {
    this.subscriptions.unsubscribe();
    // Ajoutez un abonnement pour la récupération des ressources
    this.subscriptions.add(
      this.ressourcesService.ressources$.subscribe(ressources => {
        this.ressources = ressources;
      })
    );
  }

  ngOnDestroy(): void {
    // Désabonnez-vous de tous les abonnements lorsque le composant est détruit
    this.subscriptions.unsubscribe();
  }
}
