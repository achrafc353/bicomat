import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConseiller, Conseiller } from '../conseiller.model';
import { ConseillerService } from '../service/conseiller.service';

@Injectable({ providedIn: 'root' })
export class ConseillerRoutingResolveService implements Resolve<IConseiller> {
  constructor(protected service: ConseillerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConseiller> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((conseiller: HttpResponse<Conseiller>) => {
          if (conseiller.body) {
            return of(conseiller.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Conseiller());
  }
}
