package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

/*Padrão do instrutor
 * DTO - Dados que sai da aplicação para o cliente. 
 * Form - Dados que entrão na aplicação. 
 * */
@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	@Autowired
	private CursoRepository cursoRepository;

	/*required - Obrigatorio ou não
	 * @RequestParam - Vem pela url
	 * pagina niicia em 0. 
	 * ordenacao - atributos do tópicos
	 * */ 
	@GetMapping
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso, 
			@PageableDefault(sort = "id", direction = Direction.ASC,page =0, size = 10)	Pageable paginacao 
			/*@RequestParam int pagina, @RequestParam int qtd, @RequestParam String ordenacao*/ ) {
		
		//Uma opção
		//Pageable paginacao = PageRequest.of(pagina, qtd, Direction.DESC, ordenacao); 
		
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDTO.converter(topicos); // DTO recebe topicos e retorna uma lista de DTOS. 
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,paginacao);
			return TopicoDTO.converter(topicos);
		}
	}
	
	@GetMapping("/{id}")
	//@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<DetalhesTopicoDTO> listarPorId(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id); 
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesTopicoDTO(topico.get())); 
		}
		return ResponseEntity.notFound().build(); 
	}
	
	//Existem formas diferentes de pegar URI.  
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);// TopicoForm retorna um Topico.  
		topicoRepository.save(topico); 
		
		//Retorna status 201 e DTO de resposta e cabeçalho location(endereco do no obj criado)
		URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri(); 
		return  ResponseEntity.created(uri).body(new TopicoDTO(topico)) ; 
	}
	
	@PutMapping("/{id}")
	@Transactional // Commita a atualização do topico na base sem precisar chamar o save do repository. 
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id,@RequestBody @Valid AtualizacaoTopicoForm topicoForm ){
		Optional<Topico> optional = topicoRepository.findById(id); 
		if(optional.isPresent()) {
			Topico topico = topicoForm.atualizar(id,topicoRepository); 
			//Retorna um OK, porque não é uma criação de objeto novo. 
			return ResponseEntity.ok(new TopicoDTO(topico)); 
		}
		return ResponseEntity.notFound().build(); 
		

	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDTO> remover(@PathVariable Long id ){
		Optional<Topico> optional = topicoRepository.findById(id); 
		if(optional.isPresent()) {
			topicoRepository.deleteById(id); 
			return ResponseEntity.ok().build(); 
		}
		return ResponseEntity.notFound().build(); 
	}
	
	
	 
}
