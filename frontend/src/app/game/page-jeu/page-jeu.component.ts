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


@Component({
  selector: 'app-page-jeu',
  templateUrl: './page-jeu.component.html',
  styleUrls: ['./page-jeu.component.css']
})
export class PageJeuComponent implements OnInit {
  civilisation: Civilisation | null = null;
  environnement: Environnement | null = null;
  public droppedComponentZone1: any = null;
  public droppedComponentZone2: any = null;

  
  

constructor(
  private jeuService: JeuService, 
  private civilisationService: CivilisationService, 
  private environnementService: EnvironnementService,
  private componentFactoryResolver: ComponentFactoryResolver
) { }
  ngOnInit() {
    this.civilisation = this.civilisationService.getCivilisation();
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
            this.environnementService.changeEnvironnement(data); // Mettez à jour le service ici
            console.log(data);
          },
          error => {
            console.error("Erreur lors de la récupération des environnements", error);
          }
        );
    }
  }
}
