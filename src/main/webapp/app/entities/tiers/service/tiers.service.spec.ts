import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITiers, Tiers } from '../tiers.model';

import { TiersService } from './tiers.service';

describe('Service Tests', () => {
  describe('Tiers Service', () => {
    let service: TiersService;
    let httpMock: HttpTestingController;
    let elemDefault: ITiers;
    let expectedResult: ITiers | ITiers[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TiersService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        etatTiers: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Tiers', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Tiers()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tiers', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            etatTiers: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Tiers', () => {
        const patchObject = Object.assign({}, new Tiers());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tiers', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            etatTiers: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Tiers', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTiersToCollectionIfMissing', () => {
        it('should add a Tiers to an empty array', () => {
          const tiers: ITiers = { id: 123 };
          expectedResult = service.addTiersToCollectionIfMissing([], tiers);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tiers);
        });

        it('should not add a Tiers to an array that contains it', () => {
          const tiers: ITiers = { id: 123 };
          const tiersCollection: ITiers[] = [
            {
              ...tiers,
            },
            { id: 456 },
          ];
          expectedResult = service.addTiersToCollectionIfMissing(tiersCollection, tiers);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Tiers to an array that doesn't contain it", () => {
          const tiers: ITiers = { id: 123 };
          const tiersCollection: ITiers[] = [{ id: 456 }];
          expectedResult = service.addTiersToCollectionIfMissing(tiersCollection, tiers);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tiers);
        });

        it('should add only unique Tiers to an array', () => {
          const tiersArray: ITiers[] = [{ id: 123 }, { id: 456 }, { id: 90230 }];
          const tiersCollection: ITiers[] = [{ id: 123 }];
          expectedResult = service.addTiersToCollectionIfMissing(tiersCollection, ...tiersArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tiers: ITiers = { id: 123 };
          const tiers2: ITiers = { id: 456 };
          expectedResult = service.addTiersToCollectionIfMissing([], tiers, tiers2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tiers);
          expect(expectedResult).toContain(tiers2);
        });

        it('should accept null and undefined values', () => {
          const tiers: ITiers = { id: 123 };
          expectedResult = service.addTiersToCollectionIfMissing([], null, tiers, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tiers);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
