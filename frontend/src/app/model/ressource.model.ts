export interface Ressource {
    nom: string;
    quantite: number;
    max: number;
    type: string;
    valeurEchange: number;
}

export interface Ressources {
    [key: string]: Ressource;
  }