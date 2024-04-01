import { Component, OnInit } from '@angular/core';
import { JeuService } from '../../http/jeuService';
import { Environnement, Environnements } from '../../model/environnement.model';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService'; // Importez CivilisationService

@Component({
  selector: 'app-menu-environnement',
  templateUrl: './menu-environnement.component.html',
  styleUrls: ['./menu-environnement.component.scss']
})
export class MenuEnvironnementComponent implements OnInit {
  listeEnvironnements: Environnements | null = null;
  selectedEnvironment: Environnement | null = null;
  hoveredKey: string | null = null;
  selectedKey: string | null = null;
  nomCivilisation: string = '';

  constructor(private jeuService: JeuService, private civilisationService: CivilisationService) {}

  ngOnInit(): void {
    localStorage.removeItem('environnement');
    localStorage.removeItem('civilisation');
    localStorage.removeItem('ressources');
    this.fetchData();
  }

  fetchData() {
    this.jeuService.httpListeEnvironnements().subscribe(
      data => {
        this.listeEnvironnements = data;
      },
      error => {
        console.error("Erreur lors de la récupération des environnements", error);
      }
    );
  }

  setSelectedEnvKey(key: string): void {
    this.selectedKey = key;
    if (this.listeEnvironnements) {
      this.selectedEnvironment = this.listeEnvironnements[key];
    }
  }

  setHoveredKey(key: string | null): void {
    this.hoveredKey = key;
  }

  getEnvironmentInfo(key: string | null, infoType: 'nom' | 'description'): string {
    if (key === null) {
      return ''; // Retourne une chaîne vide si la clé est null
    }
    const env = this.listeEnvironnements ? this.listeEnvironnements[key] : null;
    return env ? env[infoType] : '';
  }

  createCivilisation(): void {
    if (this.selectedKey && this.nomCivilisation) {
      const nomEnvironnement = this.getEnvironmentInfo(this.selectedKey, 'nom');
      var civilisation:Civilisation = new Civilisation();
      civilisation.nom = this.nomCivilisation;
      civilisation.nomEnvironnement = nomEnvironnement;
      this.civilisationService.setCivilisation(civilisation);
    }
  }
  
}


