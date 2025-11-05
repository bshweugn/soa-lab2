import React from 'react'

export default function ErrorModal({ error, onClose }) {
  if (!error) return null
  return (
    <div className="fixed inset-0 bg-black/40 z-50 flex items-center justify-center">
      <div className="bg-white border border-gray-200 rounded-lg w-full max-w-md p-4">
        <div className="flex items-start gap-3">
          <div className="flex-1">
            <h3 className="text-lg font-semibold">Ошибка</h3>
            <p className="mt-2 text-sm text-gray-700 whitespace-pre-wrap">{error.message || String(error)}</p>
          </div>
          <div>
            <button onClick={onClose} className="text-sm px-3 py-1 border border-gray-300 rounded">Закрыть</button>
          </div>
        </div>
      </div>
    </div>
  )
}
