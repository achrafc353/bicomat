import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompte, Compte } from '../compte.model';
import { CompteService } from '../service/compte.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

@Component({
  selector: 'jhi-compte-update',
  templateUrl: './compte-update.component.html',
})
export class CompteUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];
  banquesSharedCollection: IBanque[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    type: [],
    solde: [],
    decouvert: [],
    tauxRenumeration: [],
    client: [],
    banque: [],
  });

  constructor(
    protected compteService: CompteService,
    protected clientService: ClientService,
    protected banqueService: BanqueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compte }) => {
      this.updateForm(compte);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compte = this.createFromForm();
    if (compte.id !== undefined) {
      this.subscribeToSaveResponse(this.compteService.update(compte));
    } else {
      this.subscribeToSaveResponse(this.compteService.create(compte));
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  trackBanqueById(index: number, item: IBanque): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompte>>): void {
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

  protected updateForm(compte: ICompte): void {
    this.editForm.patchValue({
      id: compte.id,
      numero: compte.numero,
      type: compte.type,
      solde: compte.solde,
      decouvert: compte.decouvert,
      tauxRenumeration: compte.tauxRenumeration,
      client: compte.client,
      banque: compte.banque,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, compte.client);
    this.banquesSharedCollection = this.banqueService.addBanqueToCollectionIfMissing(this.banquesSharedCollection, compte.banque);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.banqueService
      .query()
      .pipe(map((res: HttpResponse<IBanque[]>) => res.body ?? []))
      .pipe(map((banques: IBanque[]) => this.banqueService.addBanqueToCollectionIfMissing(banques, this.editForm.get('banque')!.value)))
      .subscribe((banques: IBanque[]) => (this.banquesSharedCollection = banques));
  }

  protected createFromForm(): ICompte {
    return {
      ...new Compte(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      type: this.editForm.get(['type'])!.value,
      solde: this.editForm.get(['solde'])!.value,
      decouvert: this.editForm.get(['decouvert'])!.value,
      tauxRenumeration: this.editForm.get(['tauxRenumeration'])!.value,
      client: this.editForm.get(['client'])!.value,
      banque: this.editForm.get(['banque'])!.value,
    };
  }
}
