jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TiersService } from '../service/tiers.service';

import { TiersDeleteDialogComponent } from './tiers-delete-dialog.component';

describe('Component Tests', () => {
  describe('Tiers Management Delete Component', () => {
    let comp: TiersDeleteDialogComponent;
    let fixture: ComponentFixture<TiersDeleteDialogComponent>;
    let service: TiersService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TiersDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(TiersDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TiersDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TiersService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
