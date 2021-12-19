import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';
import { TypeCarte } from 'app/entities/enumerations/type-carte.model';

export interface ICarteBancaire {
  id?: number;
  numero?: string | null;
  type?: TypeCarte | null;
  echeance?: dayjs.Dayjs | null;
  codeCrypto?: string | null;
  client?: IClient | null;
}

export class CarteBancaire implements ICarteBancaire {
  constructor(
    public id?: number,
    public numero?: string | null,
    public type?: TypeCarte | null,
    public echeance?: dayjs.Dayjs | null,
    public codeCrypto?: string | null,
    public client?: IClient | null
  ) {}
}

export function getCarteBancaireIdentifier(carteBancaire: ICarteBancaire): number | undefined {
  return carteBancaire.id;
}
