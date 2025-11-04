<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Login — Sistema de Avaliação Acadêmica" />

<!DOCTYPE html>

<html lang="pt-BR">

  <head>

    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />

    <style>

      /* Estilo de erro */
      .error-message {
        background: #FFF5F5;
        color: #E53E3E;
        border: 1px solid #FED7D7;
        padding: 12px 16px;
        border-radius: 10px;
        font-size: 14px;
        font-weight: 500;
        margin-bottom: 16px;
      }

     
      /* CONFIGURAÇÃO BASE (MOBILE FIRST - SLIDE) */
      .dual-panel-wrap {
        /* Largura padrão de um painel */
        width: min(400px, 98%); 
        overflow: hidden;
      }
      .login-grid {
        display: grid;
        grid-template-columns: 100% 100%; /* Layout de slide (2x a largura) */
        width: 200%;
        transition: transform 0.3s ease;
      }
      /* Classe que aplica o slide (para mobile/tablet) */
      .login-grid.show-cadastro {
        transform: translateX(-50%); /* Move a grade inteira para a esquerda */
      }
      
      /* O PAINEL DE CADASTRO SEMPRE DEVE SER O SEGUNDO */
      .card-container:nth-child(2) {
          grid-column: 2 / 3; 
      }
      
      /* Ajuste de altura no card de cadastro simplificado */
      .simplified-profile {
          margin-bottom: 20px; 
      }
      .simplified-profile .label {
          font-weight: 600;
          color: #2D3748;
      }
      .simplified-profile .help-text {
          font-size: 12px;
          margin-top: 4px;
      }


      /* EM TELAS MAIORES (DESKTOP - EXPANSÃO SOB DEMANDA) */
      @media (min-width: 768px) {
        
        /* 1. LARGURA PADRÃO: APENAS UM PAINEL (Login) */
        .dual-panel-wrap {
            width: min(450px, 98%); 
            transition: width 0.3s ease;
        }
        
        /* 2. O PAINEL DE CADASTRO É ESCONDIDO POR PADRÃO (em desktop) */
        .card-container:nth-child(2) {
            display: none; /* <--- ESSENCIAL: OCULTA O CADASTRO INICIALMENTE */
        }

        .login-grid {
            /* Define 1 coluna por padrão */
            grid-template-columns: 1fr; 
            width: 100%;
            transform: none !important;
        }
        
        /* 3. LARGURA QUANDO show-cadastro É ATIVADO */
        .dual-panel-wrap.show-cadastro {
          width: min(900px, 98%); /* Expande para 2 painéis */
        }
        
        /* 4. MUDANÇA DE GRID E EXIBIÇÃO QUANDO show-cadastro É ATIVADO */
        .dual-panel-wrap.show-cadastro .login-grid {
          grid-template-columns: 1fr 1fr; /* Mostra 2 colunas */
        }
        
        .dual-panel-wrap.show-cadastro .card-container:nth-child(2) {
            display: block; /* Painel de Cadastro é mostrado */
        }
        
        .card-container {
            padding: 10px;
        }
      }

    </style>

  </head>

  <body>

    <main class="login-wrap">

      <div id="dualPanelWrap" class="dual-panel-wrap">

        <div id="loginGrid" class="login-grid">

         

          <%-- ================================================= --%>
          <%-- PAINEL 1: LOGIN --%>
          <%-- ================================================= --%>

          <div class="card-container">
            <div class="card login-card card-padding">
              <div class="login-header">
                <div class="login-logo" aria-hidden="true">
                  <svg width="34" height="34" viewBox="0 0 64 64" fill="none">
                    <circle cx="32" cy="32" r="30" fill="#0C4DB2" opacity="0.12"/>
                    <path d="M8 24l24-10 24 10-24 10L8 24z" fill="#0C4DB2"/>
                    <path d="M20 30v8c0 2.21 5.37 6 12 6s12-3.79 12-6v-8" stroke="#0C4DB2" stroke-width="2" fill="none"/>
                    <circle cx="32" cy="22" r="2" fill="#0C4DB2"/>
                  </svg>
                </div>
                <h1 class="login-title">Sistema de Avaliação Acadêmica</h1>
                <div class="login-subtitle">acesse sua conta</div>
              </div>

              <%-- MENSAGEM DE ERRO --%>
              <c:if test="${not empty erro}">
                <div class="error-message" role="alert">
                  <c:out value="${erro}" />
                </div>
              </c:if>

              <%-- FORMULÁRIO DE LOGIN --%>
              <form class="form-grid" action="login" method="post" novalidate>
                <div class="field">
                  <label class="label" for="email">E-mail institucional</label>
                  <input id="email" class="input" type="email" name="email" 
                         placeholder="email@ufpr.br" required 
                         value="<c:out value="${param.email}" />" /> 
                </div>

                <div class="field">
                  <div class="label-row">
                    <label class="label" for="senha">Senha</label>
                  </div>
                  <div class="input-group">
                    <input id="senha" class="input" type="password" name="senha" placeholder="Digite sua senha" required />
                  </div>
                </div>

                <button class="btn btn-primary w-full" type="submit">Entrar</button>

                <div class="text-center mt-4">
                  <%-- LINK "CADASTRE-SE" (Usado para acionar o painel 2) --%>
                  <a href="#" class="text-muted" id="btn-cadastrese">
                    Não possui conta? **Cadastre-se**
                  </a>
                </div>
              </form>
            </div>
          </div>

         
          <%-- ================================================= --%>
          <%-- PAINEL 2: CADASTRO RÁPIDO --%>
          <%-- ================================================= --%>

          <div class="card-container">
            <div class="card login-card card-padding">
              <div class="login-header">
                <h1 class="login-title">Novo Usuário</h1>
                <div class="login-subtitle">cadastro rápido</div>
              </div>

              <%-- FORMULÁRIO DE CADASTRO PÚBLICO --%>
              <form class="form-grid" action="cadastro" method="post" novalidate>
                <div class="field">
                  <label class="label" for="nome">Nome completo</label>
                  <input id="nome" class="input" type="text" name="nome" placeholder="Seu nome completo" required />
                </div>

                <div class="field">
                  <label class="label" for="email-cad">E-mail institucional</label>
                  <input id="email-cad" class="input" type="email" name="email" 
                         placeholder="seu.email@ufpr.br" required 
                         value="<c:out value="${emailParaCadastro}" />" /> 
                </div>

                <div class="field">
                  <label class="label" for="senha-cad">Senha</label>
                  <input id="senha-cad" class="input" type="password" name="senha" placeholder="Crie uma senha" required />
                </div>

                <%-- Perfil (Simplificado para ajuste de altura e valor fixo de Aluno) --%>
                <div class="field simplified-profile">
                    <input type="hidden" name="perfilId" value="1" /> 
                    <div class="label" style="text-align: center;">Seu Perfil</div>
                    <div class="input" style="text-align: center; font-weight: 500; background-color: #f7f9fc;">
                       Aluno
                    </div>
                    <span class="help-text" style="text-align: center;">Não é possível alterar o perfil no cadastro rápido.</span>
                </div>

                <button class="btn btn-primary w-full" type="submit">Finalizar Cadastro</button>

                <div class="text-center mt-4">
                  <a href="#" class="text-muted" id="btn-voltar">
                    Já possuo conta? **Voltar ao Login**
                  </a>
                </div>
              </form>
            </div>
          </div>

        </div>
      </div>
    </main>

 

    <script>
      document.addEventListener('DOMContentLoaded', function() {
        const wrap = document.getElementById('dualPanelWrap');
        const grid = document.getElementById('loginGrid');
        const btnCadastro = document.getElementById('btn-cadastrese');
        const btnVoltar = document.getElementById('btn-voltar');
       
        // Função para alternar a visualização
        function toggleView(showCadastro) {
          
          if (window.innerWidth < 768) {
            // Mobile/Tablet: Ativa o slide no grid
            grid.classList.toggle('show-cadastro', showCadastro);
            wrap.classList.remove('show-cadastro');
          } else {
            // Desktop: Expande o wrap para mostrar os dois painéis
            wrap.classList.toggle('show-cadastro', showCadastro);
            // Garante que o grid não deslize em desktop
            grid.classList.remove('show-cadastro'); 
          }
          
          if (showCadastro) {
              window.scrollTo(0, 0);
          }
        }

        // Ação: Mostrar Cadastro ao clicar no link
        if (btnCadastro) {
          btnCadastro.addEventListener('click', function(e) {
            e.preventDefault();
            toggleView(true);
          });
        }
       
        // Ação: Voltar ao Login
        if (btnVoltar) {
          btnVoltar.addEventListener('click', function(e) {
            e.preventDefault();
            toggleView(false);
          });
        }

        // Ação: Mostrar Cadastro automaticamente se o Servlet enviar o flag
        <c:if test="${mostrarCadastro}">
          toggleView(true);
        </c:if>
        
        // Listener para garantir o comportamento correto ao redimensionar
        window.addEventListener('resize', function() {
            // Verifica se o painel de cadastro está ATIVADO
            const isCadastroActive = wrap.classList.contains('show-cadastro') || grid.classList.contains('show-cadastro') || ${mostrarCadastro};
            
            if (isCadastroActive) {
                // Reavalia a visualização para o novo tamanho de tela
                toggleView(true);
            } else {
                // Garante que o layout está em estado inicial
                wrap.classList.remove('show-cadastro');
                grid.classList.remove('show-cadastro');
            }
        });

      });
    </script>

  </body>

</html>