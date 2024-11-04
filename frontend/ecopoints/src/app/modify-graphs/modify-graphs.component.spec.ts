import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyGraphsComponent } from './modify-graphs.component';

describe('ModifyGraphsComponent', () => {
  let component: ModifyGraphsComponent;
  let fixture: ComponentFixture<ModifyGraphsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyGraphsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyGraphsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
