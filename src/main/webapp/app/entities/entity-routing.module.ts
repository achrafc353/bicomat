import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'banque',
        data: { pageTitle: 'bicomatApp.banque.home.title' },
        loadChildren: () => import('./banque/banque.module').then(m => m.BanqueModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'bicomatApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'tiers',
        data: { pageTitle: 'bicomatApp.tiers.home.title' },
        loadChildren: () => import('./tiers/tiers.module').then(m => m.TiersModule),
      },
      {
        path: 'carte-bancaire',
        data: { pageTitle: 'bicomatApp.carteBancaire.home.title' },
        loadChildren: () => import('./carte-bancaire/carte-bancaire.module').then(m => m.CarteBancaireModule),
      },
      {
        path: 'compte',
        data: { pageTitle: 'bicomatApp.compte.home.title' },
        loadChildren: () => import('./compte/compte.module').then(m => m.CompteModule),
      },
      {
        path: 'operation',
        data: { pageTitle: 'bicomatApp.operation.home.title' },
        loadChildren: () => import('./operation/operation.module').then(m => m.OperationModule),
      },
      {
        path: 'conseiller',
        data: { pageTitle: 'bicomatApp.conseiller.home.title' },
        loadChildren: () => import('./conseiller/conseiller.module').then(m => m.ConseillerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
