import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../App.css";

function Extrato() {
  const navigate = useNavigate();
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const [movimentacoes, setMovimentacoes] = useState([]);

  useEffect(() => {
    const buscarExtrato = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/contas/${usuario.cpf}/extrato`);
        if (response.ok) {
          const data = await response.json();
          setMovimentacoes(data);
        } else {
          alert("Erro ao carregar extrato.");
        }
      } catch (error) {
        console.error(error);
        alert("Erro ao conectar com o servidor.");
      }
    };

    buscarExtrato();
  }, [usuario.cpf]);

  // 👇 Função para abrir o PDF
  const exportarPDF = () => {
    window.open(`http://localhost:8080/api/contas/${usuario.cpf}/extrato/pdf`, "_blank");
  };

  return (
    <div className="page">
      <div className="card">
        <h2>Extrato Bancário</h2>

        {movimentacoes.length === 0 ? (
          <p>Nenhuma movimentação encontrada.</p>
        ) : (
          <>
            <table className="extrato-table">
              <thead>
                <tr>
                  <th>Data</th>
                  <th>Tipo</th>
                  <th>Valor (R$)</th>
                </tr>
              </thead>
              <tbody>
                {movimentacoes.map((mov, index) => (
                  <tr key={index}>
                    <td>{mov.dataHora}</td>
                    <td>{mov.tipo}</td>
                    <td
                      style={{
                        color: mov.valor < 0 ? "red" : "green",
                        fontWeight: "bold",
                      }}
                    >
                      {mov.valor.toFixed(2)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            <button className="btn cadastro" onClick={exportarPDF}>
              Exportar em PDF
            </button>
          </>
        )}

        <button className="btn login" onClick={() => navigate("/menu")}>
          Voltar ao Menu
        </button>
      </div>
    </div>
  );
}

export default Extrato;
