jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CarteBancaireService } from '../service/carte-bancaire.service';
import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { CarteBancaireUpdateComponent } from './carte-bancaire-update.component';

describe('Component Tests', () => {
  describe('CarteBancaire Management Update Component', () => {
    let comp: CarteBancaireUpdateComponent;
    let fixture: ComponentFixture<CarteBancaireUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let carteBancaireService: CarteBancaireService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CarteBancaireUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CarteBancaireUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CarteBancaireUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      carteBancaireService = TestBed.inject(CarteBancaireService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const carteBancaire: ICarteBancaire = { id: 456 };
        const client: IClient = { id: 74732 };
        carteBancaire.client = client;

        const clientCollection: IClient[] = [{ id: 21062 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ carteBancaire });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const carteBancaire: ICarteBancaire = { id: 456 };
        const client: IClient = { id: 41151 };
        carteBancaire.client = client;

        activatedRoute.data = of({ carteBancaire });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(carteBancaire));
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carteBancaire = { id: 123 };
        spyOn(carteBancaireService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carteBancaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: carteBancaire }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(carteBancaireService.update).toHaveBeenCalledWith(carteBancaire);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carteBancaire = new CarteBancaire();
        spyOn(carteBancaireService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carteBancaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: carteBancaire }));
        saveSubject.complete();

        // THEN
        expect(carteBancaireService.create).toHaveBeenCalledWith(carteBancaire);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carteBancaire = { id: 123 };
        spyOn(carteBancaireService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carteBancaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(carteBancaireService.update).toHaveBeenCalledWith(carteBancaire);
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
    });
  });
});
