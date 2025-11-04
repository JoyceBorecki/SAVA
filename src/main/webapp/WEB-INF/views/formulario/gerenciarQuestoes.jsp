<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Gerenciar Questões" />
<c:set var="currentNav" value="admin_formularios" />

<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
    <%-- Estilos específicos para esta página --%>
    <style>
      .questao-card {
        border: 1px solid var(--divider);
        border-radius: var(--card-radius);
        margin-bottom: 24px;
      }
      .questao-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 16px;
        padding: 16px 20px;
        background: var(--app-bg);
      }
      .questao-header h3 {
        color: var(--text-heading);
        font-size: 16px;
        font-weight: 700;
        margin: 0;
      }
      .questao-header .meta {
        font-size: 13px;
        color: var(--text-subtle);
        display: flex;
        gap: 12px;
        flex-shrink: 0;
      }
      .questao-body {
        padding: 20px;
      }
      .alternativa-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 0;
        border-bottom: 1px dashed var(--divider);
        font-size: 14px;
      }
      .alternativa-item:last-child { border-bottom: 0; }
      .link-excluir {
        color: #E53E3E;
        font-size: 12px;
        font-weight: 600;
        text-decoration: none;
      }
      .form-inline {
        display: flex;
        gap: 10px;
        margin-top: 16px;
      }
      .form-inline .input {
        flex-grow: 1; /* Campo de texto ocupa mais espaço */
      }
      .form-inline .btn-sm {
        height: 48px; /* Alinhar com input */
        flex-shrink: 0;
      }
    </style>
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/includes/_header.jspf" />
    
    <main class="page-wrap">
      <div class="container">
        
        <section class="mb-4">
          <h1 class="page-title">Gerenciar Questões</h1>
          <p class="page-subtitle">
            Formulário: <strong style="color: var(--text-heading);"><c:out value="${formulario.titulo}"/></strong>
          </p>
        </section>

        <%-- 1. CARD PARA ADICIONAR NOVA QUESTÃO --%>
        <section class="card card-padding section">
          <h3>Adicionar Nova Questão</h3>
          <form class="form-grid" action="formularios" method="post" novalidate>
            <input type="hidden" name="action" value="adicionarQuestao" />
            <input type="hidden" name="formularioId" value="${formulario.id}" />

            <div class="field">
              <label class="label" for="enunciado">Enunciado da Questão</label>
              <textarea id="enunciado" name="enunciado" class="textarea" 
                        placeholder="Digite o enunciado da questão..." 
                        rows="3" required></textarea>
            </div>
            
            <div class="field">
              <label class="label">Tipo da Questão (RF08, RF09)</label>
              <div class="choice-group" role="radiogroup" aria-label="Tipo da Questão">
                <label class="option-control">
                  <input type="radio" name="tipo" value="ABERTA" checked />
                  <span class="option-label">Aberta (Resposta textual)</span>
                </label>
                <label class="option-control">
                  <input type="radio" name="tipo" value="UNICA" />
                  <span class="option-label">Múltipla Escolha (Resposta Única)</span>
                </label>
                <label class="option-control">
                  <input type="radio" name="tipo" value="MULTIPLA" />
                  <span class="option-label">Múltipla Escolha (Várias Respostas)</span>
                </label>
              </div>
            </div>
            
            <div class="field">
              <label class="label">Obrigatória (RF10)</label>
              <div class="choice-group">
                <label class="option-control checkbox">
                  <input type="checkbox" name="obrigatoria" checked />
                  <span class="option-label">Questão obrigatória</span>
                </label>
              </div>
            </div>

            <div class="form-footer">
              <button type="submit" class="btn btn-primary btn-sm">Adicionar Questão</button>
            </div>
          </form>
        </section>

        <%-- 2. LISTA DE QUESTÕES EXISTENTES --%>
        <section class="section">
          <h2>Questões Atuais</h2>
          
          <c:if test="${empty questoes}">
            <p class="text-muted">Este formulário ainda não possui questões.</p>
          </c:if>

          <c:forEach var="questao" items="${questoes}" varStatus="loop">
            <article class="questao-card">
              <header class="questao-header">
                <div>
                  <h3>Questão ${loop.count}</h3>
                  <p style="margin: 4px 0 0; font-size: 14px; font-weight: 500;">
                    <c:out value="${questao.enunciado}"/>
                  </p>
                </div>
                <div class="meta">
                  <span>
                    <c:choose>
                      <c:when test="${questao.tipo == 'ABERTA'}">Aberta</c:when>
                      <c:when test="${questao.tipo == 'UNICA'}">Resposta Única</c:when>
                      <c:when test="${questao.tipo == 'MULTIPLA'}">Múltipla Escolha</c:when>
                    </c:choose>
                    (${questao.obrigatoria ? 'Obrigatória' : 'Opcional'})
                  </span>
                  <a href="formularios?action=excluirQuestao&questaoId=${questao.id}&formularioId=${formulario.id}"
                     class="link-excluir"
                     onclick="return confirm('Tem certeza que deseja excluir esta questão?');">
                     Excluir Questão
                  </a>
                </div>
              </header>

              <%-- Se não for ABERTA, mostra gerenciador de alternativas --%>
              <c:if test="${questao.tipo != 'ABERTA'}">
                <div class="questao-body">
                  <h4 style="margin: 0 0 8px; color: var(--text-subtle); font-weight: 600; font-size: 13px;">
                    Alternativas
                  </h4>
                  
                  <%-- Lista de alternativas existentes --%>
                  <c:if test="${empty questao.alternativas}">
                    <p class="text-muted" style="font-size: 14px;">Nenhuma alternativa adicionada.</p>
                  </c:if>
                  
                  <div class="alternativa-list">
                    <c:forEach var="alt" items="${questao.alternativas}">
                      <div class="alternativa-item">
                        <span><c:out value="${alt.texto}"/></span>
                        <a href="formularios?action=excluirAlternativa&alternativaId=${alt.id}&formularioId=${formulario.id}"
                           class="link-excluir"
                           onclick="return confirm('Excluir esta alternativa?');">
                           Excluir
                        </a>
                      </div>
                    </c:forEach>
                  </div>
                  
                  <%-- Formulário para adicionar nova alternativa --%>
                  <form class="form-inline" action="formularios" method="post" novalidate>
                    <input type="hidden" name="action" value="adicionarAlternativa" />
                    <input type="hidden" name="formularioId" value="${formulario.id}" />
                    <input type="hidden" name="questaoId" value="${questao.id}" />
                    <input type="text" name="textoAlternativa" class="input" 
                           placeholder="Digite o texto da nova alternativa" required />
                    <button type="submit" class="btn btn-secondary btn-sm">Adicionar</button>
                  </form>
                  
                </div>
              </c:if>
            </article>
          </c:forEach>
          
          <div class="form-footer">
             <a href="formularios?action=listar" class="btn btn-secondary btn-sm">Voltar para Formulários</a>
          </div>
        </section>

      </div>
    </main>
  </body>
</html>