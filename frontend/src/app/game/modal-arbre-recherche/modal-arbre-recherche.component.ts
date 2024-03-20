import { Component, OnInit } from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Recherches } from 'src/app/model/recherche.model';
import { ModalService } from 'src/app/service/modal.Service';

@Component({
  selector: 'app-modal-arbre-recherche',
  templateUrl: './modal-arbre-recherche.component.html',
  styleUrls: ['./modal-arbre-recherche.component.css']
})

export class ModalArbreRechercheComponent implements OnInit {
  listeRecherches: Recherches | null = null;
  arbreRecherches: Recherches | null = null;
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

  ngOnInit() {
    this.modalService.watch().subscribe((status: 'open' | 'close') => {
      this.displayModal = status === 'open';
    });
  }

  closeModal() {
    this.modalService.close();
  }

  onBackgroundClicked(event: MouseEvent) {
    this.closeModal();
  }
}
