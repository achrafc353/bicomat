import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CarteBancaireService } from '../service/carte-bancaire.service';

import { CarteBancaireComponent } from './carte-bancaire.component';

describe('Component Tests', () => {
  describe('CarteBancaire Management Component', () => {
    let comp: CarteBancaireComponent;
    let fixture: ComponentFixture<CarteBancaireComponent>;
    let service: CarteBancaireService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CarteBancaireComponent],
      })
        .overrideTemplate(CarteBancaireComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CarteBancaireComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CarteBancaireService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.carteBancaires?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
