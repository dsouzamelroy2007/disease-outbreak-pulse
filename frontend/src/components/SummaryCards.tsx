import type { DiseaseSummary } from '../types'

interface Props {
  summary: DiseaseSummary
}

function formatNumber(value: number) {
  return new Intl.NumberFormat('en-US').format(value)
}

export function SummaryCards({ summary }: Props) {
  const isLive = summary.dataFreshnessLabel.toLowerCase() === 'live'

  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
      <Card label="Total cases" value={formatNumber(summary.totalCases)} />
      <Card
        label="Change since last period"
        value={`${summary.totalNew >= 0 ? '+' : ''}${formatNumber(summary.totalNew)}`}
        accent={summary.totalNew > 0 ? 'text-rose-500' : summary.totalNew < 0 ? 'text-emerald-500' : undefined}
      />
      <Card label="Locations reporting" value={formatNumber(summary.locationsReporting)} />
      <Card
        label="Data freshness"
        value={summary.dataFreshnessLabel}
        badge
        badgeClass={
          isLive
            ? 'bg-emerald-100 text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-300'
            : 'bg-amber-100 text-amber-700 dark:bg-amber-900/40 dark:text-amber-300'
        }
        footer={summary.sourceLabel}
      />
    </div>
  )
}

interface CardProps {
  label: string
  value: string
  accent?: string
  badge?: boolean
  badgeClass?: string
  footer?: string
}

function Card({ label, value, accent, badge, badgeClass, footer }: CardProps) {
  return (
    <div className="rounded-xl bg-white p-5 shadow-sm ring-1 ring-slate-200 dark:bg-slate-900 dark:ring-slate-800">
      <p className="text-sm text-slate-500 dark:text-slate-400">{label}</p>
      {badge ? (
        <span className={`mt-2 inline-block rounded-full px-3 py-1 text-sm font-semibold ${badgeClass}`}>
          {value}
        </span>
      ) : (
        <p className={`mt-2 text-2xl font-semibold ${accent ?? 'text-slate-900 dark:text-slate-100'}`}>{value}</p>
      )}
      {footer && <p className="mt-2 text-xs text-slate-400 dark:text-slate-500">{footer}</p>}
    </div>
  )
}
