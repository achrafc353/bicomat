import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOperation, Operation } from '../operation.model';
import { OperationService } from '../service/operation.service';
import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-operation-update',
  templateUrl: './operation-update.component.html',
})
export class OperationUpdateComponent implements OnInit {
  isSaving = false;

  operationLieesCollection: IOperation[] = [];
  comptesSharedCollection: ICompte[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    date: [],
    montant: [],
    signe: [],
    type: [],
    echeance: [],
    operationLiee: [],
    compte: [],
    client: [],
  });

  constructor(
    protected operationService: OperationService,
    protected compteService: CompteService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operation }) => {
      if (operation.id === undefined) {
        const today = dayjs().startOf('day');
        operation.date = today;
        operation.echeance = today;
      }

      this.updateForm(operation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operation = this.createFromForm();
    if (operation.id !== undefined) {
      this.subscribeToSaveResponse(this.operationService.update(operation));
    } else {
      this.subscribeToSaveResponse(this.operationService.create(operation));
    }
  }

  trackOperationById(index: number, item: IOperation): number {
    return item.id!;
  }

  trackCompteById(index: number, item: ICompte): number {
    return item.id!;
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperation>>): void {
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

  protected updateForm(operation: IOperation): void {
    this.editForm.patchValue({
      id: operation.id,
      numero: operation.numero,
      date: operation.date ? operation.date.format(DATE_TIME_FORMAT) : null,
      montant: operation.montant,
      signe: operation.signe,
      type: operation.type,
      echeance: operation.echeance ? operation.echeance.format(DATE_TIME_FORMAT) : null,
      operationLiee: operation.operationLiee,
      compte: operation.compte,
      client: operation.client,
    });

    this.operationLieesCollection = this.operationService.addOperationToCollectionIfMissing(
      this.operationLieesCollection,
      operation.operationLiee
    );
    this.comptesSharedCollection = this.compteService.addCompteToCollectionIfMissing(this.comptesSharedCollection, operation.compte);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, operation.client);
  }

  protected loadRelationshipsOptions(): void {
    this.operationService
      .query({ filter: 'operation-is-null' })
      .pipe(map((res: HttpResponse<IOperation[]>) => res.body ?? []))
      .pipe(
        map((operations: IOperation[]) =>
          this.operationService.addOperationToCollectionIfMissing(operations, this.editForm.get('operationLiee')!.value)
        )
      )
      .subscribe((operations: IOperation[]) => (this.operationLieesCollection = operations));

    this.compteService
      .query()
      .pipe(map((res: HttpResponse<ICompte[]>) => res.body ?? []))
      .pipe(map((comptes: ICompte[]) => this.compteService.addCompteToCollectionIfMissing(comptes, this.editForm.get('compte')!.value)))
      .subscribe((comptes: ICompte[]) => (this.comptesSharedCollection = comptes));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): IOperation {
    return {
      ...new Operation(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      montant: this.editForm.get(['montant'])!.value,
      signe: this.editForm.get(['signe'])!.value,
      type: this.editForm.get(['type'])!.value,
      echeance: this.editForm.get(['echeance'])!.value ? dayjs(this.editForm.get(['echeance'])!.value, DATE_TIME_FORMAT) : undefined,
      operationLiee: this.editForm.get(['operationLiee'])!.value,
      compte: this.editForm.get(['compte'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
