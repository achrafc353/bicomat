import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-carte-bancaire-update',
  templateUrl: './carte-bancaire-update.component.html',
})
export class CarteBancaireUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    type: [],
    echeance: [],
    codeCrypto: [],
    client: [],
  });

  constructor(
    protected carteBancaireService: CarteBancaireService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carteBancaire }) => {
      if (carteBancaire.id === undefined) {
        const today = dayjs().startOf('day');
        carteBancaire.echeance = today;
      }

      this.updateForm(carteBancaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carteBancaire = this.createFromForm();
    if (carteBancaire.id !== undefined) {
      this.subscribeToSaveResponse(this.carteBancaireService.update(carteBancaire));
    } else {
      this.subscribeToSaveResponse(this.carteBancaireService.create(carteBancaire));
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarteBancaire>>): void {
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

  protected updateForm(carteBancaire: ICarteBancaire): void {
    this.editForm.patchValue({
      id: carteBancaire.id,
      numero: carteBancaire.numero,
      type: carteBancaire.type,
      echeance: carteBancaire.echeance ? carteBancaire.echeance.format(DATE_TIME_FORMAT) : null,
      codeCrypto: carteBancaire.codeCrypto,
      client: carteBancaire.client,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, carteBancaire.client);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): ICarteBancaire {
    return {
      ...new CarteBancaire(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      type: this.editForm.get(['type'])!.value,
      echeance: this.editForm.get(['echeance'])!.value ? dayjs(this.editForm.get(['echeance'])!.value, DATE_TIME_FORMAT) : undefined,
      codeCrypto: this.editForm.get(['codeCrypto'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
