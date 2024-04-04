import { Component, OnInit, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Recherche, Recherches } from 'src/app/model/recherche.model';
import { ArbreRecherche } from 'src/app/model/arbreRecherche.model';
import { ModalService } from 'src/app/service/modal.Service';

@Component({
  selector: 'app-modal-arbre-recherche',
  templateUrl: './modal-arbre-recherche.component.html',
  styleUrls: ['./modal-arbre-recherche.component.css']
})

export class ModalArbreRechercheComponent implements OnInit {
  listeRecherches: Recherches | null = null;
  arbreRecherches: Recherches | null = null;
  arborescenceRecherche: ArbreRecherche[] = []; 
  displayModal: boolean = false;
  rechercheCouranteSurvolee: Recherche | null = null;
  

  constructor(private jeuService: JeuService, private modalService: ModalService) {this.fetchData()}

  fetchData() {
    console.log(this.listeRecherches);
    this.jeuService.httpListeRecherches().subscribe(
      data => {
        console.log(data);
        this.listeRecherches = data;
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
    this.jeuService.httpArbreRecherches().subscribe(
      data2 => {
        console.log(data2);
        this.arbreRecherches = data2;
        console.log(this.arbreRecherches);
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
  


  ngOnInit() {
    this.modalService.watch().subscribe((status: 'open' | 'close') => {
      this.displayModal = status === 'open';
      if(this.displayModal) {
        this.construireArborescence();
      }
    });
  }

  closeModal() {
    this.modalService.close();
  }

  onBackgroundClicked(event: MouseEvent) {
    this.closeModal();
  }

  getRecherche(nom: string): Recherche | null {
    console.log("getRechercheEnter dans " + nom);
    // Convertir les valeurs de l'objet listeRecherches en un tableau
    const recherchesArray = this.listeRecherches ? Object.values(this.listeRecherches) : [];
    // Utiliser .find() sur le tableau pour trouver la recherche par nom
    const recherche = recherchesArray.find(r => r.nom === nom);
    console.log("recherche trouvé dans " + recherche?.Description);
    return recherche || null; // Retourner null si recherche est undefined
  }
  

  handleMouseEnter(nom: string) {
    console.log("mouseEnter dans " + nom);
    this.rechercheCouranteSurvolee = this.getRecherche(nom);
  }
  
  handleMouseLeave() {
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
  
  
  
}
