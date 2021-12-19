import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';
import { CarteBancaireDeleteDialogComponent } from '../delete/carte-bancaire-delete-dialog.component';

@Component({
  selector: 'jhi-carte-bancaire',
  templateUrl: './carte-bancaire.component.html',
})
export class CarteBancaireComponent implements OnInit {
  carteBancaires?: ICarteBancaire[];
  isLoading = false;

  constructor(protected carteBancaireService: CarteBancaireService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.carteBancaireService.query().subscribe(
      (res: HttpResponse<ICarteBancaire[]>) => {
        this.isLoading = false;
        this.carteBancaires = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICarteBancaire): number {
    return item.id!;
  }

  delete(carteBancaire: ICarteBancaire): void {
    const modalRef = this.modalService.open(CarteBancaireDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.carteBancaire = carteBancaire;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
