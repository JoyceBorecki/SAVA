<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Gestão de Usuários" />
<c:set var="currentNav" value="admin_usuarios" /> 
<!DOCTYPE html>
<html lang="pt-BR">
  <head>

    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />

    <style>
      .table-wrapper {
        width: 100%;
        overflow-x: auto;
      }
      .data-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 16px;
      }
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
      .data-table td {
        font-size: 14px;
      }
      .data-table .actions {
        display: flex;
        gap: 16px;
      }
      .data-table .actions a {
        color: var(--brand);
        font-weight: 600;
      }
      .data-table .actions a:hover {
        color: var(--brand-hover);
      }
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
            <h1 class="page-title">Gestão de Usuários</h1>
            <p class="page-subtitle">Gerencie os perfis de acesso do sistema</p>
          </section>
          
          <div class="btn-row">
             <a href="usuarios?action=novo" class="btn btn-primary btn-sm">
                Novo Usuário
             </a>
          </div>
        </div>

        <section class="card card-padding">
          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>E-mail</th>
                  <th>Perfil</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="usuario" items="${usuarios}">
                  <tr>
                    <td><c:out value="${usuario.nome}" /></td>
                    <td><c:out value="${usuario.email}" /></td>
                    <td>
                      <c:out value="${usuario.perfil.nome}" />
                    </td>
                    <td class="actions">
                      <a href="usuarios?action=editar&id=${usuario.id}">Editar</a>
                      <a href="usuarios?action=excluir&id=${usuario.id}" 
                         onclick="return confirm('Tem certeza que deseja excluir este usuário?');">
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