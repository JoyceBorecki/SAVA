<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Gerenciar Questões" />
<c:set var="currentNav" value="admin_formularios" />

<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
    <style>
      /* Estilo para a área de alternativas dinâmicas */
      #areaAlternativas {
        display: none; /* Escondido por padrão (aparece se não for Aberta) */
        background: #F9FAFB;
        padding: 16px;
        border-radius: 8px;
        border: 1px dashed var(--divider);
        margin-top: 12px;
      }
      
      .input-alternativa-row {
        display: flex;
        gap: 8px;
        margin-bottom: 8px;
      }
      
      .btn-remove-alt {
        background: #FFF5F5;
        color: #E53E3E;
        border: 1px solid #FED7D7;
        border-radius: 6px;
        width: 40px;
        cursor: pointer;
        font-weight: bold;
        display: grid;
        place-items: center;
      }
      
      .questao-card {
        border: 1px solid var(--divider);
        border-radius: var(--card-radius);
        margin-bottom: 24px;
        overflow: hidden;
      }
      .questao-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px 20px;
        background: #fff;
        border-bottom: 1px solid var(--divider);
      }
      .questao-body { padding: 20px; background: #FAFAFA; }
      
      .badge-type {
        font-size: 11px;
        text-transform: uppercase;
        font-weight: 700;
        padding: 4px 8px;
        border-radius: 4px;
        background: #EDF2F7;
        color: #4A5568;
      }
    </style>
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/includes/_header.jspf" />
    
    <main class="page-wrap">
      <div class="container">
        
        <div class="mb-4" style="display:flex; justify-content:space-between; align-items:center;">
            <div>
                <a href="formularios" class="btn btn-secondary btn-sm" style="margin-bottom: 8px;">&larr; Voltar</a>
                <h1 class="page-title">Questões do Formulário</h1>
                <p class="page-subtitle">
                    <strong style="color: var(--text-heading);"><c:out value="${formulario.titulo}"/></strong>
                </p>
            </div>
        </div>

        <%-- 1. CARD DE CRIAÇÃO (MELHORADO) --%>
        <section class="card card-padding section">
          <h3 style="margin-bottom: 16px; border-bottom: 1px solid #eee; padding-bottom: 10px;">Nova Questão</h3>
          
          <form class="form-grid" action="formularios" method="post" id="formQuestao" novalidate>
            <input type="hidden" name="action" value="adicionarQuestao" />
            <input type="hidden" name="formularioId" value="${formulario.id}" />

            <div class="field">
              <label class="label" for="enunciado">Enunciado</label>
              <textarea id="enunciado" name="enunciado" class="textarea" 
                        placeholder="Digite a pergunta aqui..." 
                        rows="2" required></textarea>
            </div>
            
            <div class="field">
              <label class="label">Tipo de Resposta</label>
              <div class="choice-group" role="radiogroup" style="display:flex; gap: 20px; flex-wrap: wrap;">
                
                <label class="option-control">
                  <input type="radio" name="tipo" value="ABERTA" checked onchange="toggleAlternativas(false)" />
                  <span class="option-label">Texto Livre (Aberta)</span>
                </label>
                
                <label class="option-control">
                  <input type="radio" name="tipo" value="UNICA" onchange="toggleAlternativas(true)" />
                  <span class="option-label">Múltipla Escolha (Única)</span>
                </label>
                
                <label class="option-control">
                  <input type="radio" name="tipo" value="MULTIPLA" onchange="toggleAlternativas(true)" />
                  <span class="option-label">Caixas de Seleção (Várias)</span>
                </label>
              </div>
            </div>

            <%-- ÁREA DINÂMICA DE ALTERNATIVAS --%>
            <div id="areaAlternativas">
                <label class="label" style="margin-bottom: 8px; display:block;">Opções de Resposta</label>
                <div id="listaInputs">
                    <%-- Inputs serão injetados aqui via JS --%>
                </div>
                <button type="button" class="btn btn-secondary btn-sm" onclick="addInputAlternativa()" style="margin-top: 8px;">
                    + Adicionar Opção
                </button>
            </div>
            
            <div class="field">
              <div class="choice-group">
                <label class="option-control checkbox">
                  <input type="checkbox" name="obrigatoria" checked />
                  <span class="option-label">Resposta Obrigatória</span>
                </label>
              </div>
            </div>

            <div class="form-footer">
               <button type="submit" class="btn btn-primary">Salvar Questão</button>
            </div>
          </form>
        </section>

        <%-- 2. LISTA DE QUESTÕES EXISTENTES --%>
        <section class="section" style="margin-top: 40px;">
          <h2 style="font-size: 18px; margin-bottom: 16px;">Questões Cadastradas</h2>
          
          <c:if test="${empty questoes}">
            <div style="text-align:center; padding: 30px; color: #888; background: #fff; border-radius: 8px;">
                Nenhuma questão criada ainda. Use o formulário acima.
            </div>
          </c:if>

          <c:forEach var="questao" items="${questoes}" varStatus="loop">
            <article class="questao-card">
              <header class="questao-header">
                <div>
                  <span class="badge-type">Questão ${loop.count}</span>
                  <h3 style="font-size: 16px; margin: 8px 0 0; color: var(--text-heading);">
                    <c:out value="${questao.enunciado}"/>
                  </h3>
                  <small style="color: #666;">
                      Tipo: <c:out value="${questao.tipo}"/> | 
                      ${questao.obrigatoria ? 'Obrigatória' : 'Opcional'}
                  </small>
                </div>
                <div>
                  <a href="formularios?action=excluirQuestao&questaoId=${questao.id}&formularioId=${formulario.id}"
                     class="btn btn-secondary btn-sm"
                     style="color: #E53E3E; border-color: #FED7D7; background: #FFF5F5;"
                     onclick="return confirm('Tem certeza que deseja excluir esta questão?');">
                     Excluir
                  </a>
                </div>
              </header>

              <%-- Se não for ABERTA, mostra as alternativas cadastradas --%>
              <c:if test="${questao.tipo != 'ABERTA'}">
                <div class="questao-body">
                  <strong style="display:block; margin-bottom: 10px; font-size: 13px; color: #555;">Alternativas:</strong>
                  
                  <ul style="list-style: none; padding: 0; margin: 0;">
                    <c:forEach var="alt" items="${questao.alternativas}">
                      <li style="display: flex; justify-content: space-between; padding: 6px 0; border-bottom: 1px dashed #eee;">
                        <span>• <c:out value="${alt.texto}"/></span>
                        <a href="formularios?action=excluirAlternativa&alternativaId=${alt.id}&formularioId=${formulario.id}"
                           style="color: #E53E3E; font-size: 12px; text-decoration: none;"
                           onclick="return confirm('Excluir esta alternativa?');">
                           [x]
                        </a>
                      </li>
                    </c:forEach>
                  </ul>

                  <%-- Mini-form para adicionar MAIS alternativas depois --%>
                  <form action="formularios" method="post" style="margin-top: 15px; display: flex; gap: 8px;">
                    <input type="hidden" name="action" value="adicionarAlternativa" />
                    <input type="hidden" name="formularioId" value="${formulario.id}" />
                    <input type="hidden" name="questaoId" value="${questao.id}" />
                    <input type="text" name="textoAlternativa" class="input" placeholder="Nova opção..." required style="height: 36px;" />
                    <button type="submit" class="btn btn-secondary btn-sm" style="height: 36px;">Adicionar</button>
                  </form>
                </div>
              </c:if>
            </article>
          </c:forEach>
        </section>

      </div>
    </main>

    <%-- SCRIPTS PARA O FORMULÁRIO DINÂMICO --%>
    <script>
        const areaAlternativas = document.getElementById('areaAlternativas');
        const listaInputs = document.getElementById('listaInputs');

        function toggleAlternativas(mostrar) {
            if (mostrar) {
                areaAlternativas.style.display = 'block';
                // Se estiver vazio, adiciona 2 campos automaticamente para começar
                if (listaInputs.children.length === 0) {
                    addInputAlternativa();
                    addInputAlternativa();
                }
            } else {
                areaAlternativas.style.display = 'none';
            }
        }

        function addInputAlternativa() {
            const div = document.createElement('div');
            div.className = 'input-alternativa-row';
            
            div.innerHTML = `
                <input type="text" name="novasAlternativas" class="input" placeholder="Digite a opção..." required />
                <button type="button" class="btn-remove-alt" onclick="this.parentElement.remove()" title="Remover">×</button>
            `;
            
            listaInputs.appendChild(div);
        }
        
        // Inicializa estado
        toggleAlternativas(false);
    </script>
  </body>
</html>