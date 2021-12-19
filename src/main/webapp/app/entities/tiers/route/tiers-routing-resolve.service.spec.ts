jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITiers, Tiers } from '../tiers.model';
import { TiersService } from '../service/tiers.service';

import { TiersRoutingResolveService } from './tiers-routing-resolve.service';

describe('Service Tests', () => {
  describe('Tiers routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TiersRoutingResolveService;
    let service: TiersService;
    let resultTiers: ITiers | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TiersRoutingResolveService);
      service = TestBed.inject(TiersService);
      resultTiers = undefined;
    });

    describe('resolve', () => {
      it('should return ITiers returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTiers = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTiers).toEqual({ id: 123 });
      });

      it('should return new ITiers if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTiers = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTiers).toEqual(new Tiers());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTiers = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTiers).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
