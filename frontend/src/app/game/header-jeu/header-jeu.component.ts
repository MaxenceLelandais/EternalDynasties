import { Component, OnInit } from '@angular/core';
import { Environnement } from 'src/app/model/environnement.model';
import { EnvironnementService } from 'src/app/service/environnementService';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { take } from 'rxjs/operators';
import { ModalService } from 'src/app/service/modal.Service';

@Component({
  selector: 'app-header-jeu',
  templateUrl: './header-jeu.component.html',
  styleUrls: ['./header-jeu.component.scss']
})
export class HeaderJeuComponent implements OnInit {

  environnement: Environnement | null = null;
  civilisation: Civilisation | null = null;

  constructor(private environnementService: EnvironnementService, private civilisationService: CivilisationService, private modalService: ModalService) {}


  
  ngOnInit() {

    this.loadEnvironnement();
    this.loadCivilisation();
  }

  private loadEnvironnement() {
    if (localStorage.getItem('environnement')) {
      const savedEnv = localStorage.getItem('environnement');
      if (savedEnv != null) {
        this.environnement = JSON.parse(savedEnv);
      }
    } else {
      console.log("savedEnv");
      this.environnementService.currentEnvironnement.subscribe(environnement => {
        this.environnement = environnement;
        localStorage.setItem('environnement', JSON.stringify(environnement));
      });
    }
  }

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

  openModal() {
    this.modalService.open();
    
  }
  
}