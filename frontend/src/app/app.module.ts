import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { DragAndDropModule } from 'angular-draggable-droppable';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { HttpClientModule } from '@angular/common/http';
import { MenuDemarrageComponent } from './menu/menu-demarrage/menu-demarrage.component';
import { MenuEnvironnementComponent } from './menu/menu-environnement/menu-environnement.component';
import { PageAccueilComponent } from './menu/page-accueil/page-accueil.component';
import { PageJeuComponent } from './game/page-jeu/page-jeu.component';
import { HeaderJeuComponent } from './game/header-jeu/header-jeu.component';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { ModalArbreRechercheComponent } from './game/modal-arbre-recherche/modal-arbre-recherche.component';
import { FriseChronologiqueComponent } from './game/frise-chronologique/frise-chronologique.component';
import { MenuBurgerComponent } from './game/menu-burger/menu-burger.component';
import { ApercueComponent } from './game/game-components/apercue/apercue.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { SortRecherchesById } from './sorts/sort-recherches-by-id.pipe';


@NgModule({
  declarations: [
    AppComponent,
    MenuDemarrageComponent,
    MenuEnvironnementComponent,
    PageAccueilComponent,
    PageJeuComponent,
    ModalArbreRechercheComponent,
    FriseChronologiqueComponent,
    HeaderJeuComponent,
    MenuBurgerComponent,
    ApercueComponent,
    SortRecherchesById
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    DragAndDropModule,
    NgbModule,
    BrowserAnimationsModule
  ],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
  bootstrap: [AppComponent]
})
export class AppModule { }
