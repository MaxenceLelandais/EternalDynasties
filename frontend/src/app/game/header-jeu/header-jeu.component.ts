import { Component, OnInit } from '@angular/core';
import { Environnement } from 'src/app/model/environnement.model';
import { EnvironnementService } from 'src/app/service/environnementService';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';

@Component({
  selector: 'app-header-jeu',
  templateUrl: './header-jeu.component.html',
  styleUrls: ['./header-jeu.component.scss']
})
export class HeaderJeuComponent implements OnInit {

  environnement: Environnement | null = null;
  civilisation: Civilisation | null = null;

  constructor(private environnementService: EnvironnementService, private civilisationService: CivilisationService) {}

  ngOnInit() {
    this.environnementService.currentEnvironnement.subscribe(environnement => {
      this.environnement = environnement;
    });
    this.civilisation = this.civilisationService.getCivilisation();
  }
}
