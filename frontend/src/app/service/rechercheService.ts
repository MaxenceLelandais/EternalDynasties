import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { Recherches } from '../model/recherche.model';

@Injectable({
    providedIn: 'root'
})

export class RechercheService {
    private recherches: Recherches | null = null;
    private _recherches = new BehaviorSubject<Recherches | null>(null);
    public recherches$ = this._recherches.asObservable();

    constructor() { }

    setRecherches(recherches: Recherches): void {
        this.recherches = recherches;
      }
    
      getRecherches(): Recherches | null {
        return this.recherches;
      }

      updateRecherches(recherches: Recherches): void {
        this._recherches.next(recherches);
      }
}