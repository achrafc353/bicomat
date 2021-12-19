import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConseiller, getConseillerIdentifier } from '../conseiller.model';

export type EntityResponseType = HttpResponse<IConseiller>;
export type EntityArrayResponseType = HttpResponse<IConseiller[]>;

@Injectable({ providedIn: 'root' })
export class ConseillerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/conseillers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(conseiller: IConseiller): Observable<EntityResponseType> {
    return this.http.post<IConseiller>(this.resourceUrl, conseiller, { observe: 'response' });
  }

  update(conseiller: IConseiller): Observable<EntityResponseType> {
    return this.http.put<IConseiller>(`${this.resourceUrl}/${getConseillerIdentifier(conseiller) as number}`, conseiller, {
      observe: 'response',
    });
  }

  partialUpdate(conseiller: IConseiller): Observable<EntityResponseType> {
    return this.http.patch<IConseiller>(`${this.resourceUrl}/${getConseillerIdentifier(conseiller) as number}`, conseiller, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConseiller>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConseiller[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConseillerToCollectionIfMissing(
    conseillerCollection: IConseiller[],
    ...conseillersToCheck: (IConseiller | null | undefined)[]
  ): IConseiller[] {
    const conseillers: IConseiller[] = conseillersToCheck.filter(isPresent);
    if (conseillers.length > 0) {
      const conseillerCollectionIdentifiers = conseillerCollection.map(conseillerItem => getConseillerIdentifier(conseillerItem)!);
      const conseillersToAdd = conseillers.filter(conseillerItem => {
        const conseillerIdentifier = getConseillerIdentifier(conseillerItem);
        if (conseillerIdentifier == null || conseillerCollectionIdentifiers.includes(conseillerIdentifier)) {
          return false;
        }
        conseillerCollectionIdentifiers.push(conseillerIdentifier);
        return true;
      });
      return [...conseillersToAdd, ...conseillerCollection];
    }
    return conseillerCollection;
  }
}
