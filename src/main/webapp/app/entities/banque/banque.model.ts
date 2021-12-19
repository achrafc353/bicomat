import { ICompte } from 'app/entities/compte/compte.model';
import { IClient } from 'app/entities/client/client.model';

export interface IBanque {
  id?: number;
  nom?: string | null;
  adresse?: string | null;
  comptes?: ICompte[] | null;
  clients?: IClient[] | null;
}

export class Banque implements IBanque {
  constructor(
    public id?: number,
    public nom?: string | null,
    public adresse?: string | null,
    public comptes?: ICompte[] | null,
    public clients?: IClient[] | null
  ) {}
}

export function getBanqueIdentifier(banque: IBanque): number | undefined {
  return banque.id;
}
