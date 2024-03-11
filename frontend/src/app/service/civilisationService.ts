import { Injectable } from '@angular/core';
import { Civilisation } from '../model/civilisation.model';

@Injectable({
  providedIn: 'root'
})
export class CivilisationService {
  private civilisation: Civilisation | null = null;

  setCivilisation(civilisation: Civilisation): void {
    this.civilisation = civilisation;
  }

  getCivilisation(): Civilisation | null {
    return this.civilisation;
  }
}
