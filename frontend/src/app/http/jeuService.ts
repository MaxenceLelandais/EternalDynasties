import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class JeuService {
  private apiUrl = 'jeu/'; // Remplacez par l'URL de votre API
  private responseDataSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient) {}

  // Fonction pour interroger l'API
  private fetchData(url: string): Observable<any> {
    return this.http.get(url);
  }

  // Observable pour surveiller les réponses de l'API
  httpListeRecherches(): Observable<any> {
    return this.fetchData(this.apiUrl+"listeRecherches");
  }
  httpParties(): Observable<any> {
    return this.fetchData(this.apiUrl+"parties");
  }
  httpRecherchesDisponibles(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"recherchesDisponibles?nomJoueur="+nomJoueur);
  }
  httpActiverRecherche(nomJoueur:string,recherche:string): Observable<any> {
    return this.fetchData(this.apiUrl+"activerRecherche?nomJoueur="+nomJoueur+"&recherche="+recherche);
  }
  httpListeRessources(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"listeRessources?nomJoueur="+nomJoueur);
  }
  httpAddRessource(nomJoueur:string,ressource:string): Observable<any> {
    return this.fetchData(this.apiUrl+"addRessource?nomJoueur="+nomJoueur+"&ressource="+ressource);
  }
  httpTick(nomJoueur:string): Observable<any> {
    return this.fetchData(this.apiUrl+"tick?nomJoueur="+nomJoueur);
  }
}