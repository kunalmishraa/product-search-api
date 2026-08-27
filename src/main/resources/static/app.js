const API_BASE = '/api';

const resultPane = document.getElementById('resultPane');
const productsTableBody = document.getElementById('productsTableBody');
const apiBaseLabel = document.getElementById('apiBaseLabel');

apiBaseLabel.textContent = `API: ${API_BASE}`;

function setResult(value) {
  resultPane.textContent = typeof value === 'string' ? value : JSON.stringify(value, null, 2);
}

function readForm(form) {
  return new FormData(form);
}

function asNumber(value) {
  if (value === '' || value === null || value === undefined) return undefined;
  return Number(value);
}

function asOptionalString(value) {
  const text = String(value ?? '').trim();
  return text.length ? text : undefined;
}

async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  });

  const text = await response.text();
  const body = text ? JSON.parse(text) : null;

  if (!response.ok) {
    throw new Error(body?.message || body?.error || response.statusText);
  }

  return body;
}

function renderProducts(products) {
  if (!Array.isArray(products) || products.length === 0) {
    productsTableBody.innerHTML = '<tr><td colspan="6" class="muted">No products found.</td></tr>';
    return;
  }

  productsTableBody.innerHTML = products.map((product) => `
    <tr>
      <td>${product.id ?? ''}</td>
      <td>${product.name ?? ''}</td>
      <td>${product.brand ?? ''}</td>
      <td>${product.category ?? ''}</td>
      <td>${product.price ?? ''}</td>
      <td>${product.active ? 'Yes' : 'No'}</td>
    </tr>
  `).join('');
}

async function loadProducts() {
  const data = await requestJson(`${API_BASE}/products`);
  renderProducts(data);
  setResult(data);
}

async function loadPopular() {
  const data = await requestJson(`${API_BASE}/products/popular`);
  renderProducts(data);
  setResult(data);
}

async function loadAllInventory() {
  const data = await requestJson(`${API_BASE}/inventory`);
  setResult(data);
}

document.getElementById('productForm').addEventListener('submit', async (event) => {
  event.preventDefault();
  const form = readForm(event.currentTarget);
  const payload = {
    name: asOptionalString(form.get('name')),
    description: asOptionalString(form.get('description')),
    brand: asOptionalString(form.get('brand')),
    category: asOptionalString(form.get('category')),
    price: asNumber(form.get('price')),
    active: form.get('active') === 'on'
  };

  try {
    const created = await requestJson(`${API_BASE}/products`, {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    setResult(created);
    await loadProducts();
  } catch (error) {
    setResult({ error: error.message });
  }
});

document.getElementById('searchForm').addEventListener('submit', async (event) => {
  event.preventDefault();
  const form = readForm(event.currentTarget);
  const params = new URLSearchParams();
  ['q', 'category', 'brand', 'sort'].forEach((key) => {
    const value = asOptionalString(form.get(key));
    if (value !== undefined) params.set(key, value);
  });
  ['minPrice', 'maxPrice', 'page', 'size'].forEach((key) => {
    const value = form.get(key);
    if (value !== '' && value !== null) params.set(key, value);
  });

  try {
    const data = await requestJson(`${API_BASE}/products/search?${params.toString()}`);
    renderProducts(data);
    setResult(data);
  } catch (error) {
    setResult({ error: error.message });
  }
});

document.getElementById('inventoryForm').addEventListener('submit', async (event) => {
  event.preventDefault();
  const form = readForm(event.currentTarget);
  const productId = asOptionalString(form.get('productId'));
  const payload = {
    quantity: asNumber(form.get('quantity')),
    reservedQuantity: asNumber(form.get('reservedQuantity')) ?? 0,
    warehouseLocation: asOptionalString(form.get('warehouseLocation'))
  };

  try {
    const created = await requestJson(`${API_BASE}/products/${encodeURIComponent(productId)}/inventory`, {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    setResult(created);
  } catch (error) {
    setResult({ error: error.message });
  }
});

document.getElementById('lookupInventoryForm').addEventListener('submit', async (event) => {
  event.preventDefault();
  const form = readForm(event.currentTarget);
  const productId = asOptionalString(form.get('productId'));

  try {
    const data = await requestJson(`${API_BASE}/products/${encodeURIComponent(productId)}/inventory`);
    setResult(data);
  } catch (error) {
    setResult({ error: error.message });
  }
});

document.querySelectorAll('[data-action]').forEach((button) => {
  button.addEventListener('click', async () => {
    const action = button.dataset.action;
    try {
      if (action === 'load-products') await loadProducts();
      if (action === 'load-popular') await loadPopular();
      if (action === 'load-all-inventory') await loadAllInventory();
      if (action === 'fill-sample-product') {
        const form = document.getElementById('productForm');
        form.name.value = 'iPhone 15';
        form.description.value = 'Apple smartphone';
        form.brand.value = 'APPLE';
        form.category.value = 'ELECTRONICS';
        form.price.value = '79999';
        form.active.checked = true;
      }
    } catch (error) {
      setResult({ error: error.message });
    }
  });
});

loadProducts().catch((error) => setResult({ error: error.message }));
