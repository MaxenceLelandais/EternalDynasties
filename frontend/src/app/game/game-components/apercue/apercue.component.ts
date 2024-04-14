import { Component, Input, OnInit } from '@angular/core';
import { Environnement } from 'src/app/model/environnement.model';
import { EnvironnementService } from 'src/app/service/environnementService';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';
import { Ere, Eres } from 'src/app/model/ere.model';

@Component({
  selector: 'app-apercue',
  templateUrl: './apercue.component.html',
  styleUrls: ['./apercue.component.scss']
})
export class ApercueComponent implements OnInit {
  @Input()
  data:any;

  environnement: Environnement | null = null;
  civilisation: Civilisation | null = null;
  listeEre: Eres | null = null;
  nomJoueur: string | null = null;
  ere: Ere | null = null;
  idEre: number | null = null;

  constructor(private environnementService: EnvironnementService,
              private civilisationService: CivilisationService,
              private jeuService: JeuService,
              private nomJoueurService: NomJoueurService) {}


  
  ngOnInit() {
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
    this.fetchData();
    this.loadEnvironnement();
    this.loadCivilisation();
  }

  fetchData() {
    this.jeuService.httpListeEres().subscribe(
      data => {
        this.listeEre = data["Ere"];
      },
      error => {
        console.error("Erreur lors de la récupération des eres", error);
      }
    );
    if (this.nomJoueur) {
      this.jeuService.httpEre(this.nomJoueur).subscribe({
        next: (response) => {
          this.ere = response;
          if (this.listeEre && this.ere) {
            const ereValues = Object.values(this.listeEre);
            const ereMatch = ereValues.find(ereItem => ereItem.nom === this.ere!.nom);
            if (ereMatch) {
              this.idEre = ereMatch.id;
            } else {
              console.log('Aucune correspondance trouvée pour cette ère dans la liste');
            }
          }
          
        },
        error: (error) => {
          console.error('Erreur lors de la requête', error);
        }
      });
    }
  }

  private loadEnvironnement() {
    if (localStorage.getItem('environnement')) {
      const savedEnv = localStorage.getItem('environnement');
      if (savedEnv != null) {
        this.environnement = JSON.parse(savedEnv);
      }
    } else {
      console.log("savedEnv");
      this.environnementService.currentEnvironnement.subscribe(environnement => {
        this.environnement = environnement;
        localStorage.setItem('environnement', JSON.stringify(environnement));
      });
    }
  }

  private loadCivilisation() {
    if (localStorage.getItem('civilisation')) {
      const savedCivilisation = localStorage.getItem('civilisation');
      if (savedCivilisation != null) {
        this.civilisation = JSON.parse(savedCivilisation);
      }
    } else {
      console.log("savedCivilisation");
      this.civilisation = this.civilisationService.getCivilisation();
      localStorage.setItem('civilisation', JSON.stringify(this.civilisation));
    }
  }
}
