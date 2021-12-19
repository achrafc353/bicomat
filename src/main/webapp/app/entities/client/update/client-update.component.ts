import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IClient, Client } from '../client.model';
import { ClientService } from '../service/client.service';
import { ITiers } from 'app/entities/tiers/tiers.model';
import { TiersService } from 'app/entities/tiers/service/tiers.service';
import { IConseiller } from 'app/entities/conseiller/conseiller.model';
import { ConseillerService } from 'app/entities/conseiller/service/conseiller.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;

  tiersCollection: ITiers[] = [];
  conseillersSharedCollection: IConseiller[] = [];
  banquesSharedCollection: IBanque[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    admel: [],
    type: [],
    login: [],
    motDePasse: [],
    anneeArrivee: [],
    numContrat: [],
    agency: [],
    numPortable: [],
    tiers: [],
    conseiller: [],
    banque: [],
  });

  constructor(
    protected clientService: ClientService,
    protected tiersService: TiersService,
    protected conseillerService: ConseillerService,
    protected banqueService: BanqueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      if (client.id === undefined) {
        const today = dayjs().startOf('day');
        client.anneeArrivee = today;
      }

      this.updateForm(client);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  trackTiersById(index: number, item: ITiers): number {
    return item.id!;
  }

  trackConseillerById(index: number, item: IConseiller): number {
    return item.id!;
  }

  trackBanqueById(index: number, item: IBanque): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(client: IClient): void {
    this.editForm.patchValue({
      id: client.id,
      nom: client.nom,
      prenom: client.prenom,
      admel: client.admel,
      type: client.type,
      login: client.login,
      motDePasse: client.motDePasse,
      anneeArrivee: client.anneeArrivee ? client.anneeArrivee.format(DATE_TIME_FORMAT) : null,
      numContrat: client.numContrat,
      agency: client.agency,
      numPortable: client.numPortable,
      tiers: client.tiers,
      conseiller: client.conseiller,
      banque: client.banque,
    });

    this.tiersCollection = this.tiersService.addTiersToCollectionIfMissing(this.tiersCollection, client.tiers);
    this.conseillersSharedCollection = this.conseillerService.addConseillerToCollectionIfMissing(
      this.conseillersSharedCollection,
      client.conseiller
    );
    this.banquesSharedCollection = this.banqueService.addBanqueToCollectionIfMissing(this.banquesSharedCollection, client.banque);
  }

  protected loadRelationshipsOptions(): void {
    this.tiersService
      .query({ filter: 'client-is-null' })
      .pipe(map((res: HttpResponse<ITiers[]>) => res.body ?? []))
      .pipe(map((tiers: ITiers[]) => this.tiersService.addTiersToCollectionIfMissing(tiers, this.editForm.get('tiers')!.value)))
      .subscribe((tiers: ITiers[]) => (this.tiersCollection = tiers));

    this.conseillerService
      .query()
      .pipe(map((res: HttpResponse<IConseiller[]>) => res.body ?? []))
      .pipe(
        map((conseillers: IConseiller[]) =>
          this.conseillerService.addConseillerToCollectionIfMissing(conseillers, this.editForm.get('conseiller')!.value)
        )
      )
      .subscribe((conseillers: IConseiller[]) => (this.conseillersSharedCollection = conseillers));

    this.banqueService
      .query()
      .pipe(map((res: HttpResponse<IBanque[]>) => res.body ?? []))
      .pipe(map((banques: IBanque[]) => this.banqueService.addBanqueToCollectionIfMissing(banques, this.editForm.get('banque')!.value)))
      .subscribe((banques: IBanque[]) => (this.banquesSharedCollection = banques));
  }

  protected createFromForm(): IClient {
    return {
      ...new Client(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      admel: this.editForm.get(['admel'])!.value,
      type: this.editForm.get(['type'])!.value,
      login: this.editForm.get(['login'])!.value,
      motDePasse: this.editForm.get(['motDePasse'])!.value,
      anneeArrivee: this.editForm.get(['anneeArrivee'])!.value
        ? dayjs(this.editForm.get(['anneeArrivee'])!.value, DATE_TIME_FORMAT)
        : undefined,
      numContrat: this.editForm.get(['numContrat'])!.value,
      agency: this.editForm.get(['agency'])!.value,
      numPortable: this.editForm.get(['numPortable'])!.value,
      tiers: this.editForm.get(['tiers'])!.value,
      conseiller: this.editForm.get(['conseiller'])!.value,
      banque: this.editForm.get(['banque'])!.value,
    };
  }
}
