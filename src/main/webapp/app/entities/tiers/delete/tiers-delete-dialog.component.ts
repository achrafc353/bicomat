import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITiers } from '../tiers.model';
import { TiersService } from '../service/tiers.service';

@Component({
  templateUrl: './tiers-delete-dialog.component.html',
})
export class TiersDeleteDialogComponent {
  tiers?: ITiers;

  constructor(protected tiersService: TiersService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tiersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
