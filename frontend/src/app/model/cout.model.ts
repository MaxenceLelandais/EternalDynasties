export interface Bonus {
    quantite: number;
    pourcentage: number;
    quantiteParSecondes: number;
    pourcentageParSecondes: string;
}
export interface LesBonus {
  [key: string]: Bonus;
}