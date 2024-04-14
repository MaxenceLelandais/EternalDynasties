import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApercueComponent } from './apercue.component';

describe('ApercueComponent', () => {
  let component: ApercueComponent;
  let fixture: ComponentFixture<ApercueComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApercueComponent]
    });
    fixture = TestBed.createComponent(ApercueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
