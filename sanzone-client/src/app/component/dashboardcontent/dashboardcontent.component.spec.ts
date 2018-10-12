
import { fakeAsync, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardcontentComponent } from './dashboardcontent.component';

describe('DashboardcontentComponent', () => {
  let component: DashboardcontentComponent;
  let fixture: ComponentFixture<DashboardcontentComponent>;

  beforeEach(fakeAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardcontentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardcontentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
