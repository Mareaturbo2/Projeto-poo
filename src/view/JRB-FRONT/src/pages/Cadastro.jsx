import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import '../App.css'

function Cadastro() {
  const navigate = useNavigate()
  const [nome, setNome] = useState('')
  const [cpf, setCpf] = useState('')
  const [senha, setSenha] = useState('')

  const handleCadastro = async () => {
    if (!nome || !cpf || !senha) {
      alert('Preencha todos os campos!')
      return
    }

    try {
      const response = await fetch('http://localhost:8080/api/contas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          titular: nome,
          cpf,
          senha,
          saldoInicial: 0.0,
          tipo: 'corrente',
        }),
      })

      if (response.ok) {
        alert('Conta criada com sucesso!')
        navigate('/')
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
      <h1 className="title">Cadastro</h1>
      <p className="subtitle">Crie sua conta</p>

      <div className="form">
        <input
          type="text"
          placeholder="Nome completo"
          value={nome}
          onChange={(e) => setNome(e.target.value)}
        />
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
        <button className="btn cadastro" onClick={handleCadastro}>
          Cadastrar
        </button>
        <button className="btn login" onClick={() => navigate('/')}>
          Voltar
        </button>
      </div>
    </div>
  )
}

export default Cadastro
