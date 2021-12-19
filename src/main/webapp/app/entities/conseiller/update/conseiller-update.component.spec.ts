jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConseillerService } from '../service/conseiller.service';
import { IConseiller, Conseiller } from '../conseiller.model';

import { ConseillerUpdateComponent } from './conseiller-update.component';

describe('Component Tests', () => {
  describe('Conseiller Management Update Component', () => {
    let comp: ConseillerUpdateComponent;
    let fixture: ComponentFixture<ConseillerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let conseillerService: ConseillerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConseillerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConseillerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConseillerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      conseillerService = TestBed.inject(ConseillerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const conseiller: IConseiller = { id: 456 };

        activatedRoute.data = of({ conseiller });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(conseiller));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const conseiller = { id: 123 };
        spyOn(conseillerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ conseiller });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: conseiller }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(conseillerService.update).toHaveBeenCalledWith(conseiller);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const conseiller = new Conseiller();
        spyOn(conseillerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ conseiller });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: conseiller }));
        saveSubject.complete();

        // THEN
        expect(conseillerService.create).toHaveBeenCalledWith(conseiller);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const conseiller = { id: 123 };
        spyOn(conseillerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ conseiller });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(conseillerService.update).toHaveBeenCalledWith(conseiller);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
