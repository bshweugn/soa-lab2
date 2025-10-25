import React from 'react'
import OrganizationCard from './OrganizationCard'

export default function OrganizationList({ items = [], onEdit, onDelete }) {
  if (!items || items.length === 0) return <p className="text-gray-500">Организации не найдены</p>
  return (
    <div className="grid grid-cols-1 gap-4">
      {items.map((it) => (
        <OrganizationCard key={it.id || it.name} org={it} onEdit={onEdit} onDelete={onDelete} />
      ))}
    </div>
  )
}
