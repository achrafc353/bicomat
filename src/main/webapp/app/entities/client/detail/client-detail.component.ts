import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';

import { IClient } from '../client.model';

@Component({
  selector: 'jhi-client-detail',
  templateUrl: './client-detail.component.html',
})
export class ClientDetailComponent implements OnInit {
  client: IClient | null = null;
  comptes?: ICompte[];

  constructor(protected activatedRoute: ActivatedRoute, protected compteService: CompteService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.client = client;

      this.compteService.queryWithClientId(client?.id, {}).subscribe(
        (res: HttpResponse<ICompte[]>) => {
          this.onSuccess(res.body);
        },
        () => {
          this.onerror();
        }
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  protected onSuccess(data: ICompte[] | null): void {
    this.comptes = data ?? [];
  }

  protected onerror(): void {
    this.comptes = <ICompte[]>[];
  }
}
