import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConseillerDetailComponent } from './conseiller-detail.component';

describe('Component Tests', () => {
  describe('Conseiller Management Detail Component', () => {
    let comp: ConseillerDetailComponent;
    let fixture: ComponentFixture<ConseillerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConseillerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ conseiller: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConseillerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConseillerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load conseiller on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.conseiller).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
