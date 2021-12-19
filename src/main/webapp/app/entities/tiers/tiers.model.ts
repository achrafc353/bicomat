export interface ITiers {
  id?: number;
  etatTiers?: string | null;
}

export class Tiers implements ITiers {
  constructor(public id?: number, public etatTiers?: string | null) {}
}

export function getTiersIdentifier(tiers: ITiers): number | undefined {
  return tiers.id;
}
