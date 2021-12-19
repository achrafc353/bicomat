import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConseiller } from '../conseiller.model';
import { ConseillerService } from '../service/conseiller.service';

@Component({
  templateUrl: './conseiller-delete-dialog.component.html',
})
export class ConseillerDeleteDialogComponent {
  conseiller?: IConseiller;

  constructor(protected conseillerService: ConseillerService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conseillerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
