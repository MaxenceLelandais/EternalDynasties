export interface Ressource {
    nom: string;
    valeur: number;
}

export interface Ressources {
    [key: string]: Ressource;
  }