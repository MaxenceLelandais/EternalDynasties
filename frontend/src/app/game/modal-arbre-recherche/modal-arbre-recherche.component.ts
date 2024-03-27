import { Component, OnInit } from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Recherches } from 'src/app/model/recherche.model';
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
  

  constructor(private jeuService: JeuService, private modalService: ModalService) {this.fetchData()}

  fetchData() {
    console.log(this.listeRecherches);
    this.jeuService.httpListeRecherches().subscribe(
      data => {
        this.listeRecherches = data;
        console.log(this.listeRecherches);
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
    this.jeuService.httpArbreRecherches().subscribe(
      data2 => {
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
}
