import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITiers } from '../tiers.model';

@Component({
  selector: 'jhi-tiers-detail',
  templateUrl: './tiers-detail.component.html',
})
export class TiersDetailComponent implements OnInit {
  tiers: ITiers | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tiers }) => {
      this.tiers = tiers;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
