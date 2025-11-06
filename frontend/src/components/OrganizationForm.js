import React, { useState, useEffect } from 'react'

const empty = {
  name: '',
  coordinates: { x: 0, y: 0 },
  annualTurnover: 0,
  fullName: '',
  employeesCount: 0,
  type: 'COMMERCIAL',
  officialAddress: { street: '', zipCode: '', town: { x: 0, y: 0, z: 0, name: '' } },
}

export default function OrganizationForm({ initial = null, onCancel, onSubmit }) {
  const [form, setForm] = useState(() => {
    if (!initial) return empty
    return {
      ...empty,
      ...initial,
      coordinates: { ...empty.coordinates, ...initial.coordinates },
      officialAddress: {
        ...empty.officialAddress,
        ...initial.officialAddress,
        town: { ...empty.officialAddress.town, ...initial.officialAddress?.town }
      }
    }
  })

  useEffect(() => {
    if (!initial) {
      setForm(empty)
      return
    }
    setForm({
      ...empty,
      ...initial,
      coordinates: { ...empty.coordinates, ...initial.coordinates },
      officialAddress: {
        ...empty.officialAddress,
        ...initial.officialAddress,
        town: { ...empty.officialAddress.town, ...initial.officialAddress?.town }
      }
    })
  }, [initial])

  const setPath = (path, value) => {
    const next = JSON.parse(JSON.stringify(form))
    const parts = path.split('.')
    let cur = next
    parts.forEach((p, i) => {
      if (i === parts.length - 1) {
        cur[p] = value
      } else {
        cur[p] = cur[p] || {}
        cur = cur[p]
      }
    })
    setForm(next)
  }

  const submit = (e) => {
    e.preventDefault()
    onSubmit(form)
  }

  return (
    <form className="space-y-4" onSubmit={submit}>
      <div className="grid grid-cols-2 gap-4">
        <label className="flex flex-col text-sm">
          <span className="mb-1">Название</span>
          <input className="px-3 py-2 border border-gray-200 rounded" value={form.name} onChange={(e) => setPath('name', e.target.value)} required />
        </label>
        <label className="flex flex-col text-sm">
          <span className="mb-1">Полное название</span>
          <input className="px-3 py-2 border border-gray-200 rounded" value={form.fullName} onChange={(e) => setPath('fullName', e.target.value)} />
        </label>
      </div>

      <div className="grid grid-cols-3 gap-4">
        <label className="flex flex-col text-sm">
          <span className="mb-1">Тип</span>
          <select className="px-3 py-2 border border-gray-200 rounded" value={form.type} onChange={(e) => setPath('type', e.target.value)}>
            <option>COMMERCIAL</option>
            <option>PUBLIC</option>
            <option>GOVERNMENT</option>
            <option>PRIVATE_LIMITED_COMPANY</option>
          </select>
        </label>
        <label className="flex flex-col text-sm">
          <span className="mb-1">Сотрудники</span>
          <input className="px-3 py-2 border border-gray-200 rounded" type="number" value={form.employeesCount} onChange={(e) => setPath('employeesCount', Number(e.target.value))} />
        </label>
        <label className="flex flex-col text-sm">
          <span className="mb-1">Годовой оборот</span>
          <input className="px-3 py-2 border border-gray-200 rounded" type="number" value={form.annualTurnover} onChange={(e) => setPath('annualTurnover', Number(e.target.value))} />
        </label>
      </div>

      <fieldset className="border border-gray-100 p-3 rounded">
        <legend className="text-sm">Координаты</legend>
        <div className="grid grid-cols-2 gap-2 mt-2">
          <label className="flex flex-col text-sm"><span className="mb-1">X</span><input className="px-3 py-2 border border-gray-200 rounded" type="number" value={form.coordinates?.x} onChange={(e) => setPath('coordinates.x', Number(e.target.value))} /></label>
          <label className="flex flex-col text-sm"><span className="mb-1">Y</span><input className="px-3 py-2 border border-gray-200 rounded" type="number" value={form.coordinates?.y} onChange={(e) => setPath('coordinates.y', Number(e.target.value))} /></label>
        </div>
      </fieldset>

      <fieldset className="border border-gray-100 p-3 rounded">
        <legend className="text-sm">Адрес</legend>
        <div className="grid grid-cols-3 gap-2 mt-2">
          <label className="flex flex-col text-sm"><span className="mb-1">Улица</span><input className="px-3 py-2 border border-gray-200 rounded" value={form.officialAddress?.street} onChange={(e) => setPath('officialAddress.street', e.target.value)} /></label>
          <label className="flex flex-col text-sm"><span className="mb-1">Индекс</span><input className="px-3 py-2 border border-gray-200 rounded" value={form.officialAddress?.zipCode} onChange={(e) => setPath('officialAddress.zipCode', e.target.value)} /></label>
          <label className="flex flex-col text-sm"><span className="mb-1">Город</span><input className="px-3 py-2 border border-gray-200 rounded" value={form.officialAddress?.town?.name} onChange={(e) => setPath('officialAddress.town.name', e.target.value)} /></label>
        </div>
      </fieldset>

      <div className="flex gap-2 justify-end">
        <button className="px-4 py-2 bg-gray-900 text-white rounded" type="submit">Сохранить</button>
        <button className="px-4 py-2 border border-gray-300 rounded text-gray-700" type="button" onClick={onCancel}>Отмена</button>
      </div>
    </form>
  )
}
