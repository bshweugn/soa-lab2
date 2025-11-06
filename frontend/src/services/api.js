const BASE_MAIN = 'https://localhost:28445/organizations'
const BASE_DIR = 'https://localhost:23223/orgdirectory'

async function request(url, options = {}) {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })

  if (!res.ok) {
    const text = await res.text()
    throw new Error(`${res.status} ${res.statusText}: ${text}`)
  }

  const content = await res.text()
  try {
    return content ? JSON.parse(content) : null
  } catch {
    return content
  }
}

export const api = {
  search: async ({
    page = 0,
    size = 5,
    name = '',
    type = '',
    minTurnover = null,
    maxTurnover = null,
    sort = [],
  } = {}) => {
    const mainPromise = request(`${BASE_MAIN}/search`, {
      method: 'POST',
      body: JSON.stringify({
        filters: { name, type },
        page,
        size,
        sort,
      }),
    })

    const dirPromises = []

    if (type) {
      dirPromises.push(
        request(`${BASE_DIR}/find-by/type`, {
          method: 'POST',
          body: JSON.stringify({ type }),
        })
      )
    }

    if (minTurnover !== null || maxTurnover !== null) {
      dirPromises.push(
        request(`${BASE_DIR}/find-by/min-and-max-turnover`, {
          method: 'POST',
          body: JSON.stringify({
            minTurnover: minTurnover ?? 0,
            maxTurnover: maxTurnover ?? Number.MAX_SAFE_INTEGER,
          }),
        })
      )
    }

    const [mainResult, ...dirResults] = await Promise.allSettled([
      mainPromise,
      ...dirPromises,
    ])

    const mainData =
      mainResult.status === 'fulfilled'
        ? mainResult.value?.content || mainResult.value || []
        : []

    const dirData = dirResults
      .filter((r) => r.status === 'fulfilled')
      .flatMap((r) => r.value || [])

    const merged = [...mainData, ...dirData]
    const unique = merged.filter(
      (item, index, arr) =>
        index === arr.findIndex((o) => o.id === item.id && item.id != null)
    )

    return unique
  },

  getById: (id) => request(`${BASE_MAIN}/${id}`),

  create: (organizationDto) =>
    request(BASE_MAIN, {
      method: 'POST',
      body: JSON.stringify(organizationDto),
    }),

  update: (id, organization) =>
    request(`${BASE_MAIN}/${id}`, {
      method: 'PUT',
      body: JSON.stringify(organization),
    }),

  delete: (id) => request(`${BASE_MAIN}/${id}`, { method: 'DELETE' }),

  deleteByFullName: (fullName) =>
    request(`${BASE_MAIN}/by/fullname?fullName=${encodeURIComponent(fullName)}`, {
      method: 'DELETE',
    }),

  getByMinEmployees: () => request(`${BASE_MAIN}/by/min-employees`),
  getByMaxFullName: () => request(`${BASE_MAIN}/by/max-fullname`),
}

export default api
