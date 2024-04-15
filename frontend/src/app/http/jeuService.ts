import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class JeuService {
  private apiUrl = 'jeu/'; // Remplacez par l'URL de votre API

  constructor(private http: HttpClient) {}

  // Fonction pour interroger l'API
  private fetchData(url: string): Observable<any> {
    return this.http.get(url, { observe: 'body', responseType: 'json'});
  }

  // Fonction pour envoyer un texte à l'API et récupérer des données en retour
  httpChargerPartie(nomJoueur:string, partie: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.post<any>(this.apiUrl+"chargerPartie?nom="+nomJoueur, { texte: partie }, { headers: headers ,responseType: 'json'});
  }

  // Observable pour surveiller les réponses de l'API
  httpListeRecherchesJoueur(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"listeRecherchesJoueur?nomJoueur=" + nomJoueur);
  }

  httpListeRecherches(): Observable<any> {
    return this.fetchData(this.apiUrl+"listeRecherches");
  }

  httpArbreRecherches(): Observable<any> {
    return this.fetchData(this.apiUrl+"arbreRecherches");
  }

  httpListeEres(): Observable<any> {
    return this.fetchData(this.apiUrl+"listeEres");
  }
  httpListeEnvironnements(): Observable<any> {
    return this.fetchData(this.apiUrl+"listeEnvironnements");
  }

  httpEnvironnement(nom: string): Observable<any> {
    return this.fetchData(this.apiUrl+"environnement?nom="+nom);
  }

  httpEres(): Observable<any> {
    return this.fetchData(this.apiUrl + "listeEres");
  }

  httpEre(nomCivilisation:string): Observable<any> {
    return this.fetchData(this.apiUrl + "ereActuelle?nomJoueur="+nomCivilisation);
  }
  httpJoueur(nomCivilisation:string, nomEnvironnement: string): Observable<any> {
    return this.fetchData(this.apiUrl+"joueur?civilisation="+nomCivilisation+"&environnement="+nomEnvironnement);
  }

  httpEreActuelle(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"ereActuelle?nomJoueur="+nomJoueur);
  }

  httpParties(): Observable<any> {
    return this.fetchData(this.apiUrl+"parties");
  }

  httpRecherchesDisponibles(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"recherchesDisponibles?nomJoueur="+nomJoueur);
  }
  httpActiverRecherche(nomJoueur:string,recherche:string): Observable<any> {
    console.log("nom du joueur : " + nomJoueur);
    return this.fetchData(this.apiUrl+"activerRecherche?nomJoueur="+nomJoueur+"&recherche="+recherche);
  }
  httpListeRessources(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"listeRessources?nomJoueur="+nomJoueur);
  }
  httpListeRessourcesComplete(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"listeRessourcesComplete?nomJoueur="+nomJoueur);
  }
  httpArbreRessources(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"arbreRessources?nomJoueur="+nomJoueur);
  }
  httpAddRessource(nomJoueur:string,ressource:string, nombre:string): Observable<any> {
    return this.fetchData(this.apiUrl+"addRessource?nomJoueur="+nomJoueur+"&ressource="+ressource+"&nombre="+nombre);
  }
  httpTick(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"tick?nomJoueur="+nomJoueur);
  }
}