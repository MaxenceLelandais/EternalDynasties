import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuissancesComponent } from './puissances.component';

describe('PuissancesComponent', () => {
  let component: PuissancesComponent;
  let fixture: ComponentFixture<PuissancesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PuissancesComponent]
    });
    fixture = TestBed.createComponent(PuissancesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
