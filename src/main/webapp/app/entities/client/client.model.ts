import * as dayjs from 'dayjs';
import { ITiers } from 'app/entities/tiers/tiers.model';
import { ICarteBancaire } from 'app/entities/carte-bancaire/carte-bancaire.model';
import { ICompte } from 'app/entities/compte/compte.model';
import { IOperation } from 'app/entities/operation/operation.model';
import { IConseiller } from 'app/entities/conseiller/conseiller.model';
import { IBanque } from 'app/entities/banque/banque.model';
import { TypeClient2 } from 'app/entities/enumerations/type-client-2.model';

export interface IClient {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  admel?: string | null;
  type?: TypeClient2 | null;
  login?: string | null;
  motDePasse?: string | null;
  anneeArrivee?: dayjs.Dayjs | null;
  numContrat?: string | null;
  agency?: string | null;
  numPortable?: string | null;
  tiers?: ITiers | null;
  cartes?: ICarteBancaire[] | null;
  comptes?: ICompte[] | null;
  operations?: IOperation[] | null;
  conseiller?: IConseiller | null;
  banque?: IBanque | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public admel?: string | null,
    public type?: TypeClient2 | null,
    public login?: string | null,
    public motDePasse?: string | null,
    public anneeArrivee?: dayjs.Dayjs | null,
    public numContrat?: string | null,
    public agency?: string | null,
    public numPortable?: string | null,
    public tiers?: ITiers | null,
    public cartes?: ICarteBancaire[] | null,
    public comptes?: ICompte[] | null,
    public operations?: IOperation[] | null,
    public conseiller?: IConseiller | null,
    public banque?: IBanque | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
