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
  currentAudioIndex: number = 0;
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
  

  playRandomAudio() {
    if (this.environnement) {
        const randomIndex = Math.floor(Math.random() * this.audioFiles.length);
        this.currentAudioIndex = randomIndex;
        this.playAudioAtIndex(randomIndex);
    }
  }

  nextAudio() {
    if (this.environnement) {
        this.currentAudioIndex = (this.currentAudioIndex + 1) % this.audioFiles.length;
        this.playAudioAtIndex(this.currentAudioIndex);
    }
  }

  previousAudio() {
    if (this.environnement) {
        this.currentAudioIndex = (this.currentAudioIndex - 1 + this.audioFiles.length) % this.audioFiles.length;
        this.playAudioAtIndex(this.currentAudioIndex);
    }
  }

  private playAudioAtIndex(index: number) {
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    audioPlayer.src = `assets/music/${this.environnement?.nom}/${this.ereActuelle}/${this.audioFiles[index]}`;
    audioPlayer.play();
  }
}
