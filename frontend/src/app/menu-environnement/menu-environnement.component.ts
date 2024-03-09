import { Component, OnInit } from '@angular/core';
import { JeuService } from '../http/jeuService';
import {Environnement, Environnements } from '../model/environnement.model'

@Component({
  selector: 'app-menu-environnement',
  templateUrl: './menu-environnement.component.html',
  styleUrls: ['./menu-environnement.component.scss']
})
export class MenuEnvironnementComponent implements OnInit {
  listeEnvironnements: Environnements | null = null;

  constructor(private jeuService: JeuService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData() {
    this.jeuService.httpListeEnvironnements().subscribe(
      data => {
        this.listeEnvironnements = data;
      },
      error => {
        console.error("Erreur lors de la récupération de listeEnvironnements", error);
      }
    );
  }
}
