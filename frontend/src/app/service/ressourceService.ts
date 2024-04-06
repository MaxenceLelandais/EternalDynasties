import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { Ressources } from '../model/ressource.model';

@Injectable({
    providedIn: 'root'
})

export class RessourceService {
    private ressources: Ressources | null = null;
    private _ressources = new BehaviorSubject<Ressources | null>(null);
    public ressources$ = this._ressources.asObservable();

    constructor() { }

    setRessources(ressources: Ressources): void {
        this.ressources = ressources;
      }
    
      getRessources(): Ressources | null {
        return this.ressources;
      }

      updateRessources(ressources: Ressources): void {
        this._ressources.next(ressources);
      }
}