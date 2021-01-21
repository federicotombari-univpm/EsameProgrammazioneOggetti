package it.controller;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.service.AdminService;

/**
 * Controller riservato agli amministratori per visualizzare e modificare i parametri di configurazione del sistema a runtime, e per
 * gestire il timer. Consiste di quattro @RequestMapping che svolgono le varie operazioni servendosi della classe AdminService
 * (package 'service').
 * @author FedericoTombari
 */
@RestController
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	/**
	 * Metodo che permette di ottenere lo stato dei parametri di configurazione a runtime.
	 * @return i valori dei parametri di configurazione
	 */
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public ResponseEntity<Object> getConfig() {
		
		return new ResponseEntity<>(adminService.getConfigStatus(), HttpStatus.OK);
	}

	/**
	 * Metodo che permette di inizializzare nuovamente tutti i parametri di configurazione leggendoli dal file.
	 * @return i nuovi valori dei parametri di configurazione
	 */
	@RequestMapping(value = "/config", method = RequestMethod.PUT)
	public ResponseEntity<Object> initializeConfig() {
		
		return new ResponseEntity<>(adminService.reinitializeConfig(), HttpStatus.OK);
	}
	
	/**
	 * Metodo che permette di modificare alcuni parametri i configurazione a runtime attraverso un @RequestBody.
	 * @param configurationBody i nuovi parametri di configurazione
	 * @return una lista dei valori modificati, ed eventuali errori verificatisi nel processo
	 */
	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public ResponseEntity<Object> setConfig(@RequestBody JSONObject configurationBody) {
		
		return new ResponseEntity<>(adminService.checkAndChangeConfig(configurationBody), HttpStatus.OK);
	}
	
	/**
	 * Metodo che permette di eseguire azioni riguardanti il thread gestito da un timer, chiamando un metodo di AdminService per
	 * distinguere il tipo di azione.
	 * @param action il tipo di azione da eseguire
	 * @param forced per forzare il riavvio del thread nel caso in cui sia già in esecuzione
	 * @return informazioni sull'esito della richiesta e\o sullo stato del timer
	 */
	@RequestMapping(value = "/timertask/{action}", method = RequestMethod.POST)
	public ResponseEntity<Object> timerOperation(
			@PathVariable (value = "action") String action,
			@RequestParam(value = "forced", required = false) boolean forced) {
		
		return new ResponseEntity<>(adminService.timerTaskService(action, forced), HttpStatus.OK);
	}
}




