package com.cl3.docente.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cl3.docente.entity.Categoria;
import com.cl3.docente.entity.Docente;
import com.google.gson.Gson;


@Controller
@RequestMapping("/docente")
public class DocenteController {
	
	private String URL="http://localhost:8091";
	
	@RequestMapping("/lista")
	public String lista(Model model) {
		RestTemplate rt=new RestTemplate();
		ResponseEntity<Docente[]> data=  rt.getForEntity(URL+"/docente/lista", Docente[].class);
		ResponseEntity<Categoria[]> dataCat=  rt.getForEntity(URL+"/docente/listaCategoria", Categoria[].class);
		model.addAttribute("docentes",data.getBody());
		model.addAttribute("categorias",dataCat.getBody());
		model.addAttribute("docente", new Docente());
		return "docente";
	}
	
	@RequestMapping("/registrar")
	public String registrar(@ModelAttribute Docente bean, RedirectAttributes redirect) {
		try {
			Gson gson=new  Gson();
			String json=gson.toJson(bean);
			RestTemplate rt=new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<>(json, headers);
			if(bean.getIddocente()==0) {
			rt.postForObject(URL+"/docente/registrar", request, String.class);
			redirect.addFlashAttribute("MENSAJE","Docente registrado");}
			else {
				rt.put(URL+"/docente/actualizar", request);
				redirect.addFlashAttribute("MENSAJE","Docente actualizado");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/docente/lista";
	}
	
	
}
