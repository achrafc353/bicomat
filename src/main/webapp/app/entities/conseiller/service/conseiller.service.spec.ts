import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConseiller, Conseiller } from '../conseiller.model';

import { ConseillerService } from './conseiller.service';

describe('Service Tests', () => {
  describe('Conseiller Service', () => {
    let service: ConseillerService;
    let httpMock: HttpTestingController;
    let elemDefault: IConseiller;
    let expectedResult: IConseiller | IConseiller[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConseillerService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        prenom: 'AAAAAAA',
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

      it('should create a Conseiller', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Conseiller()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Conseiller', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Conseiller', () => {
        const patchObject = Object.assign(
          {
            prenom: 'BBBBBB',
          },
          new Conseiller()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Conseiller', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
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

      it('should delete a Conseiller', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConseillerToCollectionIfMissing', () => {
        it('should add a Conseiller to an empty array', () => {
          const conseiller: IConseiller = { id: 123 };
          expectedResult = service.addConseillerToCollectionIfMissing([], conseiller);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(conseiller);
        });

        it('should not add a Conseiller to an array that contains it', () => {
          const conseiller: IConseiller = { id: 123 };
          const conseillerCollection: IConseiller[] = [
            {
              ...conseiller,
            },
            { id: 456 },
          ];
          expectedResult = service.addConseillerToCollectionIfMissing(conseillerCollection, conseiller);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Conseiller to an array that doesn't contain it", () => {
          const conseiller: IConseiller = { id: 123 };
          const conseillerCollection: IConseiller[] = [{ id: 456 }];
          expectedResult = service.addConseillerToCollectionIfMissing(conseillerCollection, conseiller);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(conseiller);
        });

        it('should add only unique Conseiller to an array', () => {
          const conseillerArray: IConseiller[] = [{ id: 123 }, { id: 456 }, { id: 2180 }];
          const conseillerCollection: IConseiller[] = [{ id: 123 }];
          expectedResult = service.addConseillerToCollectionIfMissing(conseillerCollection, ...conseillerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const conseiller: IConseiller = { id: 123 };
          const conseiller2: IConseiller = { id: 456 };
          expectedResult = service.addConseillerToCollectionIfMissing([], conseiller, conseiller2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(conseiller);
          expect(expectedResult).toContain(conseiller2);
        });

        it('should accept null and undefined values', () => {
          const conseiller: IConseiller = { id: 123 };
          expectedResult = service.addConseillerToCollectionIfMissing([], null, conseiller, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(conseiller);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
