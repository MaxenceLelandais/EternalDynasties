import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  audioFiles: string[] = []; // Array to hold the list of audio files
  currentAudioIndex: number = 0; // Index of the currently playing audio file

  constructor() { }

  ngOnInit(): void {
    this.loadAudioFiles(); // Load the list of audio files when the component initializes
  }

  loadAudioFiles() {
    // Assuming you have an array of file names in your assets folder
    this.audioFiles = ['1.mp3', '2.mp3', '3.mp3', '4.mp3']; // Add all your audio file names here
  }

  playRandomAudio() {
    const randomIndex = Math.floor(Math.random() * this.audioFiles.length); // Generate a random index
    this.currentAudioIndex = randomIndex; // Update the current audio index
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    audioPlayer.src = `assets/music/${this.audioFiles[randomIndex]}`; // Set the source of the audio player
    audioPlayer.play(); // Play the audio
  }

  nextAudio() {
    this.currentAudioIndex = (this.currentAudioIndex + 1) % this.audioFiles.length; // Move to the next audio file
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    audioPlayer.src = `assets/music/${this.audioFiles[this.currentAudioIndex]}`; // Set the source of the audio player
    audioPlayer.play(); // Play the audio
  }

  previousAudio() {
    this.currentAudioIndex = (this.currentAudioIndex - 1 + this.audioFiles.length) % this.audioFiles.length; // Move to the previous audio file
    const audioPlayer = document.getElementById('audioPlayer') as HTMLAudioElement;
    audioPlayer.src = `assets/music/${this.audioFiles[this.currentAudioIndex]}`; // Set the source of the audio player
    audioPlayer.play(); // Play the audio
  }
}
