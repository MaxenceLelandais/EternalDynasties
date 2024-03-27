import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrappableComponent } from './drappable.component';

describe('DrappableComponent', () => {
  let component: DrappableComponent;
  let fixture: ComponentFixture<DrappableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DrappableComponent]
    });
    fixture = TestBed.createComponent(DrappableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
