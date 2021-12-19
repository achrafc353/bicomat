jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';

import { CarteBancaireRoutingResolveService } from './carte-bancaire-routing-resolve.service';

describe('Service Tests', () => {
  describe('CarteBancaire routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CarteBancaireRoutingResolveService;
    let service: CarteBancaireService;
    let resultCarteBancaire: ICarteBancaire | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CarteBancaireRoutingResolveService);
      service = TestBed.inject(CarteBancaireService);
      resultCarteBancaire = undefined;
    });

    describe('resolve', () => {
      it('should return ICarteBancaire returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCarteBancaire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCarteBancaire).toEqual({ id: 123 });
      });

      it('should return new ICarteBancaire if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCarteBancaire = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCarteBancaire).toEqual(new CarteBancaire());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCarteBancaire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCarteBancaire).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
