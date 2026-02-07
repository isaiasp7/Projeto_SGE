package Util;

import P_api.Exceptions.Erros.CampoVazio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {//Teste unitarios

    // ==================== Testes para gerarEmail ====================

    @Test
    void testGerarEmail_NomeCompleto() {
        // Arrange
        String nome = "João Silva";

        // Act
        String resultado = Utilities.gerar_email(nome);

        // Assert
        assertEquals("joão.silva@instituto_ensino.gov.br", resultado.toLowerCase());
        assertTrue(resultado.endsWith("@instituto_ensino.gov.br"));
    }

    @Test
    void testGerarEmail_NomeUnico() {
        // Arrange
        String nome = "Maria";

        // Act
        String resultado = Utilities.gerar_email(nome);

        // Assert
        assertEquals("maria@instituto_ensino.gov.br", resultado.toLowerCase());
    }

    @Test
    void testGerarEmail_NomeComTresPartes() {
        // Arrange
        String nome = "João Silva Santos";

        // Act
        String resultado = Utilities.gerar_email(nome);

        // Assert
        assertEquals("joão.silva@instituto_ensino.gov.br", resultado.toLowerCase());
    }

    /*
    AQUI FORÇOU A AMIZADADE - Corrigir
    ===================================
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void testGerarEmail_NomeNuloOuVazio(String nome) {
        // Act
         assertEquals(CampoVazio.class,Utilities.gerar_email(nome));



    }

    @Test
    void testGerarEmail_NomeComEspacosExtras() {
        // Arrange
        String nome = "  Pedro  Almeida  ";

        // Act
        String resultado = Utilities.gerar_email(nome);
        System.out.println(resultado);
        // Assert
        assertEquals("pedro.almeida@instituto_ensino.gov.br", resultado.toLowerCase());
    }*/

    @Test
    void testGerarEmail_MaiusculasMinusculas() {
        // Arrange
        String nome = "JOSÉ da Silva";

        // Act
        String resultado = Utilities.gerar_email(nome);

        // Assert
        assertEquals("josé.da@instituto_ensino.gov.br", resultado.toLowerCase());
    }

}