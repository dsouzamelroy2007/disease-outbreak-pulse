import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'
import type { LocationStat } from '../types'

interface Props {
  stats: LocationStat[]
}

const TOP_N = 10

export function TrendChart({ stats }: Props) {
  const top = [...stats]
    .sort((a, b) => b.latestTotalCases - a.latestTotalCases)
    .slice(0, TOP_N)
    .map((stat) => ({
      name: stat.state ?? stat.country,
      cases: stat.latestTotalCases,
    }))
    .reverse()

  return (
    <div className="rounded-xl bg-white p-5 shadow-sm ring-1 ring-slate-200 dark:bg-slate-900 dark:ring-slate-800">
      <p className="mb-4 text-sm font-medium text-slate-500 dark:text-slate-400">
        Top {Math.min(TOP_N, top.length)} locations by total cases
      </p>
      <ResponsiveContainer width="100%" height={320}>
        <BarChart data={top} layout="vertical" margin={{ left: 24 }}>
          <CartesianGrid strokeDasharray="3 3" className="stroke-slate-200 dark:stroke-slate-800" />
          <XAxis type="number" tick={{ fontSize: 12 }} />
          <YAxis type="category" dataKey="name" width={110} tick={{ fontSize: 12 }} />
          <Tooltip
            formatter={(value) => new Intl.NumberFormat('en-US').format(Number(value))}
            contentStyle={{ borderRadius: 8, fontSize: 12 }}
          />
          <Bar dataKey="cases" fill="#4f46e5" radius={[0, 4, 4, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
