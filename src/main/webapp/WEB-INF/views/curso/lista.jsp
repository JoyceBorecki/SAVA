<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Gestão de Cursos" />
<c:set var="currentNav" value="admin_cursos" />
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
    
    <%-- Estilo da tabela (reaproveitado do lista.jsp de usuário) --%>
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
      .data-table .actions { display: flex; gap: 16px; }
      .data-table .actions a { color: var(--brand); font-weight: 600; }
      .data-table .actions a:hover { color: var(--brand-hover); }
      .page-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 16px;
        flex-wrap: wrap;
      }
    </style>
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/includes/_header.jspf" />
    
    <main class="page-wrap">
      <div class="container">
        
        <div class="page-header">
          <section class="mb-4">
            <h1 class="page-title">Gestão de Cursos</h1>
            <p class="page-subtitle">Gerencie os cursos da instituição.</p>
          </section>
          
          <div class="btn-row">
             <a href="cursos?action=novo" class="btn btn-primary btn-sm">
                Novo Curso
             </a>
          </div>
        </div>

        <section class="card card-padding">
          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Nome do Curso</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="curso" items="${cursos}">
                  <tr>
                    <td><c:out value="${curso.nome}" /></td>
                    <td class="actions">
                      <a href="cursos?action=editar&id=${curso.id}">Editar</a>
                      <a href="cursos?action=excluir&id=${curso.id}" 
                         onclick="return confirm('Tem certeza que deseja excluir este curso?');">
                         Excluir
                      </a>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </section>
        
      </div>
    </main>
  </body>
</html>