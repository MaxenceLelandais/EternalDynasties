import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageJeuComponent } from './page-jeu.component';

describe('PageJeuComponent', () => {
  let component: PageJeuComponent;
  let fixture: ComponentFixture<PageJeuComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageJeuComponent]
    });
    fixture = TestBed.createComponent(PageJeuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
