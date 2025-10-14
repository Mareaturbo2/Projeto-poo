import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import '../App.css'

function Login() {
  const navigate = useNavigate()
  const [cpf, setCpf] = useState('')
  const [senha, setSenha] = useState('')

  const handleLogin = async () => {
    if (!cpf || !senha) {
      alert('Preencha todos os campos!')
      return
    }

    try {
      const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cpf, senha }),
      })

      if (response.ok) {
        const conta = await response.json()
        localStorage.setItem('usuario', JSON.stringify({ ...conta, cpf }))
        alert('Login realizado com sucesso!')
        navigate('/menu')
      } else {
        const erro = await response.json()
        alert(erro)
      }
    } catch (error) {
      alert('Erro ao conectar com o servidor!')
      console.error(error)
    }
  }

  return (
    <div className="container">
      <h1 className="title">Login</h1>
      <p className="subtitle">Acesse sua conta</p>

      <div className="form">
        <input
          type="text"
          placeholder="CPF"
          value={cpf}
          onChange={(e) => setCpf(e.target.value)}
        />
        <input
          type="password"
          placeholder="Senha"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
        />
        <button className="btn cadastro" onClick={handleLogin}>
          Entrar
        </button>
        <button className="btn login" onClick={() => navigate('/')}>
          Voltar
        </button>
      </div>
    </div>
  )
}

export default Login
