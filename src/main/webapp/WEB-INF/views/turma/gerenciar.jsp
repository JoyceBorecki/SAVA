<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Gerenciar Turma" />
<c:set var="currentNav" value="admin_turmas" />

<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
    <%-- Estilos da tabela (reaproveitado) e do form-inline --%>
    <style>
      .table-wrapper { width: 100%; overflow-x: auto; }
      .data-table { width: 100%; border-collapse: collapse; margin-top: 16px; }
      .data-table th, .data-table td {
        padding: 12px 16px;
        text-align: left;
        border-bottom: 1px solid var(--divider);
      }
      .data-table th {
        color: var(--text-heading);
        font-size: 13px;
        font-weight: 600;
        text-transform: uppercase;
        background: var(--app-bg);
      }
      .data-table td { font-size: 14px; }
      .data-table .actions a { color: #E53E3E; font-weight: 600; }
      .data-table .actions a:hover { color: #C53030; }
      
      .form-inline {
        display: flex;
        gap: 10px;
        padding: 16px;
        background: var(--app-bg);
        border-radius: 0 0 var(--card-radius) var(--card-radius);
      }
      .form-inline .select {
        flex-grow: 1; /* Campo de select ocupa mais espaço */
      }
      .form-inline .btn-sm {
        height: 48px; /* Alinhar com select */
        flex-shrink: 0;
      }
      /* Ajuste para o card não ter padding no corpo */
      .card-no-padding {
         background: var(--card-bg);
         border-radius: var(--card-radius);
         box-shadow: var(--card-shadow);
      }
      .card-header {
        padding: 16px 20px;
        border-bottom: 1px solid var(--divider);
      }
      .card-header h3 {
        color: var(--text-heading);
        font-size: 16px;
        margin: 0;
      }
    </style>
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/includes/_header.jspf" />
    
    <main class="page-wrap">
      <div class="container">
        
        <section class="mb-4">
          <h1 class="page-title">Gerenciar Turma</h1>
          <p class="page-subtitle">
            <c:out value="${turma.disciplina.nome}" /> (<c:out value="${turma.semestre}" />)
          </p>
        </section>

        <%-- 1. PAINEL DE PROFESSORES --%>
        <section class="card-no-padding section">
          <div class="card-header">
            <h3>Professores</h3>
          </div>
          
          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>E-mail</th>
                  <th>Ação</th>
                </tr>
              </thead>
              <tbody>
                <c:if test="${empty turma.professores}">
                  <tr><td colspan="3" class="text-muted">Nenhum professor vinculado.</td></tr>
                </c:if>
                <c:forEach var="prof" items="${turma.professores}">
                  <tr>
                    <td><c:out value="${prof.nome}" /></td>
                    <td><c:out value="${prof.email}" /></td>
                    <td class="actions">
                      <a href="turmas?action=removerParticipante&turmaId=${turma.id}&usuarioId=${prof.id}&tipo=professor"
                         onclick="return confirm('Remover este professor da turma?');">
                         Remover
                      </a>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
          
          <%-- Formulário para adicionar Professor --%>
          <form class="form-inline" action="turmas" method="post" novalidate>
            <input type="hidden" name="action" value="adicionarParticipante" />
            <input type="hidden" name="turmaId" value="${turma.id}" />
            <input type="hidden" name="tipo" value="professor" />
            <select name="usuarioId" class="select" required>
              <option value="" disabled selected>Selecione um professor para adicionar</option>
              <c:forEach var="prof" items="${todosProfessores}">
                <option value="${prof.id}"><c:out value="${prof.nome}"/></option>
              </c:forEach>
            </select>
            <button type="submit" class="btn btn-secondary btn-sm">Adicionar Professor</button>
          </form>
        </section>

        <%-- 2. PAINEL DE ALUNOS --%>
        <section class="card-no-padding section">
          <div class="card-header">
            <h3>Alunos Matriculados</h3>
          </div>
          
          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>E-mail</th>
                  <th>Ação</th>
                </tr>
              </thead>
              <tbody>
                <c:if test="${empty turma.alunos}">
                  <tr><td colspan="3" class="text-muted">Nenhum aluno matriculado.</td></tr>
                </c:if>
                <c:forEach var="aluno" items="${turma.alunos}">
                  <tr>
                    <td><c:out value="${aluno.nome}" /></td>
                    <td><c:out value="${aluno.email}" /></td>
                    <td class="actions">
                      <a href="turmas?action=removerParticipante&turmaId=${turma.id}&usuarioId=${aluno.id}&tipo=aluno"
                         onclick="return confirm('Remover este aluno da turma?');">
                         Remover
                      </a>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
          
          <%-- Formulário para adicionar Aluno --%>
          <form class="form-inline" action="turmas" method="post" novalidate>
            <input type="hidden" name="action" value="adicionarParticipante" />
            <input type="hidden" name="turmaId" value="${turma.id}" />
            <input type="hidden" name="tipo" value="aluno" />
            <select name="usuarioId" class="select" required>
              <option value="" disabled selected>Selecione um aluno para matricular</tr">
              <c:forEach var="aluno" items="${todosAlunos}">
                <option value="${aluno.id}"><c:out value="${aluno.nome}"/></option>
              </c:forEach>
            </select>
            <button type="submit" class="btn btn-secondary btn-sm">Matricular Aluno</button>
          </form>
        </section>

        <div class="form-footer">
           <a href="turmas?action=listar" class="btn btn-secondary btn-sm">Voltar para Turmas</a>
        </div>

      </div>
    </main>
  </body>
</html>