package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
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

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
	}
	
	/*
	 * Adicionando paginação nos resultados.
	 * **Os parâmetros virão da requisição, logo, o spring precisa saber desse detalhe**.
	 * A anotação @RequestParam serve para indicar que o 
	 * parametro virá na url.
	 * 
	 * TODOS os RequestParam são obrigatórios por padrão.
	 * Para evitar a obrigatoriedade, use o parâmetro required = false.
	 * 
	 * 
	 * @param int qtd -> quantidade de dados por página
	 * @param int page -> página atual
	 * @author Pedro victor
	 */
	@GetMapping("/paginado")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @RequestParam int qtd, @RequestParam int page) {
		/*
		 * O SpringData possui a interface Paginable, que pode
		 * ser extendida do objeto Page, que serve para indicar a paginação.
		 * Dentro dele, há a lista de resultados e os atribtos da 
		 * paginação, como a página atual, quantidade de 
		 * dados por page, etc.
		 */
		Pageable paginacao = PageRequest.of(page, qtd);
		
		if (nomeCurso == null) {			
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
	}
	
	@GetMapping("/ordenado")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, 
			@RequestParam int qtd, @RequestParam int page, @RequestParam String ordenacao) {
		/*
		 * O SpringData possui a interface Paginable, que pode
		 * ser extendida do objeto Page, que serve para indicar a paginação.
		 * Dentro dele, há a lista de resultados e os atribtos da 
		 * paginação, como a página atual, quantidade de 
		 * dados por page, etc.
		 * Neste caso, usaremos ainda mais dois parâmetros:
		 * A direção (crescente, decrescente), que é um ENUM do Spring
		 * e o critério de ordenação, ou seja, 
		 * o atributo que será usado para ordenar os registro.
		 */
		Pageable paginacao = PageRequest.of(page, qtd, Direction.ASC, ordenacao);
		
		if (nomeCurso == null) {			
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
	}
	
	/*
	 * Com esta anotação, a resposta do servidor vai ser 
	 * guardada em cache (atenção, deve ser do pacote spring).
	 * 
	 * Cada método precisa de um identificador único, para que o spring
	 * saiba quem é quem na memória. Desta forma, usamos o parâmetro value
	 * com uma String, que é o identificador 
	*/
	@GetMapping("/ordenadocomcache")
	@Cacheable(value="ListaTopicosCache")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, 
			@RequestParam int qtd, @RequestParam int page, 
			@RequestParam(required = false) String nenhumParametro1, 
			@RequestParam(required = false) String nenhumParametro2) {
		/*
		 * O SpringData possui a interface Paginable, que pode
		 * ser extendida do objeto Page, que serve para indicar a paginação.
		 * Dentro dele, há a lista de resultados e os atribtos da 
		 * paginação, como a página atual, quantidade de 
		 * dados por page, etc.
		 */
		Pageable paginacao = PageRequest.of(page, qtd);
		
		if (nomeCurso == null) {			
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}

}







