import type { DiseaseInfo, DiseaseSummary, LocationStat } from '../types'

const BASE = '/tracker/api'

async function getJson<T>(path: string): Promise<T> {
  const response = await fetch(`${BASE}${path}`)
  if (!response.ok) {
    throw new Error(`Request to ${path} failed with status ${response.status}`)
  }
  return response.json() as Promise<T>
}

export const api = {
  listDiseases: () => getJson<DiseaseInfo[]>('/diseases'),
  getSummary: (code: string) => getJson<DiseaseSummary>(`/diseases/${code}/summary`),
  getStats: (code: string) => getJson<LocationStat[]>(`/diseases/${code}/stats`),
}
