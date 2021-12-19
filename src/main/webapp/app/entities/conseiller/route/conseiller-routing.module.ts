import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConseillerComponent } from '../list/conseiller.component';
import { ConseillerDetailComponent } from '../detail/conseiller-detail.component';
import { ConseillerUpdateComponent } from '../update/conseiller-update.component';
import { ConseillerRoutingResolveService } from './conseiller-routing-resolve.service';

const conseillerRoute: Routes = [
  {
    path: '',
    component: ConseillerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConseillerDetailComponent,
    resolve: {
      conseiller: ConseillerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConseillerUpdateComponent,
    resolve: {
      conseiller: ConseillerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConseillerUpdateComponent,
    resolve: {
      conseiller: ConseillerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conseillerRoute)],
  exports: [RouterModule],
})
export class ConseillerRoutingModule {}
