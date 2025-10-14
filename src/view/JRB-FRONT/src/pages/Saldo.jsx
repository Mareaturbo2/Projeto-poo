import { useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import '../App.css'

function Saldo() {
  const navigate = useNavigate()
  const [saldo, setSaldo] = useState(0)
  const usuario = JSON.parse(localStorage.getItem('usuario'))

  useEffect(() => {
    const buscarSaldo = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/contas/${usuario.cpf}`)
        if (response.ok) {
          const conta = await response.json()
          setSaldo(conta.saldo)
        } else {
          alert('Erro ao buscar saldo')
        }
      } catch (error) {
        console.error(error)
        alert('Erro ao conectar com o servidor')
      }
    }

    buscarSaldo()
  }, [usuario.cpf])

  return (
    <div className="page">
      <div className="card">
        <h2>Saldo da Conta</h2>
        <p>Seu saldo atual é:</p>
        <h2 style={{ color: '#2e8b57' }}>R$ {saldo.toFixed(2)}</h2>
        <button className="btn login" onClick={() => navigate('/menu')}>
          Voltar ao Menu
        </button>
      </div>
    </div>
  )
}

export default Saldo
