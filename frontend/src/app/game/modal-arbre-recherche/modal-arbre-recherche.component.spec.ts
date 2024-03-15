import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalArbreRechercheComponent } from './modal-arbre-recherche.component';

describe('ModalArbreRechercheComponent', () => {
  let component: ModalArbreRechercheComponent;
  let fixture: ComponentFixture<ModalArbreRechercheComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModalArbreRechercheComponent]
    });
    fixture = TestBed.createComponent(ModalArbreRechercheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
