import { Component, OnInit } from '@angular/core';;
import { Environnement } from '../model/environnement.model';
import { EnvironnementService } from '../service/environnementService';

@Component({
  selector: 'app-music',
  templateUrl: './music.component.html',
  styleUrls: ['./music.component.scss']
})

export class MusicComponent implements OnInit {

  audioFiles: string[] = [];
  currentAudioIndex: number = 0;
  environnement: Environnement | null = null;

  constructor(private environnementService: EnvironnementService) {}

  ngOnInit(): void {
    this.loadAudioFiles();
    this.loadEnvironnement();
  }

  loadAudioFiles() {
    this.audioFiles = ['1.mp3', '2.mp3', '3.mp3', '4.mp3'];
  }

  loadEnvironnement() {
    if (localStorage.getItem('environnement')) {
      const savedEnv = localStorage.getItem('environnement');
      if (savedEnv != null) {
        this.environnement = JSON.parse(savedEnv);
      }
    } else {
      console.log("savedEnv");
      console.log("savedEnv");
      this.environnementService.currentEnvironnement.subscribe(environnement => {
        this.environnement = environnement;
        localStorage.setItem('environnement', JSON.stringify(environnement));
      });
    }
  }

  playRandomAudio() {
    const randomIndex = Math.floor(Math.random() * this.audioFiles.length);
    this.currentAudioIndex = randomIndex;
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    audioPlayer.src = `assets/music/${this.audioFiles[randomIndex]}`;
    audioPlayer.play();
  }

  nextAudio() {
    this.currentAudioIndex = (this.currentAudioIndex + 1) % this.audioFiles.length;
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    audioPlayer.src = `assets/music/${this.audioFiles[this.currentAudioIndex]}`;
    audioPlayer.play();
  }

  previousAudio() {
    this.currentAudioIndex = (this.currentAudioIndex - 1 + this.audioFiles.length) % this.audioFiles.length;
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    audioPlayer.src = `assets/music/${this.audioFiles[this.currentAudioIndex]}`;
    audioPlayer.play();
  }
}
