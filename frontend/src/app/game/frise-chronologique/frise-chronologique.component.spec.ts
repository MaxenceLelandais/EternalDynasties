import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriseChronologiqueComponent } from './frise-chronologique.component';

describe('FriseChronologiqueComponent', () => {
  let component: FriseChronologiqueComponent;
  let fixture: ComponentFixture<FriseChronologiqueComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FriseChronologiqueComponent]
    });
    fixture = TestBed.createComponent(FriseChronologiqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
