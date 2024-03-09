import { JeuService } from '../http/jeuService';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'test-http',
  templateUrl: './test.html'
})
export class Test  {

  //le $ signifi que c'est un observeur
  listeRecherches$!: Observable<any>;
  listeEnvironnements$!: Observable<any>;
  parties$!: Observable<any>;
  recherchesDisponibles$!: Observable<any>;
  listeRessources$!: Observable<any>;
  addRessources$!: Observable<any>;
  tick$!: Observable<any>;

  constructor(private jeuService: JeuService) {}


  fetchData(){
    this.listeRecherches$ = this.jeuService.httpListeRecherches();
    this.listeEnvironnements$ = this.jeuService.httpListeEnvironnements();
    this.parties$ = this.jeuService.httpParties();
    this.recherchesDisponibles$ = this.jeuService.httpRecherchesDisponibles("max");
    this.listeRessources$ = this.jeuService.httpListeRessources("max");
    this.addRessources$ = this.jeuService.httpAddRessource("max","Bois");
    this.tick$ = this.jeuService.httpTick("max");
  }
}
