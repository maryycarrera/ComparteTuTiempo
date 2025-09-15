import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsButton } from './details-button';

describe('DetailsButton', () => {
  let component: DetailsButton;
  let fixture: ComponentFixture<DetailsButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailsButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsButton);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
