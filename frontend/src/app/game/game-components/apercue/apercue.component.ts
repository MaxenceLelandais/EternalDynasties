import { Component, Input, OnInit } from '@angular/core';
import { Environnement } from 'src/app/model/environnement.model';
import { EnvironnementService } from 'src/app/service/environnementService';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';

@Component({
  selector: 'app-apercue',
  templateUrl: './apercue.component.html',
  styleUrls: ['./apercue.component.scss']
})
export class ApercueComponent implements OnInit {
  @Input()
  data:any;

  environnement: Environnement | null = null;
  civilisation: Civilisation | null = null;

  constructor(private environnementService: EnvironnementService, private civilisationService: CivilisationService) {}


  
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
}
