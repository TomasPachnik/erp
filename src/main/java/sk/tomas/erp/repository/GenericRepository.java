package sk.tomas.erp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class GenericRepository {

    private Repositories repositories;

    @Autowired
    public GenericRepository(WebApplicationContext appContext) {
        repositories = new Repositories(appContext);
    }

    public JpaRepository getRepository(Class clazz) {
        if (repositories.getRepositoryFor(clazz).isPresent()) {
            return (JpaRepository) repositories.getRepositoryFor(clazz).get();
        }
        return null;
    }
}
