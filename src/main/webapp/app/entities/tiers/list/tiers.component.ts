import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITiers } from '../tiers.model';
import { TiersService } from '../service/tiers.service';
import { TiersDeleteDialogComponent } from '../delete/tiers-delete-dialog.component';

@Component({
  selector: 'jhi-tiers',
  templateUrl: './tiers.component.html',
})
export class TiersComponent implements OnInit {
  tiers?: ITiers[];
  isLoading = false;

  constructor(protected tiersService: TiersService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tiersService.query().subscribe(
      (res: HttpResponse<ITiers[]>) => {
        this.isLoading = false;
        this.tiers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITiers): number {
    return item.id!;
  }

  delete(tiers: ITiers): void {
    const modalRef = this.modalService.open(TiersDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tiers = tiers;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
