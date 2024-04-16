export interface Ressource {
    nom: string;
    description: string;
    quantite: number;
    max: number;
    type: string;
    valeurEchange: number;
    image: string;
    Coût: ValeurRessource[];
}

export interface ValeurRessource {
    nom: string;
    val: string;
}

export interface Ressources {
    [key: string]: Ressource;
}