package br.com.curso.rest.core;

import io.restassured.http.ContentType;

public interface Constantes {

    String APP_BASE_URL = "https://barrigarest.wcaquino.me";
    Integer APP_PORT = 443; //HTTP -> 80
    String APP_BASE_PATH = "";

    ContentType APP_CONTENT_TYPE = ContentType.JSON;

    Long MAX_TIMEOUT = 5000L;
}
