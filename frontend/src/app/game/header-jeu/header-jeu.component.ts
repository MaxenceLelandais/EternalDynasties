import { Component, OnInit } from '@angular/core';
import { Environnement } from 'src/app/model/environnement.model';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { ModalService } from 'src/app/service/modal.Service';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { Subscription } from 'rxjs';
import { JeuService } from 'src/app/http/jeuService';
import { Ere, Eres } from 'src/app/model/ere.model';
import { NomJoueurService } from 'src/app/service/nomJoueurService';

@Component({
  selector: 'app-header-jeu',
  templateUrl: './header-jeu.component.html',
  styleUrls: ['./header-jeu.component.scss']
})
export class HeaderJeuComponent implements OnInit {

  environnement: Environnement | null = null;
  civilisation: Civilisation | null = null;
  ressources: Ressources | null = null;
  listeEre: Eres | null = null;
  nomJoueur: string | null = null;
  ere: Ere | null = null;
  idEre: number | null = null;
  
  private subscriptions: Subscription = new Subscription();

  constructor(
    private civilisationService: CivilisationService, 
    private modalService: ModalService,
    private jeuService: JeuService,
    private ressourcesService: RessourceService,
    private nomJoueurService: NomJoueurService) {}


  
  ngOnInit() {
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
    this.subscriptions.add(this.ressourcesService.ressources$.subscribe(
      ressources => this.ressources = ressources
    ));
    if(this.civilisationService.getCivilisation()!=null){
      this.civilisation = this.civilisationService.getCivilisation();
    }
    this.fetchData();
    this.loadEnvironnement();
    this.loadRessources();
    this.loopTick();
  }

  fetchData() {
    this.jeuService.httpListeEres().subscribe(
      data => {
        this.listeEre = data["Ere"];
      },
      error => {
        console.error("Erreur lors de la récupération des eres", error);
      }
    );
    if (this.nomJoueur) {
      this.jeuService.httpEre(this.nomJoueur).subscribe({
        next: (response) => {
          this.ere = response;
          if (this.listeEre && this.ere) {
            const ereValues = Object.values(this.listeEre);
            const ereMatch = ereValues.find(ereItem => ereItem.nom === this.ere!.nom);
            console.log("ereMatch : " + ereMatch);
            if (ereMatch) {
              this.idEre = ereMatch.id;
              console.log("ere id : " + this.idEre);
            } else {
              console.log('Aucune correspondance trouvée pour cette ère dans la liste');
            }
          }
          
        },
        error: (error) => {
          console.error('Erreur lors de la requête', error);
        }
      });
    }
  }

  loopTick(){
    setInterval(() => {
      if(this.civilisation!=null){
        this.jeuService.httpTick(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement).subscribe({
          next: (reponse) => {
            this.updateResources(reponse);
          },
          error: (error) => {
            console.error('Erreur lors de la requête', error);
          }
        });
    }
    }, 1000);
  }

  openModal() {
    this.modalService.open();
  }

  public loadEnvironnement() {
    if(this.civilisation!=null){
      this.jeuService.httpEnvironnement(this.civilisation.nomEnvironnement).subscribe({
        next: (reponse) => {
          this.environnement = reponse;
        },
        error: (error) => {
          console.error('Erreur lors de la requête', error);
        }
      });
    }
  }

  public loadRessources() {
    if(this.civilisation!=null){
        this.jeuService.httpListeRessources(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement).subscribe({
          next: (reponse) => {
            this.updateResources(reponse);
          },
          error: (error) => {
            console.error('Erreur lors de la requête', error);
          }
        });
    }
  }
  
  updateResources(newResource: Ressources) {
    this.ressources = newResource;
    if(this.ressources!=null)
    {
      this.ressourcesService.updateRessources(this.ressources);
      this.subscriptions.add(
        this.ressourcesService.ressources$.subscribe(ressources => {
          this.ressources = ressources;
        })
      )
      localStorage.setItem('ressources', JSON.stringify(this.ressources));
    }
  }
}