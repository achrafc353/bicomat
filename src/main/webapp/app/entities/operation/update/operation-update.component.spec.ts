jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OperationService } from '../service/operation.service';
import { IOperation, Operation } from '../operation.model';
import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { OperationUpdateComponent } from './operation-update.component';

describe('Component Tests', () => {
  describe('Operation Management Update Component', () => {
    let comp: OperationUpdateComponent;
    let fixture: ComponentFixture<OperationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let operationService: OperationService;
    let compteService: CompteService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OperationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OperationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OperationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      operationService = TestBed.inject(OperationService);
      compteService = TestBed.inject(CompteService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call operationLiee query and add missing value', () => {
        const operation: IOperation = { id: 456 };
        const operationLiee: IOperation = { id: 55065 };
        operation.operationLiee = operationLiee;

        const operationLieeCollection: IOperation[] = [{ id: 97552 }];
        spyOn(operationService, 'query').and.returnValue(of(new HttpResponse({ body: operationLieeCollection })));
        const expectedCollection: IOperation[] = [operationLiee, ...operationLieeCollection];
        spyOn(operationService, 'addOperationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ operation });
        comp.ngOnInit();

        expect(operationService.query).toHaveBeenCalled();
        expect(operationService.addOperationToCollectionIfMissing).toHaveBeenCalledWith(operationLieeCollection, operationLiee);
        expect(comp.operationLieesCollection).toEqual(expectedCollection);
      });

      it('Should call Compte query and add missing value', () => {
        const operation: IOperation = { id: 456 };
        const compte: ICompte = { id: 5814 };
        operation.compte = compte;

        const compteCollection: ICompte[] = [{ id: 76957 }];
        spyOn(compteService, 'query').and.returnValue(of(new HttpResponse({ body: compteCollection })));
        const additionalComptes = [compte];
        const expectedCollection: ICompte[] = [...additionalComptes, ...compteCollection];
        spyOn(compteService, 'addCompteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ operation });
        comp.ngOnInit();

        expect(compteService.query).toHaveBeenCalled();
        expect(compteService.addCompteToCollectionIfMissing).toHaveBeenCalledWith(compteCollection, ...additionalComptes);
        expect(comp.comptesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Client query and add missing value', () => {
        const operation: IOperation = { id: 456 };
        const client: IClient = { id: 13968 };
        operation.client = client;

        const clientCollection: IClient[] = [{ id: 35333 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ operation });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const operation: IOperation = { id: 456 };
        const operationLiee: IOperation = { id: 96940 };
        operation.operationLiee = operationLiee;
        const compte: ICompte = { id: 71446 };
        operation.compte = compte;
        const client: IClient = { id: 25948 };
        operation.client = client;

        activatedRoute.data = of({ operation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(operation));
        expect(comp.operationLieesCollection).toContain(operationLiee);
        expect(comp.comptesSharedCollection).toContain(compte);
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const operation = { id: 123 };
        spyOn(operationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ operation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: operation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(operationService.update).toHaveBeenCalledWith(operation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const operation = new Operation();
        spyOn(operationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ operation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: operation }));
        saveSubject.complete();

        // THEN
        expect(operationService.create).toHaveBeenCalledWith(operation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const operation = { id: 123 };
        spyOn(operationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ operation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(operationService.update).toHaveBeenCalledWith(operation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOperationById', () => {
        it('Should return tracked Operation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOperationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCompteById', () => {
        it('Should return tracked Compte primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
