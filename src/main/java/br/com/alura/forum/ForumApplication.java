package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport //Habilita a leitura do Pageable por requisição. Paginação. 
@EnableCaching // Habilita uso de cache
@EnableSwagger2 // Habilita uso de swagger para gerar documentação
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
