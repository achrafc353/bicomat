import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TiersComponent } from './list/tiers.component';
import { TiersDetailComponent } from './detail/tiers-detail.component';
import { TiersUpdateComponent } from './update/tiers-update.component';
import { TiersDeleteDialogComponent } from './delete/tiers-delete-dialog.component';
import { TiersRoutingModule } from './route/tiers-routing.module';

@NgModule({
  imports: [SharedModule, TiersRoutingModule],
  declarations: [TiersComponent, TiersDetailComponent, TiersUpdateComponent, TiersDeleteDialogComponent],
  entryComponents: [TiersDeleteDialogComponent],
})
export class TiersModule {}
