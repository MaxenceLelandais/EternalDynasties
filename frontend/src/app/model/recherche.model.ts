import { Ere } from "./ere.model";

export interface Recherche {
    nom: string;
    description: string;
    icone: string;
    cout: string;
    bonus: string;
    dobloque: Recherche[];
    verouille: Recherche[];
    ere: Ere;
  }
  
  export interface Recherches {
    [key: string]: Recherche;
  }