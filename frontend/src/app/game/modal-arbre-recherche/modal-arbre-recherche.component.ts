import { Component, OnInit, ElementRef, Renderer2, ViewChild, Input} from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Recherche, Recherches } from 'src/app/model/recherche.model';
import { ArbreRecherche } from 'src/app/model/arbreRecherche.model';
import { ModalService } from 'src/app/service/modal.Service';
import { NomJoueurService } from 'src/app/service/nomJoueurService';
import { Subscription } from 'rxjs';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { RessourceService } from 'src/app/service/ressourceService';
import { Ressources } from 'src/app/model/ressource.model';

@Component({
  selector: 'app-modal-arbre-recherche',
  templateUrl: './modal-arbre-recherche.component.html',
  styleUrls: ['./modal-arbre-recherche.component.css']
})

export class ModalArbreRechercheComponent implements OnInit {
  listeRecherches: Recherches | null = null;
  listeRecherchesJoueur: Recherches | null = null;
  arbreRecherches: Recherches | null = null;
  arborescenceRecherche: ArbreRecherche[] = []; 
  displayModal: boolean = false;
  rechercheCouranteSurvolee: Recherche | null = null;
  rechercheCouranteCliquee: Recherche | null = null;
  nomJoueur: string | null = null;
  private subscriptions: Subscription = new Subscription();  

  ressources: Ressources | null = null;

  civilisation: Civilisation | null = null;

  
  @ViewChild('modalContent', { static: false }) modalContent!: ElementRef;
  

  constructor(private jeuService: JeuService,
              private modalService: ModalService,
              private renderer: Renderer2,
              private ressourcesService: RessourceService,
              private civilisationService: CivilisationService, 
              private nomJoueurService: NomJoueurService,) {}

