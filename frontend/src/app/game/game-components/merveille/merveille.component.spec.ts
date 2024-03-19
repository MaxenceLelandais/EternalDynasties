import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MerveilleComponent } from './merveille.component';

describe('MerveilleComponent', () => {
  let component: MerveilleComponent;
  let fixture: ComponentFixture<MerveilleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MerveilleComponent]
    });
    fixture = TestBed.createComponent(MerveilleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
