import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuEnvironnementComponent } from './menu-environnement.component';

describe('MenuFactionComponent', () => {
  let component: MenuEnvironnementComponent;
  let fixture: ComponentFixture<MenuEnvironnementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MenuEnvironnementComponent]
    });
    fixture = TestBed.createComponent(MenuEnvironnementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
