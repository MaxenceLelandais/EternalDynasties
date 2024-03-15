import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { HttpClientModule } from '@angular/common/http';
import { MenuDemarrageComponent } from './menu/menu-demarrage/menu-demarrage.component';
import { MenuEnvironnementComponent } from './menu/menu-environnement/menu-environnement.component';
import { PageAccueilComponent } from './menu/page-accueil/page-accueil.component';
import { PageJeuComponent } from './game/page-jeu/page-jeu.component';
import { HeaderJeuComponent } from './game/header-jeu/header-jeu.component';
import { Test } from './http/test';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
<<<<<<< HEAD
import { ModalArbreRechercheComponent } from './game/modal-arbre-recherche/modal-arbre-recherche.component';
import { FriseChronologiqueComponent } from './game/frise-chronologique/frise-chronologique.component';
=======
import { ButtonComponent } from './button/button.component';
import { MenuBurgerComponent } from './game/menu-burger/menu-burger.component';
import { DragAndDropModule } from 'angular-draggable-droppable';
>>>>>>> be78f7eb72a42c17d7021421fdabad5efa23f360

@NgModule({
  declarations: [
    AppComponent,
    MenuDemarrageComponent,
    MenuEnvironnementComponent,
    PageAccueilComponent,
    PageJeuComponent,
    Test,
    HeaderJeuComponent,
<<<<<<< HEAD
    ModalArbreRechercheComponent,
    FriseChronologiqueComponent
=======
    ButtonComponent,
    MenuBurgerComponent
>>>>>>> be78f7eb72a42c17d7021421fdabad5efa23f360
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    DragAndDropModule
  ],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
  bootstrap: [AppComponent]
})
export class AppModule { }
