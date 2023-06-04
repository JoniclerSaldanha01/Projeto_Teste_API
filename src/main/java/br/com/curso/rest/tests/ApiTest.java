package br.com.curso.rest.tests;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import br.com.curso.rest.core.BaseTest;
import io.restassured.RestAssured;

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

        RestAssured.given()
                   .header("Authorization", "JWT " + TOKEN) //bearer
                   .body(movimentacao)
                .when()
                   .post("/transacoes")
                .then()
                  .statusCode(201)

        ;

    }
}
