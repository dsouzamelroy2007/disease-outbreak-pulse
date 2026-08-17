import type { DiseaseInfo } from '../types'

interface Props {
  diseases: DiseaseInfo[]
  active: string
  onSelect: (code: string) => void
}

export function DiseaseSwitcher({ diseases, active, onSelect }: Props) {
  return (
    <nav className="flex flex-wrap gap-2">
      {diseases.map((disease) => {
        const isActive = disease.code === active
        return (
          <button
            key={disease.code}
            onClick={() => onSelect(disease.code)}
            className={`rounded-full px-4 py-1.5 text-sm font-medium transition-colors ${
              isActive
                ? 'bg-indigo-600 text-white'
                : 'bg-white text-slate-600 ring-1 ring-slate-200 hover:bg-slate-100 dark:bg-slate-900 dark:text-slate-300 dark:ring-slate-700 dark:hover:bg-slate-800'
            }`}
          >
            {disease.displayName}
          </button>
        )
      })}
    </nav>
  )
}
