import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TiersComponent } from '../list/tiers.component';
import { TiersDetailComponent } from '../detail/tiers-detail.component';
import { TiersUpdateComponent } from '../update/tiers-update.component';
import { TiersRoutingResolveService } from './tiers-routing-resolve.service';

const tiersRoute: Routes = [
  {
    path: '',
    component: TiersComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TiersDetailComponent,
    resolve: {
      tiers: TiersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TiersUpdateComponent,
    resolve: {
      tiers: TiersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TiersUpdateComponent,
    resolve: {
      tiers: TiersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tiersRoute)],
  exports: [RouterModule],
})
export class TiersRoutingModule {}
