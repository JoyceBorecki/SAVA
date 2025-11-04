<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- O título da página muda se estamos editando ou criando --%>
<c:set var="pageTitle" value="${empty usuario.id ? 'Novo Usuário' : 'Editar Usuário'}" />
<c:set var="currentNav" value="admin_usuarios" />

<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/includes/_header.jspf" />
    
    <main class="page-wrap">
      <div class="container">
        
        <section class="mb-4">
          <h1 class="page-title"><c:out value="${pageTitle}" /></h1>
          <p class="page-subtitle">Preencha os dados para criar ou atualizar um usuário.</p>
        </section>

        <section class="card card-padding">
          <%-- O formulário agora aponta para o nosso servlet --%>
          <form class="form-grid" action="usuarios" method="post" novalidate>
            
            <%-- Campo oculto para o ID (essencial para edição) --%>
            <input type="hidden" name="id" value="<c:out value='${usuario.id}' />" />

            <%-- Campo Nome --%>
            <div class="field">
              <label class="label" for="nome">Nome completo</label>
              <input id="nome" class="input" type="text" name="nome" 
                     placeholder="Digite o nome completo" 
                     value="<c:out value='${usuario.nome}' />" 
                     required />
              <span class="help-text">Informe o nome como consta nos registros.</span>
            </div>

            <%-- Campo E-mail --%>
            <div class="field">
              <label class="label" for="email">E-mail institucional</label>
              <input id="email" class="input" type="email" name="email" 
                     placeholder="email@ufpr.br" 
                     value="<c:out value='${usuario.email}' />" 
                     required />
              <span class="help-text">Este será utilizado para login no sistema.</span>
            </div>

            <%-- Campo Perfil (Dropdown dinâmico) --%>
            <div class="field">
              <label class="label" for="perfilId">Perfil</label>
              <select id="perfilId" class="select" name="perfilId" required>
                <option value="" disabled ${empty usuario.perfil ? 'selected' : ''}>
                  Selecione um perfil
                </option>
                <%-- Loop para carregar os perfis do banco --%>
                <c:forEach var="perfil" items="${perfis}">
                  <option value="${perfil.id}" 
                          ${perfil.id == usuario.perfil.id ? 'selected' : ''}>
                    <c:out value="${perfil.nome}" />
                  </option>
                </c:forEach>
              </select>
              <span class="help-text">Selecione o perfil de acesso adequado.</span>
            </div>

            <%-- Campo Senha --%>
            <div class="field">
              <label class="label" for="senha">Senha</label>
              <input id="senha" class="input" type="password" name="senha" 
                     placeholder="Digite uma senha" 
                     <c:if test="${empty usuario.id}">required</c:if> 
                     />
              <span class="help-text">
                <c:if test="${not empty usuario.id}">
                  Deixe em branco para manter a senha atual.
                </c:if>
                <c:if test="${empty usuario.id}">
                  A senha é obrigatória para novos usuários.
                </c:if>
              </span>
            </div>

            <%-- Botões de Ação --%>
            <div class="form-footer">
              <a href="usuarios?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
              <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
            </div>
            
          </form>
        </section>
      </div>
    </main>
  </body>
</html>