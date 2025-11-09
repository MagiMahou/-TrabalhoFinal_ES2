package com.engsoft2.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {
    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
    private CurrencyExchangeRepository repository;
    private Environment environment;

    public CurrencyExchangeController(CurrencyExchangeRepository repository, Environment environment) {
        this.repository = repository;
        this.environment = environment;
    }
    @PostMapping("/currency-exchange")
public CurrencyExchange createExchangeValue(@RequestBody CurrencyExchange newExchange) {
    logger.info("createExchangeValue called to create new rate");
   
    return repository.save(newExchange);
}


@PutMapping("/currency-exchange/{id}")
public CurrencyExchange updateExchangeValue(@PathVariable Long id, @RequestBody CurrencyExchange updatedExchange) {
    logger.info("updateExchangeValue called for id {}", id);

    // Procura o valor antigo
    return repository.findById(id).map(exchange -> {
        // Atualiza o valor
        exchange.setConversionMultiple(updatedExchange.getConversionMultiple());
        return repository.save(exchange);
    }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " not found"));
}

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        logger.info("retrieveExchangeValue called with from {} to {}", from, to);
        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);
        if (currencyExchange == null) {
            throw new ResourceNotFoundException("From " + from + "To " + to + " not found");
        }
        String port = environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);
        return currencyExchange;
    }

}
