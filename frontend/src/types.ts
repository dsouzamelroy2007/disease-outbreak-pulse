export interface DiseaseInfo {
  code: string
  displayName: string
  dataFreshnessLabel: string
  sourceLabel: string
}

export interface DiseaseSummary {
  code: string
  displayName: string
  totalCases: number
  totalNew: number
  dataFreshnessLabel: string
  sourceLabel: string
  locationsReporting: number
}

export interface LocationStat {
  disease: string
  state: string | null
  country: string
  latestTotalCases: number
  diffFromPrevPeriod: number | null
  asOfDate: string | null
}
