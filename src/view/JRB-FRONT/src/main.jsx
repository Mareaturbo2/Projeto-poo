import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import App from './App.jsx'
import Login from './pages/Login.jsx'
import Cadastro from './pages/Cadastro.jsx'
import Menu from './pages/Menu.jsx'
import Saldo from './pages/Saldo.jsx'
import Deposito from './pages/Deposito.jsx'
import Saque from './pages/Saque.jsx'
import './App.css'
import Extrato from "./pages/Extrato.jsx";


ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/login" element={<Login />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/saldo" element={<Saldo />} />
        <Route path="/deposito" element={<Deposito />} />
        <Route path="/saque" element={<Saque />} />
        <Route path="/extrato" element={<Extrato />} />

      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
)
