import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ConseillerComponent } from './list/conseiller.component';
import { ConseillerDetailComponent } from './detail/conseiller-detail.component';
import { ConseillerUpdateComponent } from './update/conseiller-update.component';
import { ConseillerDeleteDialogComponent } from './delete/conseiller-delete-dialog.component';
import { ConseillerRoutingModule } from './route/conseiller-routing.module';

@NgModule({
  imports: [SharedModule, ConseillerRoutingModule],
  declarations: [ConseillerComponent, ConseillerDetailComponent, ConseillerUpdateComponent, ConseillerDeleteDialogComponent],
  entryComponents: [ConseillerDeleteDialogComponent],
})
export class ConseillerModule {}
