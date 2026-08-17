import { useEffect, useState } from 'react'
import { api } from './api/client'
import { DiseaseSwitcher } from './components/DiseaseSwitcher'
import { SummaryCards } from './components/SummaryCards'
import { StatsTable } from './components/StatsTable'
import { TrendChart } from './components/TrendChart'
import { ThemeToggle } from './components/ThemeToggle'
import type { DiseaseInfo, DiseaseSummary, LocationStat } from './types'

export default function App() {
  const [diseases, setDiseases] = useState<DiseaseInfo[]>([])
  const [active, setActive] = useState<string | null>(null)
  const [summary, setSummary] = useState<DiseaseSummary | null>(null)
  const [stats, setStats] = useState<LocationStat[]>([])
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api
      .listDiseases()
      .then((list) => {
        setDiseases(list)
        setActive(list[0]?.code ?? null)
      })
      .catch(() => setError('Could not reach the tracker API.'))
  }, [])

  useEffect(() => {
    if (!active) return
    setLoading(true)
    setError(null)
    Promise.all([api.getSummary(active), api.getStats(active)])
      .then(([summaryData, statsData]) => {
        setSummary(summaryData)
        setStats(statsData)
      })
      .catch(() => setError(`Could not load data for ${active}.`))
      .finally(() => setLoading(false))
  }, [active])

  return (
    <div className="min-h-screen">
      <header className="border-b border-slate-200 bg-white dark:border-slate-800 dark:bg-slate-900">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4 sm:px-6">
          <div>
            <h1 className="text-xl font-semibold text-slate-900 dark:text-slate-100">Outbreak Pulse</h1>
            <p className="text-sm text-slate-500 dark:text-slate-400">
              Live case tracking across multiple diseases, worldwide
            </p>
          </div>
          <ThemeToggle />
        </div>
      </header>

      <main className="mx-auto max-w-6xl space-y-6 px-4 py-6 sm:px-6">
        <DiseaseSwitcher diseases={diseases} active={active ?? ''} onSelect={setActive} />

        {error && (
          <div className="rounded-lg bg-rose-50 px-4 py-3 text-sm text-rose-700 dark:bg-rose-950/40 dark:text-rose-300">
            {error}
          </div>
        )}

        {loading && !error && (
          <div className="py-16 text-center text-slate-400">Loading…</div>
        )}

        {!loading && !error && summary && (
          <>
            <SummaryCards summary={summary} />
            <TrendChart stats={stats} />
            <StatsTable stats={stats} />
          </>
        )}
      </main>
    </div>
  )
}
