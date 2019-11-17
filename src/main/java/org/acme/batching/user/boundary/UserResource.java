package org.acme.batching.user.boundary;

import org.acme.batching.user.entity.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    EntityManager entityManager;

    @GET
    @Transactional
    public List<User> hello() {
        List<User> users = new ArrayList<>();
        for (long i = 0; i < 6; i++) {

            if (i > 0 && i % 3 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            User user = new User();
            user.name = "" + i;
//            user.id = i;
            persistOrMerge(user);
            users.add(user);
        }
        entityManager.flush();
        entityManager.clear();
        return users;
    }

    private User persistOrMerge(User entity) {
        if (entity.id == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}