  ngOnInit() {
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
    if(this.civilisationService.getCivilisation()!=null){
      this.civilisation = this.civilisationService.getCivilisation();
    }
    this.loadRessources();
    
    this.fetchData();
    this.modalService.watch().subscribe((status: 'open' | 'close') => {
      this.displayModal = status === 'open';
      if(this.displayModal) {
        this.construireArborescence();
      }
    });
  }

    ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  fetchData() {
    this.jeuService.httpListeRecherches().subscribe(
      data => {
        this.listeRecherches = data;
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
    this.jeuService.httpArbreRecherches().subscribe(
      data2 => {
        this.arbreRecherches = data2;
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
    this.jeuService.httpListeRecherchesJoueur(this.nomJoueur!).subscribe(
      data3 => {
        this.listeRecherchesJoueur = data3;
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
  }
  
  construireArborescence() {
    this.arborescenceRecherche = []; 
    if (this.arbreRecherches === null) {
      console.warn("arbreRecherches est null, impossible de construire l'arborescence");
      return;
    }
  
    const map = new Map<number, ArbreRecherche>();
  
    Object.values(this.arbreRecherches).forEach(recherche => {
      map.set(recherche.id, { id: recherche.id, nom: recherche.nom, enfant: [] });
    });
  
    Object.values(this.arbreRecherches).forEach(recherche => {
      const arbreRechercheTransforme = map.get(recherche.id);
      if (recherche.parent !== null && map.has(recherche.parent)) {
        const parentArbre = map.get(recherche.parent);
        if (parentArbre && arbreRechercheTransforme) {
          parentArbre.enfant.push(arbreRechercheTransforme);
        }
      } else {
        if (arbreRechercheTransforme) {
          this.arborescenceRecherche.push(arbreRechercheTransforme);
        }
      }
    });
    console.log(this.arborescenceRecherche);
  }
  
  closeModal() {
    this.modalService.close();
  }

  onBackgroundClicked(event: MouseEvent) {
    this.closeModal();
  }

  getRecherche(nom: string): Recherche | null {
    const recherchesArray = this.listeRecherchesJoueur ? Object.values(this.listeRecherchesJoueur) : [];
    const recherche = recherchesArray.find(r => r.nom === nom);
    return recherche || null;
  }
  
  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  async handleMouseEnter(nom: string, event: MouseEvent) {
    this.rechercheCouranteSurvolee = this.getRecherche(nom);
    await this.delay(1);
    const tooltips = document.getElementsByClassName("tooltiptext");
    
    if (tooltips) {
      const tooltip = tooltips[0] as HTMLElement;

      const modalRect = this.modalContent.nativeElement.getBoundingClientRect();
      const tooltipRect = tooltip.getBoundingClientRect();
      if (tooltipRect.bottom > modalRect.bottom) {
        const bottomOffset = tooltipRect.bottom - modalRect.bottom;
        tooltip.style.top = `calc(50% - ${bottomOffset}px - 50px)`;
      }    
      if (tooltipRect.top < modalRect.top) {
        const topOffset = modalRect.top - tooltipRect.top;
        tooltip.style.top = `calc(50% + ${topOffset}px + 20px)`;
      }
    }
  }
  
  
  handleMouseLeave(event: MouseEvent) {
    const tooltip = (event.target as HTMLElement).querySelector('.tooltiptext');
    if (tooltip) {
      // Réinitialisez les styles de l'infobulle
      this.renderer.removeStyle(tooltip, 'top');
    }
    this.rechercheCouranteSurvolee = null;
  }

  logArborescenceRecherche(arborescence: ArbreRecherche[], niveau: number = 0): void {
    if (!arborescence) {
      console.log("Aucune arborescence de recherche disponible");
      return;
    }
    
    arborescence.forEach(recherche => {
      const indentation = Array(niveau + 1).join("  "); // Crée une indentation basée sur le niveau de profondeur
      console.log(`${indentation}${recherche.nom}`);
      if (recherche.enfant && recherche.enfant.length > 0) {
        // S'il y a des enfants, loggez-les récursivement
        this.logArborescenceRecherche(recherche.enfant, niveau + 1);
      }
    });
  }
  
  // Appel de la fonction pour afficher l'arborescence
  
  
  isLastChild(recherche: Recherche, arborescence: ArbreRecherche[]): boolean {
    function search(arborescence: ArbreRecherche[]): boolean | null {
      for (let i = 0; i < arborescence.length; i++) {
        const node = arborescence[i];
        if (node.nom === recherche.nom) {
          if (node.enfant && node.enfant.length > 0) {
            return false;
          } else {
            return i === arborescence.length - 1;
          }
        }
        if (node.enfant && node.enfant.length > 0) {
          const result = search(node.enfant);
          if (result !== null) {
            return result;
          }
        }
      }
      return null;
    }
  
    const result = search(arborescence);
    return result !== null ? result : false;
  }
  
  get coutKeyValue() {
    if (this.rechercheCouranteSurvolee && this.rechercheCouranteSurvolee['Coût']) {
      return this.rechercheCouranteSurvolee['Coût'];
    }
    return null;
  }
  
  addRecherche(nomJoueur: string, recherche: Recherche) {
    this.rechercheCouranteCliquee = this.getRecherche(recherche.nom);
    if (this.rechercheCouranteCliquee == null) {
      return;
    }
    for (const [key, value] of Object.entries(this.rechercheCouranteCliquee)) {
      console.log(`${key}: ${value}`);
    }
    if (nomJoueur == null) {
      return;
    }
    if (recherche.RecherchePossible == false) {
      return;
    }
    this.jeuService.httpActiverRecherche(nomJoueur, recherche.nom).subscribe({
      next: (response) => {
        this.listeRecherches = response;
        
        this.loadRessources();
        
      },
      error: (error) => {
        console.error("Erreur lors de l'ajout de la ressource", error);
      }
    });
    this.jeuService.httpArbreRecherches().subscribe(
      data2 => {
        this.arbreRecherches = data2;
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
    this.jeuService.httpListeRecherchesJoueur(this.nomJoueur!).subscribe(
      data3 => {
        this.listeRecherchesJoueur = data3;
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
  }
  public loadRessources() {
    if(this.civilisation!=null){
        this.jeuService.httpListeRessources(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement).subscribe({
          next: (reponse) => {
              this.ressourcesService.updateRessources(reponse);
              this.subscriptions.add(
                this.ressourcesService.ressources$.subscribe(ressources => {
                  this.ressources = ressources;
                })
              )
              localStorage.setItem('ressources', JSON.stringify(reponse));
            
          },
          error: (error) => {
            console.error('Erreur lors de la requête', error);
          }
        });
    }
  }
}
