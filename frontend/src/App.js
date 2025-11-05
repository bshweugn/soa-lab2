import React, { useEffect, useState } from 'react'
import './App.css'
import { api } from './services/api'
import OrganizationList from './components/OrganizationList'
import OrganizationForm from './components/OrganizationForm'
import SearchBar from './components/SearchBar'
import ErrorModal from './components/ErrorModal'

function App() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(false)
  const [editing, setEditing] = useState(null)

  const load = async () => {
    setLoading(true)
    try {
      const res = await api.search({ page: 0, size: 10 })
      setItems(res?.content || [])
    } catch (e) {
      console.error(e)
      setError(e)
    } finally {
      setLoading(false)
    }
  }


  useEffect(() => { load() }, [])

  const handleCreate = async (obj) => {
    await api.create(obj)
    await load()
    setEditing(null)
  }

  const handleUpdate = async (obj) => {
    if (!obj.id) {
      setError(new Error('ID отсутствует при обновлении'))
      return
    }
    await api.update(obj.id, obj)
    await load()
    setEditing(null)
  }

  const handleDelete = async (org) => {
    if (!window.confirm('Delete ' + org.name + '?')) return
    try {
      if (org.id) await api.delete(org.id)
      else if (org.fullName) await api.deleteByFullName(org.fullName)
      await load()
    } catch (e) { setError(new Error('Delete failed: ' + (e.message || e))) }
  }

  const handleSearch = async (params) => {
    setLoading(true)
    try {
      const res = await api.search(params)
      setItems(res?.content || [])
    } catch (e) {
      setError(new Error('Search failed: ' + (e.message || e)))
    } finally {
      setLoading(false)
    }
  }

  const [error, setError] = useState(null)

  const clearError = () => setError(null)

  return (
    <div className="App min-h-screen bg-white text-gray-900 p-6">
      <div className="max-w-4xl mx-auto">
        <header className="flex items-center justify-between mb-6">
          <h1 className="text-2xl font-semibold">Организации</h1>
          <div className="flex items-center gap-2">
            {/* discreet button for loading test data */}
            <button
              title="Показать тестовые данные"
              onClick={async () => {
                try {
                  const res = await fetch('/test-data.json')
                  const data = await res.json()
                  setItems(data)
                } catch (e) {
                  setError(e)
                }
              }}
              className="px-2 py-1 text-sm text-gray-600 bg-gray-100 border border-gray-200 rounded hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300"
            >
              ⋯
            </button>
            <button onClick={() => setEditing({})} className="px-3 py-1 bg-gray-900 text-white rounded">Создать</button>
            <button onClick={load} className="px-3 py-1 border border-gray-300 rounded text-sm">Обновить</button>
          </div>
        </header>

        <div className="mb-4">
          <SearchBar onSearch={handleSearch} />
        </div>

        {loading ? (
          <p className="text-center text-gray-500">Загрузка...</p>
        ) : (
          <OrganizationList items={items} onEdit={(o) => setEditing(o)} onDelete={handleDelete} />
        )}

        {editing !== null && (
          <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
            <div className="bg-white border border-gray-200 rounded-lg shadow-lg w-full max-w-2xl p-6">
              <h2 className="text-lg font-medium mb-4">{editing && editing.id ? 'Редактирование' : 'Создание'} организации</h2>
              <OrganizationForm initial={editing} onCancel={() => setEditing(null)} onSubmit={(obj) => (editing && editing.id ? handleUpdate(obj) : handleCreate(obj))} />
            </div>
          </div>
        )}
        <ErrorModal error={error} onClose={clearError} />
      </div>
    </div>
  )
}

export default App
