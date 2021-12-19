jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BanqueService } from '../service/banque.service';
import { IBanque, Banque } from '../banque.model';

import { BanqueUpdateComponent } from './banque-update.component';

describe('Component Tests', () => {
  describe('Banque Management Update Component', () => {
    let comp: BanqueUpdateComponent;
    let fixture: ComponentFixture<BanqueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let banqueService: BanqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BanqueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BanqueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BanqueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      banqueService = TestBed.inject(BanqueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const banque: IBanque = { id: 456 };

        activatedRoute.data = of({ banque });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(banque));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const banque = { id: 123 };
        spyOn(banqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ banque });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: banque }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(banqueService.update).toHaveBeenCalledWith(banque);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const banque = new Banque();
        spyOn(banqueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ banque });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: banque }));
        saveSubject.complete();

        // THEN
        expect(banqueService.create).toHaveBeenCalledWith(banque);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const banque = { id: 123 };
        spyOn(banqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ banque });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(banqueService.update).toHaveBeenCalledWith(banque);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
