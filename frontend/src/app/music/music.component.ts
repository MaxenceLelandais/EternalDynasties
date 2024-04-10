import { Component, OnInit } from '@angular/core';
import { Environnement } from '../model/environnement.model';
import { EnvironnementService } from '../service/environnementService';
import { EreService } from '../service/ere.service'; 
import { JeuService } from '../http/jeuService';
import { CivilisationService } from '../service/civilisationService';

@Component({
  selector: 'app-music',
  templateUrl: './music.component.html',
  styleUrls: ['./music.component.scss']
})

export class MusicComponent implements OnInit {

  audioFiles: string[] = [];
  etat: boolean = true;
  environnement: Environnement | null = null;
  ereActuelle: string = ""; // Variable pour stocker l'ère actuelle
  civilisation:any;

  constructor(
    private environnementService: EnvironnementService,
    private ereService: EreService,
    private jeuService: JeuService, 
    private civilisationService: CivilisationService // Injection du service EreService
  ) {}
  
  ngOnInit(): void {
    this.loadAudioFiles();
    this.loadEnvironnement();
    this.loadEreActuelle(); // Appel à la méthode pour charger l'ère actuelle
    this.playAudioAtIndex();
  }

  loadAudioFiles() {
    this.audioFiles = ['1.mp3', '2.mp3', '3.mp3', '4.mp3'];
  }

  loadEnvironnement() {
    if (localStorage.getItem('environnement')) {
      const savedEnv = localStorage.getItem('environnement');
      if (savedEnv !== null) {
        this.environnement = JSON.parse(savedEnv);
      }
    } else {
      this.environnementService.currentEnvironnement.subscribe((environnement: Environnement | null) => {
        if (environnement !== null) {
          this.environnement = environnement;
          localStorage.setItem('environnement', JSON.stringify(environnement));
        }
      });
    }
  }
  
  loadEreActuelle() {
    if(this.civilisationService.getCivilisation()!=null){
      this.civilisation = this.civilisationService.getCivilisation();
      this.jeuService.httpEreActuelle(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement).subscribe(
        ere => {
          this.ereActuelle = ere["nom"];
          localStorage.setItem('ereActuelle', ere["nom"]);
        },
        error => {
          console.error("Erreur lors de l'ajout de la ressource", error);
        }
      );
    }
  }

  private playAudioAtIndex() {
    try{
      const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
      audioPlayer.src = `assets/music/${this.environnement?.nom}/${this.ereActuelle}/1.mp3`;
      audioPlayer.loop = true; // Activer la boucle
      audioPlayer.addEventListener('ended', () => {
        audioPlayer.currentTime = 0; // Revenir au début de la piste
        audioPlayer.play(); // Relancer la lecture
      });
      audioPlayer.play();
    }catch{

    }
  }

  public pauseAudio() {
    try {
      const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
      audioPlayer.pause(); // Mettre en pause la lecture
      this.etat = true;
    } catch (error) {
      console.error('Une erreur s\'est produite lors de la mise en pause de la musique :', error);
    }
  }
  
  public resumeAudio() {
    try {
      const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
      audioPlayer.play(); // Reprendre la lecture
      this.etat = false;
    } catch (error) {
      console.error('Une erreur s\'est produite lors de la reprise de la lecture de la musique :', error);
    }
  }
}
