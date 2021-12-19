import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConseiller } from '../conseiller.model';
import { ConseillerService } from '../service/conseiller.service';
import { ConseillerDeleteDialogComponent } from '../delete/conseiller-delete-dialog.component';

@Component({
  selector: 'jhi-conseiller',
  templateUrl: './conseiller.component.html',
})
export class ConseillerComponent implements OnInit {
  conseillers?: IConseiller[];
  isLoading = false;

  constructor(protected conseillerService: ConseillerService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.conseillerService.query().subscribe(
      (res: HttpResponse<IConseiller[]>) => {
        this.isLoading = false;
        this.conseillers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IConseiller): number {
    return item.id!;
  }

  delete(conseiller: IConseiller): void {
    const modalRef = this.modalService.open(ConseillerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.conseiller = conseiller;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
