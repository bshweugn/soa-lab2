import React, { useState } from 'react'

export default function SearchBar({ onSearch }) {
  const [name, setName] = useState('')
  const [type, setType] = useState('')
  const [minTurnover, setMinTurnover] = useState('')
  const [maxTurnover, setMaxTurnover] = useState('')

  const submit = (e) => {
    e.preventDefault()
    onSearch({
      name,
      type,
      minTurnover: minTurnover ? Number(minTurnover) : null,
      maxTurnover: maxTurnover ? Number(maxTurnover) : null,
    })
  }

  return (
    <form className="flex flex-wrap gap-2 items-center" onSubmit={submit}>
      <input
        className="px-3 py-2 border border-gray-200 rounded w-48 text-sm"
        placeholder="Название"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <select
        className="px-3 py-2 border border-gray-200 rounded text-sm"
        value={type}
        onChange={(e) => setType(e.target.value)}
      >
        <option value="">Любой тип</option>
        <option value="COMMERCIAL">Коммерческая</option>
        <option value="PUBLIC">Публичная</option>
        <option value="GOVERNMENT">Государственная</option>
        <option value="PRIVATE_LIMITED_COMPANY">Частная</option>
      </select>
      <input
        type="number"
        className="px-3 py-2 border border-gray-200 rounded w-28 text-sm"
        placeholder="Мин. оборот"
        value={minTurnover}
        onChange={(e) => setMinTurnover(e.target.value)}
      />
      <input
        type="number"
        className="px-3 py-2 border border-gray-200 rounded w-28 text-sm"
        placeholder="Макс. оборот"
        value={maxTurnover}
        onChange={(e) => setMaxTurnover(e.target.value)}
      />
      <button
        type="submit"
        className="px-3 py-2 bg-gray-900 text-white rounded text-sm"
      >
        Поиск
      </button>
    </form>
  )
}
