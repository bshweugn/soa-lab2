import React, { useState } from 'react'

function formatMoney(value) {
  if (value == null) return '$0'
  return '$' + Number(value).toLocaleString()
}

export default function OrganizationCard({ org, onEdit, onDelete }) {
  const [showRaw, setShowRaw] = useState(false)
  const [expanded, setExpanded] = useState(false)

  if (!org) return null

  return (
    <article className="flex flex-col gap-4 p-4 border border-gray-200 rounded-lg bg-white">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h3 className="text-lg font-semibold">{org.name}</h3>
          <div className="text-sm text-gray-600">{org.type} • {org.fullName}</div>
        </div>
        <div className="flex gap-3 items-center">
          <div className="flex flex-col items-end">
            <span className="text-xs text-gray-500">Сотрудники</span>
            <span className="text-xl font-medium">{org.employeesCount ?? 0}</span>
          </div>
          <div className="flex flex-col items-end">
            <span className="text-xs text-gray-500">Оборот</span>
            <span className="text-xl font-medium">{formatMoney(org.annualTurnover)}</span>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm text-gray-700">
        <div className="col-span-1">
          <div className="text-gray-500 text-xs">Адрес</div>
          <div className="mt-1 text-gray-800">
            {org.officialAddress?.street || '—'}
            {org.officialAddress?.zipCode ? (', ' + org.officialAddress.zipCode) : ''}
            {org.officialAddress?.town?.name ? (' • ' + org.officialAddress.town.name) : ''}
          </div>
        </div>

        <div>
          <div className="text-gray-500 text-xs">Координаты</div>
          <div className="mt-1 text-gray-800">x: {org.coordinates?.x ?? '—'}, y: {org.coordinates?.y ?? '—'}</div>
        </div>

        <div>
          <div className="text-gray-500 text-xs">Контакты</div>
          <div className="mt-1 text-gray-800">
            {org.website && <div>Сайт: <a className="text-gray-700 underline" href={org.website}>{org.website}</a></div>}
            {org.email && <div>Email: <a className="text-gray-700" href={`mailto:${org.email}`}>{org.email}</a></div>}
            {org.phone && <div>Телефон: <span className="text-gray-800">{org.phone}</span></div>}
            {!org.website && !org.email && !org.phone && <div className="text-gray-500">—</div>}
          </div>
        </div>
      </div>

      <div className="flex items-center justify-between">
        <div className="flex gap-2">
          <button onClick={() => setExpanded(s => !s)} className="px-3 py-1 text-sm border border-gray-200 rounded text-gray-700">{expanded ? 'Свернуть' : 'Подробнее'}</button>
          <button onClick={() => setShowRaw(s => !s)} className="px-3 py-1 text-sm border border-gray-200 rounded text-gray-700">{showRaw ? 'Скрыть JSON' : 'Показать JSON'}</button>
        </div>
        <div className="flex gap-2">
          <button onClick={() => onEdit(org)} className="px-3 py-1 text-sm bg-gray-900 text-white rounded">Изменить</button>
          <button onClick={() => onDelete(org)} className="px-3 py-1 text-sm border border-gray-300 rounded text-gray-700">Удалить</button>
        </div>
      </div>

      {expanded && (
        <div className="mt-2 border-t border-gray-100 pt-3 text-sm text-gray-700">
          <div><strong>ID:</strong> {org.id ?? '—'}</div>
          <div className="mt-1"><strong>Полное название:</strong> {org.fullName || '—'}</div>
          <div className="mt-1"><strong>Тип:</strong> {org.type || '—'}</div>
          <div className="mt-1"><strong>Сотрудники:</strong> {org.employeesCount ?? '—'}</div>
          <div className="mt-1"><strong>Годовой оборот:</strong> {formatMoney(org.annualTurnover)}</div>
        </div>
      )}

      {showRaw && (
        <pre className="mt-3 bg-gray-50 p-3 rounded text-xs overflow-auto border border-gray-100">{JSON.stringify(org, null, 2)}</pre>
      )}
    </article>
  )
}
