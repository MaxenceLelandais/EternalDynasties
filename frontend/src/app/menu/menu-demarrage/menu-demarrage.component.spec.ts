import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuDemarrageComponent } from './menu-demarrage.component';

describe('MenuDemarrageComponent', () => {
  let component: MenuDemarrageComponent;
  let fixture: ComponentFixture<MenuDemarrageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MenuDemarrageComponent]
    });
    fixture = TestBed.createComponent(MenuDemarrageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
