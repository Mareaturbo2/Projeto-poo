import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import '../App.css'

function Saque() {
  const navigate = useNavigate()
  const usuario = JSON.parse(localStorage.getItem('usuario'))
  const [valor, setValor] = useState('')

  const handleSaque = async () => {
    if (!valor || valor <= 0) {
      alert('Informe um valor válido!')
      return
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/contas/${usuario.cpf}/saque?valor=${valor}`,
        { method: 'PUT' }
      )

      if (response.ok) {
        alert('Saque realizado com sucesso!')
        navigate('/menu')
      } else {
        const erro = await response.json()
        alert(erro)
      }
    } catch (error) {
      console.error(error)
      alert('Erro ao conectar com o servidor')
    }
  }

  return (
    <div className="page">
      <div className="card">
        <h2>Saque</h2>
        <p>Informe o valor que deseja sacar:</p>
        <input
          type="number"
          placeholder="Ex: 50.00"
          value={valor}
          onChange={(e) => setValor(e.target.value)}
        />
        <button className="btn cadastro" onClick={handleSaque}>
          Confirmar Saque
        </button>
        <button className="btn login" onClick={() => navigate('/menu')}>
          Voltar ao Menu
        </button>
      </div>
    </div>
  )
}

export default Saque
