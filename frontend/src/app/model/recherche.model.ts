import { Ere } from "./ere.model";

export interface Recherche {
    id: number;
    parent: number;
    nom: string;
    description: string;
    icone: string;
    cout: string;
    bonus: string;
    debloque: Recherche[];
    verouille: Recherche[];
    ere: Ere;
  }
  
  export interface Recherches {
    [key: string]: Recherche;
  }
