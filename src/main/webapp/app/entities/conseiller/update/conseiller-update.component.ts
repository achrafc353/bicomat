import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IConseiller, Conseiller } from '../conseiller.model';
import { ConseillerService } from '../service/conseiller.service';

@Component({
  selector: 'jhi-conseiller-update',
  templateUrl: './conseiller-update.component.html',
})
export class ConseillerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
  });

  constructor(protected conseillerService: ConseillerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conseiller }) => {
      this.updateForm(conseiller);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conseiller = this.createFromForm();
    if (conseiller.id !== undefined) {
      this.subscribeToSaveResponse(this.conseillerService.update(conseiller));
    } else {
      this.subscribeToSaveResponse(this.conseillerService.create(conseiller));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConseiller>>): void {
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

  protected updateForm(conseiller: IConseiller): void {
    this.editForm.patchValue({
      id: conseiller.id,
      nom: conseiller.nom,
      prenom: conseiller.prenom,
    });
  }

  protected createFromForm(): IConseiller {
    return {
      ...new Conseiller(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
    };
  }
}
