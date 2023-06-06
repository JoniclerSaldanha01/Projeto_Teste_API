package br.com.curso.rest.tests;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import br.com.curso.rest.core.BaseTest;
import io.restassured.RestAssured;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ApiTest extends BaseTest {

    private String TOKEN;
    @Before
    public void login(){
        Map<String, String> login = new HashMap<>();
        login.put("email", "joni.b.g.g.passos@gmail.com");
        login.put("senha", "123456");



        TOKEN = RestAssured.given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");
    }


    @Test
    public void naoDeveAcessarAPISemToken(){
        RestAssured
                .given()
                .when()
                    .get("/contas")
                .then()
                    .statusCode(401)
        ;
    }

    @Test
    public void deveIncluirContaCoSucesso(){
        RestAssured.given()
                    .header("Authorization", "JWT " + TOKEN) //bearer
                    .body("{ \"nome\": \"conta dp Joni 001\" }")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)
        ;

    }

    @Test
    public void deveAlterarContaCoSucesso(){
        RestAssured.given()
                    .header("Authorization", "JWT " + TOKEN) //bearer
                    .body("{ \"nome\": \"conta alterada\" }")
                .when()
                    .put("/contas/1762022")
                .then()
                    .statusCode(200)
                    .body("nome", Matchers.is("conta alterada"))
        ;

    }

    @Test
    public void naoDeveInserirContaMesmoNome(){
        RestAssured.given()
                    .header("Authorization", "JWT " + TOKEN) //bearer
                    .body("{ \"nome\": \"conta alterada\" }")
                .when()
                    .post("/contas")
                .then()
                   .log().all()
                   .statusCode(400)
                .body("error", Matchers.is("Já existe uma conta com esse nome!"))
        ;

    }

    @Test
    public void deveInserirMovimentacaoSucesso(){
        Movimentacao movimentacao = getMovimentacaoValida();

        RestAssured.given()
                   .header("Authorization", "JWT " + TOKEN) //bearer
                   .body(movimentacao)
                .when()
                   .post("/transacoes")
                .then()
                  .statusCode(201)

        ;

    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao(){
        RestAssured.given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .body("msg", hasItems(
                       "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"
                ))

        ;

    }

    @Test
    public void naoDeveInserirMovimentacaoComDataFutura(){
        Movimentacao movimentacao = getMovimentacaoValida();
        movimentacao.setData_transacao("08/06/2023");
        RestAssured.given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                .body(movimentacao)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))

        ;

    }

    private Movimentacao getMovimentacaoValida(){
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(1762022);
        // movimentacao.setUsuario_id();
        movimentacao.setDescricao("Descricao da movimentacao");
        movimentacao.setEnvolvido("Envolvido na movimentacao");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao("01/01/2000");
        movimentacao.setData_pagamento("10/05/2010");
        movimentacao.setValor(100f);
        movimentacao.setStatus(true);
        return movimentacao;
    }
}
