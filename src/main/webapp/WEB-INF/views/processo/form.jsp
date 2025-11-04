<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty processo.id ? 'Novo Processo' : 'Editar Processo'}" />
<c:set var="currentNav" value="admin_processos" />

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
          <p class="page-subtitle">Preencha os dados do processo avaliativo.</p>
        </section>

        <section class="card card-padding">
          <form class="form-grid" action="processos" method="post" novalidate>
            
            <input type="hidden" name="id" value="<c:out value='${processo.id}' />" />

            <%-- Campo Nome --%>
            <div class="field">
              <label class="label" for="nome">Nome do Processo</label>
              <input id="nome" class="input" type="text" name="nome"
                     placeholder="Ex.: Avaliação Semestral 2025/2" 
                     value="<c:out value='${processo.nome}' />" 
                     required />
              <span class="help-text">Nome de identificação do período.</span>
            </div>

            <%-- Campo Data Início --%>
            <div class="field">
              <label class="label" for="dataInicio">Data de Início</label>
              <input id="dataInicio" class="input" type="date" name="dataInicio"
                     value="<c:out value='${processo.dataInicio}' />" 
                     required />
              <span class="help-text">Data em que os formulários serão abertos.</span>
            </div>
            
            <%-- Campo Data Fim --%>
            <div class="field">
              <label class="label" for="dataFim">Data de Fim</label>
              <input id="dataFim" class="input" type="date" name="dataFim"
                     value="<c:out value='${processo.dataFim}' />" 
                     required />
              <span class="help-text">Data de encerramento das respostas.</span>
            </div>
            
            <%-- Botões de Ação --%>
            <div class="form-footer">
              <a href="processos?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
              <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
            </div>
            
          </form>
        </section>
      </div>
    </main>
  </body>
</html>