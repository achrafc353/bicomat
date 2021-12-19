import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITiers, Tiers } from '../tiers.model';
import { TiersService } from '../service/tiers.service';

@Component({
  selector: 'jhi-tiers-update',
  templateUrl: './tiers-update.component.html',
})
export class TiersUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    etatTiers: [],
  });

  constructor(protected tiersService: TiersService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tiers }) => {
      this.updateForm(tiers);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tiers = this.createFromForm();
    if (tiers.id !== undefined) {
      this.subscribeToSaveResponse(this.tiersService.update(tiers));
    } else {
      this.subscribeToSaveResponse(this.tiersService.create(tiers));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITiers>>): void {
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

  protected updateForm(tiers: ITiers): void {
    this.editForm.patchValue({
      id: tiers.id,
      etatTiers: tiers.etatTiers,
    });
  }

  protected createFromForm(): ITiers {
    return {
      ...new Tiers(),
      id: this.editForm.get(['id'])!.value,
      etatTiers: this.editForm.get(['etatTiers'])!.value,
    };
  }
}
