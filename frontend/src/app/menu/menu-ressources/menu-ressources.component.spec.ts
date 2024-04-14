import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuRessourcesComponent } from './menu-ressources.component';

describe('MenuRessourcesComponent', () => {
  let component: MenuRessourcesComponent;
  let fixture: ComponentFixture<MenuRessourcesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MenuRessourcesComponent]
    });
    fixture = TestBed.createComponent(MenuRessourcesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
