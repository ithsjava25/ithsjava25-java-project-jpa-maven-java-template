package persistence.repository;

import org.example.model.Artist;
import javax.persistence.EntityManager;
import java.util.List;

public class ArtistRepository {
    private final EntityManager em;

    public ArtistRepository(EntityManager em) {
        this.em = em;
    }

    public Artist save(Artist artist) {
        em.persist(artist);
        return artist;
    }

    public Artist findById(Long id) {
        return em.find(Artist.class, id);
    }

    public List<Artist> findAll() {
        return em.createQuery("SELECT a FROM Artist a", Artist.class).getResultList();
    }

    public boolean deleteArtist(Long id) {
        try {
            Artist artist = findById(id);
            if (artist != null) {
                em.remove(artist);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
