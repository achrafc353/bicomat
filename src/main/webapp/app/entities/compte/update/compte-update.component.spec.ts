jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompteService } from '../service/compte.service';
import { ICompte, Compte } from '../compte.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

import { CompteUpdateComponent } from './compte-update.component';

describe('Component Tests', () => {
  describe('Compte Management Update Component', () => {
    let comp: CompteUpdateComponent;
    let fixture: ComponentFixture<CompteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let compteService: CompteService;
    let clientService: ClientService;
    let banqueService: BanqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      compteService = TestBed.inject(CompteService);
      clientService = TestBed.inject(ClientService);
      banqueService = TestBed.inject(BanqueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const compte: ICompte = { id: 456 };
        const client: IClient = { id: 9959 };
        compte.client = client;

        const clientCollection: IClient[] = [{ id: 76061 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ compte });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Banque query and add missing value', () => {
        const compte: ICompte = { id: 456 };
        const banque: IBanque = { id: 72474 };
        compte.banque = banque;

        const banqueCollection: IBanque[] = [{ id: 6813 }];
        spyOn(banqueService, 'query').and.returnValue(of(new HttpResponse({ body: banqueCollection })));
        const additionalBanques = [banque];
        const expectedCollection: IBanque[] = [...additionalBanques, ...banqueCollection];
        spyOn(banqueService, 'addBanqueToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ compte });
        comp.ngOnInit();

        expect(banqueService.query).toHaveBeenCalled();
        expect(banqueService.addBanqueToCollectionIfMissing).toHaveBeenCalledWith(banqueCollection, ...additionalBanques);
        expect(comp.banquesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const compte: ICompte = { id: 456 };
        const client: IClient = { id: 55845 };
        compte.client = client;
        const banque: IBanque = { id: 20506 };
        compte.banque = banque;

        activatedRoute.data = of({ compte });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(compte));
        expect(comp.clientsSharedCollection).toContain(client);
        expect(comp.banquesSharedCollection).toContain(banque);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const compte = { id: 123 };
        spyOn(compteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ compte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: compte }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(compteService.update).toHaveBeenCalledWith(compte);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const compte = new Compte();
        spyOn(compteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ compte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: compte }));
        saveSubject.complete();

        // THEN
        expect(compteService.create).toHaveBeenCalledWith(compte);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const compte = { id: 123 };
        spyOn(compteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ compte });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(compteService.update).toHaveBeenCalledWith(compte);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBanqueById', () => {
        it('Should return tracked Banque primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBanqueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
