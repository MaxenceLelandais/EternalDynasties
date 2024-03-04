import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageAccueilComponent } from './page-accueil/page-accueil.component';
import { MenuEnvironnementComponent } from './menu-environnement/menu-environnement.component';
import { PageJeuComponent } from './page-jeu/page-jeu.component';

const routes: Routes = [
  { path: '', component: PageAccueilComponent },
  { path: 'menu-environnement', component: MenuEnvironnementComponent },
  { path: 'page-jeu', component: PageJeuComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
