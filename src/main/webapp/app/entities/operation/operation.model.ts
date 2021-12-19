import * as dayjs from 'dayjs';
import { ICompte } from 'app/entities/compte/compte.model';
import { IClient } from 'app/entities/client/client.model';
import { TypeOperation } from 'app/entities/enumerations/type-operation.model';

export interface IOperation {
  id?: number;
  numero?: string | null;
  date?: dayjs.Dayjs | null;
  montant?: number | null;
  signe?: number | null;
  type?: TypeOperation | null;
  echeance?: dayjs.Dayjs | null;
  operationLiee?: IOperation | null;
  compte?: ICompte | null;
  client?: IClient | null;
}

export class Operation implements IOperation {
  constructor(
    public id?: number,
    public numero?: string | null,
    public date?: dayjs.Dayjs | null,
    public montant?: number | null,
    public signe?: number | null,
    public type?: TypeOperation | null,
    public echeance?: dayjs.Dayjs | null,
    public operationLiee?: IOperation | null,
    public compte?: ICompte | null,
    public client?: IClient | null
  ) {}
}

export function getOperationIdentifier(operation: IOperation): number | undefined {
  return operation.id;
}
