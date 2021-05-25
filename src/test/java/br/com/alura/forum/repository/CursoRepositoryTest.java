package br.com.alura.forum.repository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.alura.forum.modelo.Curso;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)//Diz para não mudar as configurações do properties ativo para teste
@ActiveProfiles("test")// Diz para ler o properties de test. Não está funcionando corretamente. Tem algum problema relacionado na leitura dos properties. 
public class CursoRepositoryTest {

	@Autowired
	private CursoRepository cursoRepository; 
	
	//Pode  utilizar para armazenar dados no BD
	@Autowired
	private TestEntityManager em; 
	
	@Test
	public void deveriaCarregarUmCursoAoBuscarPeloNome() {
		//String nomeCurso = "HTML 5"; 
		//Curso curso = cursoRepository.findByNome(nomeCurso); 
		
		//OU 
		
		Curso programacao = new Curso(); 
		programacao.setNome("Programação");
		System.out.println("Curso: "+ programacao.getNome());
		em.persist(programacao); 
		
		Curso curso = cursoRepository.findByNome("Programação"); 
		Assert.assertNotNull(curso);
		//Assert.assertEquals(nomeCurso, curso.getNome());
		
	}
	
	@Test
	public void naoDeveriaCarregarUmCursoAoBuscarPeloNome() {
		String nomeCurso = "HTML "; 
		Curso curso = cursoRepository.findByNome(nomeCurso); 
		
		System.out.println("Curso: "+ nomeCurso);
		Assert.assertNull(curso);
		
	}

}
