import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderJeuComponent } from './header-jeu.component';

describe('HeaderJeuComponent', () => {
  let component: HeaderJeuComponent;
  let fixture: ComponentFixture<HeaderJeuComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderJeuComponent]
    });
    fixture = TestBed.createComponent(HeaderJeuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
