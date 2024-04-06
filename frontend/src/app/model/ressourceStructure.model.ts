export interface RessourceStructure {
    nom: string;
    valeur: number;
    max: number;
    valeurEchange: number;
}

export interface RessourceStructures {
    [key: string]: RessourceStructure;
}

class RessourceJeu implements RessourceStructure {
    nom: string;
    valeur: number;
    max: number;
    valeurEchange: number;

    constructor(nom: string, valeur: number, max: number, valeurEchange: number) {
        this.nom = nom;
        this.valeur = valeur;
        this.max = max;
        this.valeurEchange = valeurEchange;
    }
}

class RessourceBase implements RessourceStructure {
    nom: string;
    valeur: number;
    max: number;
    valeurEchange: number;

    constructor(nom: string, valeur: number, max: number,valeurEchange: number) {
        this.nom = nom;
        this.valeur = valeur;
        this.max = max;
        this.valeurEchange = valeurEchange
    }

}


class RessourceSpecialite implements RessourceStructure {
    nom: string;
    valeur: number;
    max: number;
    valeurEchange: number;

    constructor(nom: string, valeur: number, max: number,valeurEchange: number) {
        this.nom = nom;
        this.valeur = valeur;
        this.max = max;
        this.valeurEchange = valeurEchange
    }
}


class Batiment implements RessourceStructure {
    nom: string;
    valeur: number;
    max: number;
    valeurEchange: number;

    constructor(nom: string, valeur: number, max: number,valeurEchange: number) {
        this.nom = nom;
        this.valeur = valeur;
        this.max = max;
        this.valeurEchange = valeurEchange
    }
}


class Metier implements RessourceStructure {
    nom: string;
    valeur: number;
    max: number;
    valeurEchange: number;

    constructor(nom: string, valeur: number, max: number,valeurEchange: number) {
        this.nom = nom;
        this.valeur = valeur;
        this.max = max;
        this.valeurEchange = valeurEchange
    }
}
