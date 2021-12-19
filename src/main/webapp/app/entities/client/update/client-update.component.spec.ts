jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClientService } from '../service/client.service';
import { IClient, Client } from '../client.model';
import { ITiers } from 'app/entities/tiers/tiers.model';
import { TiersService } from 'app/entities/tiers/service/tiers.service';
import { IConseiller } from 'app/entities/conseiller/conseiller.model';
import { ConseillerService } from 'app/entities/conseiller/service/conseiller.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

import { ClientUpdateComponent } from './client-update.component';

describe('Component Tests', () => {
  describe('Client Management Update Component', () => {
    let comp: ClientUpdateComponent;
    let fixture: ComponentFixture<ClientUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clientService: ClientService;
    let tiersService: TiersService;
    let conseillerService: ConseillerService;
    let banqueService: BanqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClientUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clientService = TestBed.inject(ClientService);
      tiersService = TestBed.inject(TiersService);
      conseillerService = TestBed.inject(ConseillerService);
      banqueService = TestBed.inject(BanqueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call tiers query and add missing value', () => {
        const client: IClient = { id: 456 };
        const tiers: ITiers = { id: 83931 };
        client.tiers = tiers;

        const tiersCollection: ITiers[] = [{ id: 30839 }];
        spyOn(tiersService, 'query').and.returnValue(of(new HttpResponse({ body: tiersCollection })));
        const expectedCollection: ITiers[] = [tiers, ...tiersCollection];
        spyOn(tiersService, 'addTiersToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(tiersService.query).toHaveBeenCalled();
        expect(tiersService.addTiersToCollectionIfMissing).toHaveBeenCalledWith(tiersCollection, tiers);
        expect(comp.tiersCollection).toEqual(expectedCollection);
      });

      it('Should call Conseiller query and add missing value', () => {
        const client: IClient = { id: 456 };
        const conseiller: IConseiller = { id: 8567 };
        client.conseiller = conseiller;

        const conseillerCollection: IConseiller[] = [{ id: 65355 }];
        spyOn(conseillerService, 'query').and.returnValue(of(new HttpResponse({ body: conseillerCollection })));
        const additionalConseillers = [conseiller];
        const expectedCollection: IConseiller[] = [...additionalConseillers, ...conseillerCollection];
        spyOn(conseillerService, 'addConseillerToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(conseillerService.query).toHaveBeenCalled();
        expect(conseillerService.addConseillerToCollectionIfMissing).toHaveBeenCalledWith(conseillerCollection, ...additionalConseillers);
        expect(comp.conseillersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Banque query and add missing value', () => {
        const client: IClient = { id: 456 };
        const banque: IBanque = { id: 77851 };
        client.banque = banque;

        const banqueCollection: IBanque[] = [{ id: 57886 }];
        spyOn(banqueService, 'query').and.returnValue(of(new HttpResponse({ body: banqueCollection })));
        const additionalBanques = [banque];
        const expectedCollection: IBanque[] = [...additionalBanques, ...banqueCollection];
        spyOn(banqueService, 'addBanqueToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(banqueService.query).toHaveBeenCalled();
        expect(banqueService.addBanqueToCollectionIfMissing).toHaveBeenCalledWith(banqueCollection, ...additionalBanques);
        expect(comp.banquesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const client: IClient = { id: 456 };
        const tiers: ITiers = { id: 77479 };
        client.tiers = tiers;
        const conseiller: IConseiller = { id: 97092 };
        client.conseiller = conseiller;
        const banque: IBanque = { id: 41487 };
        client.banque = banque;

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(client));
        expect(comp.tiersCollection).toContain(tiers);
        expect(comp.conseillersSharedCollection).toContain(conseiller);
        expect(comp.banquesSharedCollection).toContain(banque);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const client = { id: 123 };
        spyOn(clientService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clientService.update).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const client = new Client();
        spyOn(clientService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(clientService.create).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const client = { id: 123 };
        spyOn(clientService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clientService.update).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTiersById', () => {
        it('Should return tracked Tiers primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTiersById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackConseillerById', () => {
        it('Should return tracked Conseiller primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackConseillerById(0, entity);
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
