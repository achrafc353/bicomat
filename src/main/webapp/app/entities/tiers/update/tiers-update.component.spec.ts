jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TiersService } from '../service/tiers.service';
import { ITiers, Tiers } from '../tiers.model';

import { TiersUpdateComponent } from './tiers-update.component';

describe('Component Tests', () => {
  describe('Tiers Management Update Component', () => {
    let comp: TiersUpdateComponent;
    let fixture: ComponentFixture<TiersUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tiersService: TiersService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TiersUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TiersUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TiersUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tiersService = TestBed.inject(TiersService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tiers: ITiers = { id: 456 };

        activatedRoute.data = of({ tiers });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tiers));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tiers = { id: 123 };
        spyOn(tiersService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tiers });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tiers }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tiersService.update).toHaveBeenCalledWith(tiers);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tiers = new Tiers();
        spyOn(tiersService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tiers });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tiers }));
        saveSubject.complete();

        // THEN
        expect(tiersService.create).toHaveBeenCalledWith(tiers);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tiers = { id: 123 };
        spyOn(tiersService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tiers });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tiersService.update).toHaveBeenCalledWith(tiers);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
