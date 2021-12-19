import { IClient } from 'app/entities/client/client.model';
import { IBanque } from 'app/entities/banque/banque.model';
import { TypeCompte } from 'app/entities/enumerations/type-compte.model';

export interface ICompte {
  id?: number;
  numero?: string | null;
  type?: TypeCompte | null;
  solde?: number | null;
  decouvert?: number | null;
  tauxRenumeration?: number | null;
  client?: IClient | null;
  banque?: IBanque | null;
}

export class Compte implements ICompte {
  constructor(
    public id?: number,
    public numero?: string | null,
    public type?: TypeCompte | null,
    public solde?: number | null,
    public decouvert?: number | null,
    public tauxRenumeration?: number | null,
    public client?: IClient | null,
    public banque?: IBanque | null
  ) {}
}

export function getCompteIdentifier(compte: ICompte): number | undefined {
  return compte.id;
}
