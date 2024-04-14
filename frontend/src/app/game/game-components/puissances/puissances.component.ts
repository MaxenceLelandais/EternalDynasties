import { Component } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';

@Component({
  selector: 'app-puissances',
  templateUrl: './puissances.component.html',
  styleUrls: ['./puissances.component.scss']
})
export class PuissancesComponent {
  ressources: Ressources | null = null;

  constructor(private ressourcesService: RessourceService) {
  }

  ngOnInit() {
    this.loadRessources();
  }

  private loadRessources() {
    if (localStorage.getItem('ressources')) {
      const savedRessources = localStorage.getItem('ressources');
      if (savedRessources != null) {
        this.ressources = JSON.parse(savedRessources);
      }
    } else {
      console.log("savedRessources");
      this.ressources = this.ressourcesService.getRessources();
      localStorage.setItem('ressources', JSON.stringify(this.ressources));
    }
  }
}
