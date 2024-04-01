import { Injectable } from "@angular/core";
import { Ressources } from "../model/ressource.model";

@Injectable({
    providedIn: 'root'
})

export class RessourceService {
    private ressources: Ressources | null = null;

    setRessources(ressources: Ressources): void {
        this.ressources = ressources;
      }
    
      getRessources(): Ressources | null {
        return this.ressources;
      }
}