import { useMemo, useState } from 'react'
import type { LocationStat } from '../types'

interface Props {
  stats: LocationStat[]
}

type SortKey = 'location' | 'latestTotalCases' | 'diffFromPrevPeriod'

export function StatsTable({ stats }: Props) {
  const [sortKey, setSortKey] = useState<SortKey>('latestTotalCases')
  const [sortDesc, setSortDesc] = useState(true)
  const [filter, setFilter] = useState('')

  const filtered = useMemo(() => {
    const query = filter.trim().toLowerCase()
    if (!query) return stats
    return stats.filter(
      (stat) =>
        stat.country.toLowerCase().includes(query) || (stat.state ?? '').toLowerCase().includes(query),
    )
  }, [stats, filter])

  const sorted = useMemo(() => {
    const copy = [...filtered]
    copy.sort((a, b) => {
      let result = 0
      if (sortKey === 'location') {
        result = `${a.state ?? ''}${a.country}`.localeCompare(`${b.state ?? ''}${b.country}`)
      } else {
        const aVal = a[sortKey] ?? 0
        const bVal = b[sortKey] ?? 0
        result = aVal - bVal
      }
      return sortDesc ? -result : result
    })
    return copy
  }, [filtered, sortKey, sortDesc])

  function toggleSort(key: SortKey) {
    if (key === sortKey) {
      setSortDesc((prev) => !prev)
    } else {
      setSortKey(key)
      setSortDesc(true)
    }
  }

  return (
    <div className="rounded-xl bg-white shadow-sm ring-1 ring-slate-200 dark:bg-slate-900 dark:ring-slate-800">
      <div className="border-b border-slate-200 p-4 dark:border-slate-800">
        <input
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
          placeholder="Filter by country or state..."
          className="w-full max-w-xs rounded-lg border border-slate-200 bg-slate-50 px-3 py-1.5 text-sm outline-none focus:border-indigo-400 dark:border-slate-700 dark:bg-slate-800"
        />
      </div>
      <div className="max-h-[28rem] overflow-auto">
        <table className="w-full text-left text-sm">
          <thead className="sticky top-0 bg-slate-50 text-slate-500 dark:bg-slate-800 dark:text-slate-400">
            <tr>
              <Th label="Location" active={sortKey === 'location'} desc={sortDesc} onClick={() => toggleSort('location')} />
              <Th
                label="Total cases"
                active={sortKey === 'latestTotalCases'}
                desc={sortDesc}
                onClick={() => toggleSort('latestTotalCases')}
                align="right"
              />
              <Th
                label="Change"
                active={sortKey === 'diffFromPrevPeriod'}
                desc={sortDesc}
                onClick={() => toggleSort('diffFromPrevPeriod')}
                align="right"
              />
            </tr>
          </thead>
          <tbody>
            {sorted.map((stat, i) => (
              <tr
                key={`${stat.country}-${stat.state ?? ''}`}
                className={`border-t border-slate-100 dark:border-slate-800 ${i % 2 === 1 ? 'bg-slate-50/50 dark:bg-slate-800/30' : ''}`}
              >
                <td className="px-4 py-2">
                  {stat.state ? `${stat.state}, ${stat.country}` : stat.country}
                </td>
                <td className="px-4 py-2 text-right tabular-nums">
                  {new Intl.NumberFormat('en-US').format(stat.latestTotalCases)}
                </td>
                <td
                  className={`px-4 py-2 text-right tabular-nums ${
                    (stat.diffFromPrevPeriod ?? 0) > 0
                      ? 'text-rose-500'
                      : (stat.diffFromPrevPeriod ?? 0) < 0
                        ? 'text-emerald-500'
                        : 'text-slate-400'
                  }`}
                >
                  {stat.diffFromPrevPeriod === null
                    ? '—'
                    : `${stat.diffFromPrevPeriod >= 0 ? '+' : ''}${new Intl.NumberFormat('en-US').format(stat.diffFromPrevPeriod)}`}
                </td>
              </tr>
            ))}
            {sorted.length === 0 && (
              <tr>
                <td colSpan={3} className="px-4 py-6 text-center text-slate-400">
                  No matching locations.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

interface ThProps {
  label: string
  active: boolean
  desc: boolean
  onClick: () => void
  align?: 'left' | 'right'
}

function Th({ label, active, desc, onClick, align = 'left' }: ThProps) {
  return (
    <th
      onClick={onClick}
      className={`cursor-pointer select-none px-4 py-2 font-medium ${align === 'right' ? 'text-right' : 'text-left'}`}
    >
      {label}
      {active && <span className="ml-1">{desc ? '↓' : '↑'}</span>}
    </th>
  )
}
