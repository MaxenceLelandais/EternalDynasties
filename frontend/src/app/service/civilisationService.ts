import { Injectable } from '@angular/core';
import { Civilisation } from '../model/civilisation.model';

@Injectable({
  providedIn: 'root'
})
export class CivilisationService {
  private civilisation: Civilisation | null = null;

  setCivilisation(civilisation: Civilisation): void {
    sessionStorage.setItem('nom', civilisation.nom);
    sessionStorage.setItem('nomEnvironnement', civilisation.nomEnvironnement);
    this.civilisation = civilisation;
  }

  getCivilisation(): Civilisation | null {
    if (this.civilisation === null) {
      this.civilisation = new Civilisation();
      const nom = sessionStorage.getItem('nom');
      const nomEnvironnement = sessionStorage.getItem('nomEnvironnement');
      if (nom !== null && nomEnvironnement !== null) {
        this.civilisation.nom = nom;
        this.civilisation.nomEnvironnement = nomEnvironnement;
      }
    }

    return this.civilisation;
  }
}
