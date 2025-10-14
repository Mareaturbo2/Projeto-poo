import './App.css'
import { useNavigate } from 'react-router-dom'

function App() {
  const navigate = useNavigate()

  return (
    <div className="container">
      <h1 className="title">JRB</h1>
      <p className="subtitle">O Dinheiro que voce nunca viu</p>

      <div className="buttons">
        <button className="btn login" onClick={() => navigate('/login')}>
          Login
        </button>
        <button className="btn cadastro" onClick={() => navigate('/cadastro')}>
          Cadastro
        </button>
      </div>
    </div>
  )
}

export default App
