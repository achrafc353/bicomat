import { IClient } from 'app/entities/client/client.model';

export interface IConseiller {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  clients?: IClient[] | null;
}

export class Conseiller implements IConseiller {
  constructor(public id?: number, public nom?: string | null, public prenom?: string | null, public clients?: IClient[] | null) {}
}

export function getConseillerIdentifier(conseiller: IConseiller): number | undefined {
  return conseiller.id;
}
