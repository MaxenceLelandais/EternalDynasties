import { Component, OnInit } from '@angular/core';
import { Environnement } from '../model/environnement.model';
import { EnvironnementService } from '../service/environnementService';

@Component({
  selector: 'app-musique',
  templateUrl: './musique.component.html',
  styleUrls: ['./musique.component.scss']
})
export class MusiqueComponent implements OnInit {

  isPaused: boolean = true;
  environnement: Environnement | null = null;

  constructor(private environnementService: EnvironnementService) {}

  ngOnInit(): void {
    this.loadEnvironnement();
  }

  loadEnvironnement() {
    if (localStorage.getItem('environnement')) {
      const savedEnv = localStorage.getItem('environnement');
      if (savedEnv != null) {
        this.environnement = JSON.parse(savedEnv);
        // Maintenant, nous pouvons lire le premier fichier audio lors du chargement de l'environnement
        this.playAudio('1.mp3');
      }
    } else {
      this.environnementService.currentEnvironnement.subscribe(environnement => {
        this.environnement = environnement;
        localStorage.setItem('environnement', JSON.stringify(environnement));
        // Maintenant, nous pouvons lire le premier fichier audio lors de la mise à jour de l'environnement
        this.playAudio('1.mp3');
      });
    }
  }

  togglePause(): void {
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    if (this.isPaused) {
      audioPlayer.pause();
    } else {
      audioPlayer.play();
    }
    this.isPaused = !this.isPaused;
  }
  

  playAudio(fileName: string): void {
    if (this.environnement) {
      const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
      audioPlayer.src = `assets/music/${this.environnement.nom}/${fileName}`;
      audioPlayer.load();
      if (!this.isPaused) {
        audioPlayer.play();
      }
    }
  }
}
