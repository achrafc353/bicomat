import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITiers, getTiersIdentifier } from '../tiers.model';

export type EntityResponseType = HttpResponse<ITiers>;
export type EntityArrayResponseType = HttpResponse<ITiers[]>;

@Injectable({ providedIn: 'root' })
export class TiersService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tiers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tiers: ITiers): Observable<EntityResponseType> {
    return this.http.post<ITiers>(this.resourceUrl, tiers, { observe: 'response' });
  }

  update(tiers: ITiers): Observable<EntityResponseType> {
    return this.http.put<ITiers>(`${this.resourceUrl}/${getTiersIdentifier(tiers) as number}`, tiers, { observe: 'response' });
  }

  partialUpdate(tiers: ITiers): Observable<EntityResponseType> {
    return this.http.patch<ITiers>(`${this.resourceUrl}/${getTiersIdentifier(tiers) as number}`, tiers, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITiers>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITiers[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTiersToCollectionIfMissing(tiersCollection: ITiers[], ...tiersToCheck: (ITiers | null | undefined)[]): ITiers[] {
    const tiers: ITiers[] = tiersToCheck.filter(isPresent);
    if (tiers.length > 0) {
      const tiersCollectionIdentifiers = tiersCollection.map(tiersItem => getTiersIdentifier(tiersItem)!);
      const tiersToAdd = tiers.filter(tiersItem => {
        const tiersIdentifier = getTiersIdentifier(tiersItem);
        if (tiersIdentifier == null || tiersCollectionIdentifiers.includes(tiersIdentifier)) {
          return false;
        }
        tiersCollectionIdentifiers.push(tiersIdentifier);
        return true;
      });
      return [...tiersToAdd, ...tiersCollection];
    }
    return tiersCollection;
  }
}
