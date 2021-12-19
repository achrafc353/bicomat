import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TiersDetailComponent } from './tiers-detail.component';

describe('Component Tests', () => {
  describe('Tiers Management Detail Component', () => {
    let comp: TiersDetailComponent;
    let fixture: ComponentFixture<TiersDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TiersDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tiers: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TiersDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TiersDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tiers on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tiers).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
