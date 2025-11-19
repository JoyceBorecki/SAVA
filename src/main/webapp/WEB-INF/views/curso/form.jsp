<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty curso.id ? 'Novo Curso' : 'Editar Curso'}" />
<c:set var="currentNav" value="admin_cursos" />

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
          <p class="page-subtitle">Preencha os dados do curso.</p>
        </section>

        <section class="card card-padding">
          <form class="form-grid" action="cursos" method="post">
            
            <input type="hidden" name="id" value="<c:out value='${curso.id}' />" />

            <%-- Baseado no "Nome do curso" do seu arquivo [cite: 110-111] --%>
            <div class="field">
              <label class="label" for="nome">Nome do curso</label>
              <input id="nome" class="input" type="text" name="nome"
                     placeholder="Ex.: Ciência da Computação" 
                     value="<c:out value='${curso.nome}' />" 
                     required />
              <span class="help-text">Nome completo como divulgado oficialmente. [cite: 111]</span>
            </div>
            
            <%-- Baseado nos botões do seu arquivo [cite: 115-116] --%>
            <div class="form-footer">
              <a href="cursos?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
              <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
            </div>
            
          </form>
        </section>
      </div>
    </main>
  </body>
</html>