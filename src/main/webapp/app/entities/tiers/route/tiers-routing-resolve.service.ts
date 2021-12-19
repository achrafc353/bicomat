import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITiers, Tiers } from '../tiers.model';
import { TiersService } from '../service/tiers.service';

@Injectable({ providedIn: 'root' })
export class TiersRoutingResolveService implements Resolve<ITiers> {
  constructor(protected service: TiersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITiers> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tiers: HttpResponse<Tiers>) => {
          if (tiers.body) {
            return of(tiers.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tiers());
  }
}
