package persistence.repository;

import org.example.model.Artist;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ArtistRepository {
    private final EntityManager em;

    public ArtistRepository(EntityManager em) {
        this.em = em;
    }

    public Artist save(Artist artist) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(artist);
            transaction.commit();
            return artist;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    public Artist findById(Long id) {
        return em.find(Artist.class, id);
    }

    public List<Artist> findAll() {
        return em.createQuery("SELECT a FROM Artist a", Artist.class).getResultList();
    }

    public boolean deleteArtist(Long id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Artist artist = em.find(Artist.class, id);
            if (artist != null) {
                em.remove(artist);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            return false;
        }
    }
}
