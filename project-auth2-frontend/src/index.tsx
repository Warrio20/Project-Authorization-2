
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './styles/main.css'
import { BrowserRouter } from 'react-router-dom'
import { DataProvider } from './data/DataContext.tsx'
ReactDOM.createRoot(document.getElementById('root')!).render(
    <BrowserRouter>
      <DataProvider>
        <App />
      </DataProvider>
    </BrowserRouter>
)