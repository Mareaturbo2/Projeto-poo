import { useNavigate } from 'react-router-dom'
import '../App.css'

function Menu() {
  const navigate = useNavigate()
  const usuario = JSON.parse(localStorage.getItem('usuario'))

  return (
    <div className="container">
      <h1 className="title">Bem-vindo, {usuario?.titular || 'Cliente'}!</h1>
      <p className="subtitle">Escolha uma opção</p>

      <div className="menu-buttons">
        <button className="btn menu-btn" onClick={() => navigate('/saldo')}>
          Consultar Saldo
        </button>
        <button className="btn menu-btn" onClick={() => navigate('/deposito')}>
          Depósito
        </button>
        <button className="btn menu-btn" onClick={() => navigate('/saque')}>
          Saque
        </button>
        <button className="btn menu-btn" onClick={() => navigate('/extrato')}>
          Extrato
        </button>
        <button
          className="btn login"
          onClick={() => {
            localStorage.removeItem('usuario')
            navigate('/')
          }}
        >
          Sair
        </button>
      </div>
    </div>
  )
}

export default Menu
