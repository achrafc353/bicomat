import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TiersService } from '../service/tiers.service';

import { TiersComponent } from './tiers.component';

describe('Component Tests', () => {
  describe('Tiers Management Component', () => {
    let comp: TiersComponent;
    let fixture: ComponentFixture<TiersComponent>;
    let service: TiersService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TiersComponent],
      })
        .overrideTemplate(TiersComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TiersComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TiersService);

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
      expect(comp.tiers?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
