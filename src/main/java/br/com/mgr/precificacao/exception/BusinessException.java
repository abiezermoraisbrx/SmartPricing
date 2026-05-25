package br.com.mgr.precificacao.exception;

/**
 * Exceção de negócio para isolar erros de validação financeira 
 * de erros de infraestrutura ou sistema.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String mensagem) {
        super(mensagem);
    }
}
