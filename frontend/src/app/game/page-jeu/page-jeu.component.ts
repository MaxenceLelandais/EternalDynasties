import { Component, OnInit, ElementRef, ViewChild  } from '@angular/core';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { JeuService } from 'src/app/http/jeuService';
import { Environnement } from 'src/app/model/environnement.model';
import { EnvironnementService } from 'src/app/service/environnementService';
import { DropEvent } from 'angular-draggable-droppable';
import { ComponentFactoryResolver, ViewContainerRef } from '@angular/core';
import {
  DroppableDirective,
  ValidateDrop,
} from 'src/lib/droppable.directive';
import { Icon } from 'src/app/model/icon.model';
import { Ressource, Ressources } from 'src/app/model/ressource.model';
import { RessourceStructure, RessourceStructures } from 'src/app/model/ressourceStructure.model';
import { NomJoueurService } from 'src/app/service/nomJoueurService';


@Component({
  selector: 'app-page-jeu',
  templateUrl: './page-jeu.component.html',
  styleUrls: ['./page-jeu.component.css']
})
export class PageJeuComponent implements OnInit {
  civilisation: Civilisation | null = null;
  environnement: Environnement | null = null;
  ressources: Ressources | null = null;
  ressoorcesStructures: RessourceStructures[] = [];
  nomJoueur: string = "";
  public droppedComponentZone1: any = null;
  public droppedComponentZone2: any = null;

  
  

constructor(
  private jeuService: JeuService, 
  private civilisationService: CivilisationService, 
  private environnementService: EnvironnementService,
  private nomJoueurService: NomJoueurService
) { }
  ngOnInit() {
    this.loadCivilisation();
    this.fetchData();
  }

  droppedDataZone1!: string;
  droppedDataZone2!: string;

  @ViewChild(DroppableDirective, { read: ElementRef, static: true })
  droppableElement!: ElementRef;

  onDropZone1({ dropData }: DropEvent<string>): void {
    this.droppedDataZone1 = dropData;
  }

  onDropZone2({ dropData }: DropEvent<string>): void {
    this.droppedDataZone2 = dropData;
  }

  validateDrop: ValidateDrop = ({ target }) =>
    this.droppableElement.nativeElement.contains(target as Node);

  private loadCivilisation() {
    if (localStorage.getItem('civilisation')) {
      const savedCivilisation = localStorage.getItem('civilisation');
      if (savedCivilisation != null) {
        this.civilisation = JSON.parse(savedCivilisation);
      }
    } else {
      console.log("savedCivilisation");
      this.civilisation = this.civilisationService.getCivilisation();
      localStorage.setItem('civilisation', JSON.stringify(this.civilisation));
    }
  }
  

  fetchData() {
    if (this.civilisation != null) {
      this.jeuService.httpJoueur(this.civilisation.nom, this.civilisation.nomEnvironnement).subscribe({
          next: (response) => {
            console.log('Réponse du serveur', response);
          },
          error: (error) => {
            console.error('Erreur lors de la requête', error);
          }
        });

        this.jeuService.httpEnvironnement(this.civilisation.nomEnvironnement).subscribe(
          data => {
            this.environnement = data;
            this.environnementService.changeEnvironnement(data);
            console.log(data);
          },
          error => {
            console.error("Erreur lors de la récupération des environnements", error);
          }
        );

        this.nomJoueur = this.civilisation.nom + "-" + this.civilisation.nomEnvironnement;
        this.nomJoueurService.setNomJoueur(this.nomJoueur);
        
        this.jeuService.httpListeRessources(this.nomJoueur).subscribe({
          next: (response) => {
            this.ressources = response;
            localStorage.setItem('ressources', JSON.stringify(this.ressources));
            console.log('Réponse du serveur', response);
          },
          error: (error) => {
            console.error('Erreur lors de la requête', error);
          }
        });
      //   const storedRessources = localStorage.getItem('ressources');
      //   if (storedRessources) {
      //   try {
      //     const parsedRessources: Ressources = JSON.parse(storedRessources);
      //     if (Object.keys(parsedRessources).length > 0) {
      //       this.ressources = parsedRessources;
      //       return;
      //     }
      //   } catch (error) {
      //     console.error("Erreur lors de la lecture des ressources depuis localStorage", error);
      //   }
      // }
      // else {
      //   this.jeuService.httpListeRessources(this.nomJoueur).subscribe({
      //     next: (response) => {
      //       console.log('Réponse du serveur', response);
      //     },
      //     error: (error) => {
      //       console.error('Erreur lors de la requête', error);
      //     }
      //   });
      // }
    }
  }
}
