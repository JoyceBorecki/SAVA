<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Acesso Negado</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f0f2f5; text-align: center; padding-top: 60px; }
        .box { background: white; padding: 40px; border-radius: 8px; display: inline-block; }
        h1 { color: #c0392b; font-size: 48px; margin-bottom: 10px; }
        p { font-size: 18px; margin-top: 5px; }
        a { text-decoration: none; color: #0C4DB2; font-weight: bold; }
    </style>
</head>
<body>

<div class="box">
    <h1>Erro 403</h1>

    <p>
        ${mensagem403 != null ? mensagem403 : "Você não tem permissão para acessar esta página."}
    </p>

    <p><a href="${pageContext.request.contextPath}/dashboard">Voltar ao painel</a></p>
</div>

</body>
</html>
